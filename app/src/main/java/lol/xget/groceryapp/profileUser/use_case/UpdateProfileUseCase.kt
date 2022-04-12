package lol.xget.groceryapp.profileUser.use_case

import android.net.Uri
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.mainUser.domain.User
import lol.xget.groceryapp.mainUser.repository.UserRepository
import lol.xget.groceryapp.profileUser.presentation.ProfileState
import java.io.IOException
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val repo: UserRepository
) {
    private val ref = FirebaseStorage.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser?.uid

    @ExperimentalCoroutinesApi
    operator fun invoke(user: User, profilePhotoUri: Uri?): Flow<Resource<ProfileState>> =
        callbackFlow {
            try {
                try {
                    //test update and if can
                    trySend(Resource.Loading())
                    val profImgRef = ref.getReference("profileImages").child(user.accountType!!)
                        .child(user.uid!!).child("photo")
                    val imgRef =
                        ref.getReference("profileImages").child(user.accountType).child(user.uid!!)
                    if (profilePhotoUri != null) {
                        try {
                            imgRef.listAll().addOnSuccessListener {
                                if (it.items.size > 0) {
                                    //if exist profile image
                                    profImgRef.putFile(profilePhotoUri)
                                        .addOnSuccessListener {
                                            // Get the download URL and upload to
                                            profImgRef.downloadUrl.addOnSuccessListener {
                                                launch {
                                                    user.profilePhoto = it.toString()
                                                    repo.updateProfile(user, currentUser!!)
                                                        .addOnSuccessListener {
                                                            trySend(
                                                                Resource.Success(
                                                                    ProfileState(
                                                                        successUpdate = true,
                                                                        user = user
                                                                    )
                                                                )
                                                            )
                                                        }.addOnFailureListener {
                                                        trySend(Resource.Error("Can't change your profile "))
                                                    }
                                                }
                                            }
                                        }.addOnFailureListener {
                                            trySend(Resource.Error("Can't change your profile photo"))
                                        }

                                } else {
                                    //If no exist profile image
                                    profImgRef.putFile(profilePhotoUri).addOnSuccessListener {
                                        profImgRef.downloadUrl.addOnSuccessListener {
                                            launch {
                                                user.profilePhoto = it.toString()
                                                repo.updateProfile(user, currentUser!!)
                                                    .addOnSuccessListener {
                                                        trySend(
                                                            Resource.Success(
                                                                ProfileState(
                                                                    successUpdate = true,
                                                                    user = user
                                                                )
                                                            )
                                                        )
                                                    }.addOnFailureListener {
                                                    trySend(Resource.Error("Can't upload your profile photo"))
                                                }
                                            }
                                        }
                                    }.addOnFailureListener {
                                        trySend(Resource.Error("Can't upload your profile photo"))
                                    }
                                }
                            }


                        } catch (e: Exception) {
                            CrashlyticsReport.Session.Event.Log.builder()
                                .setContent(e.localizedMessage!!)
                        }
                    } else {
                        repo.updateProfile(user, currentUser!!).addOnSuccessListener {
                            trySend(
                                Resource.Success(
                                    ProfileState(
                                        successUpdate = true,
                                        loading = false
                                    )
                                )
                            )
                        }.addOnFailureListener {
                            trySend(Resource.Error("Can't update your profile"))
                        }
                    }
                } catch (e: FirebaseException) {
                    cancel()
                    trySend(Resource.Error(e.localizedMessage!!))
                }

                try {
                    awaitClose { channel.close() }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            } catch (e: FirebaseException) {
                trySend(
                    Resource.Error(
                        e.localizedMessage ?: "An unexpected error occured"
                    )
                ).isFailure
                awaitClose { cancel() }
            } catch (e: IOException) {
                awaitClose { cancel() }
                trySend(Resource.Error("Couldn't reach server. Check your internet connection.")).isFailure
            }

        }
}