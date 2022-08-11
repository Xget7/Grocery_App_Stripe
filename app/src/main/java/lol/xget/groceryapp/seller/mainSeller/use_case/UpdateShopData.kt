package lol.xget.groceryapp.seller.mainSeller.use_case

import android.net.Uri
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.seller.mainSeller.repository.SellerRepository
import lol.xget.groceryapp.seller.profileSeller.presentation.EditProfile.EditSellerProfileState
import lol.xget.groceryapp.user.mainUser.domain.User
import java.io.IOException
import javax.inject.Inject

class UpdateShopData @Inject constructor(
    private val repo: SellerRepository
) {
    private val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    private val ref = FirebaseStorage.getInstance()

    operator fun invoke(
        user: User,
        shopPhoto: Uri?,
        shopBanner: Uri?,
    ): Flow<Resource<EditSellerProfileState>> =
        callbackFlow {
            try {
                try {
                    trySend(Resource.Loading())

                    //Profile photo upload
                    val profImgRefChild =
                        user.uid?.let {
                            ref.getReference("profileImages").child(user.accountType!!)
                                .child(it).child("photo")
                        }
                    val imgRef =
                        user.accountType?.let {
                            ref.getReference("profileImages").child(it)
                                .child(user.uid!!)
                        }
                    //banner photo upload
                    val bannerImgRefChild =
                        ref.getReference("bannersImages").child(user.accountType!!)
                            .child(user.uid!!).child("photo")
                    val bannerImgRef =
                        ref.getReference("bannersImages").child(user.accountType)
                            .child(user.uid!!)

                    if (shopPhoto != null) {
                        try {
                            imgRef?.listAll()?.addOnSuccessListener {
                                if (it.items.size > 0) {
                                    //if exist profile image
                                    profImgRefChild?.putFile(shopPhoto)
                                        ?.addOnSuccessListener {
                                            // Get the download URL and upload to
                                            profImgRefChild.downloadUrl.addOnSuccessListener {
                                                launch {
                                                    user.profilePhoto = it.toString()
                                                    repo.updateShopData(currentUser, user)
                                                        .addOnSuccessListener {
                                                            trySend(
                                                                Resource.Success(
                                                                    EditSellerProfileState(
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
                                        }?.addOnFailureListener {
                                            trySend(Resource.Error("Can't change your profile photo"))
                                        }

                                } else {
                                    //If no exist profile image
                                    profImgRefChild?.putFile(shopPhoto)?.addOnSuccessListener {
                                        profImgRefChild.downloadUrl.addOnSuccessListener {
                                            launch {
                                                user.profilePhoto = it.toString()
                                                repo.updateShopData(currentUser, user)
                                                    .addOnSuccessListener {
                                                        trySend(
                                                            Resource.Success(
                                                                EditSellerProfileState(
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
                                    }?.addOnFailureListener {
                                        trySend(Resource.Error("Can't upload your profile photo"))
                                    }
                                }
                            }

                        } catch (e: Exception) {
                            CrashlyticsReport.Session.Event.Log.builder()
                                .setContent(e.localizedMessage!!)
                        }


                    } else {
                        repo.updateShopData(currentUser, user).addOnSuccessListener {
                            trySend(
                                Resource.Success(
                                    EditSellerProfileState(
                                        successUpdate = true,
                                        loading = false
                                    )
                                )
                            )
                        }.addOnFailureListener {
                            trySend(Resource.Error("Can't update your profile"))
                        }
                    }
                    if (shopBanner != null) {
                        try {
                            bannerImgRef.listAll().addOnSuccessListener {
                                if (it.items.size > 0) {
                                    //if exist banner image
                                    bannerImgRefChild.putFile(shopBanner)
                                        .addOnSuccessListener {
                                            // Get the download URL and upload to
                                            bannerImgRefChild.downloadUrl.addOnSuccessListener {
                                                launch {
                                                    user.shopBanner = it.toString()
                                                    repo.updateShopData(currentUser, user)
                                                        .addOnSuccessListener {
                                                            trySend(
                                                                Resource.Success(
                                                                    EditSellerProfileState(
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
                                            trySend(Resource.Error("Can't change your shop banner photo"))
                                        }

                                } else {
                                    //If no exist profile image
                                    bannerImgRefChild.putFile(shopBanner).addOnSuccessListener {
                                        bannerImgRefChild.downloadUrl.addOnSuccessListener {
                                            launch {
                                                user.shopBanner = it.toString()
                                                repo.updateShopData(currentUser, user)
                                                    .addOnSuccessListener {
                                                        trySend(
                                                            Resource.Success(
                                                                EditSellerProfileState(
                                                                    successUpdate = true,
                                                                    user = user
                                                                )
                                                            )
                                                        )
                                                    }.addOnFailureListener {
                                                        trySend(Resource.Error("Can't upload your shop banner  photo"))
                                                    }
                                            }
                                        }
                                    }.addOnFailureListener {
                                        trySend(Resource.Error("Can't upload your shop banner  photo"))
                                    }
                                }
                            }

                        } catch (e: Exception) {
                            CrashlyticsReport.Session.Event.Log.builder()
                                .setContent(e.localizedMessage!!)
                        }
                    } else {
                            repo.updateShopData(currentUser, user).addOnSuccessListener {
                                trySend(
                                    Resource.Success(
                                        EditSellerProfileState(
                                            successUpdate = true,
                                            loading = false
                                        )
                                    )
                                )
                            }.addOnFailureListener {
                                trySend(Resource.Error("Can't update your profile"))
                            }
                    }

                    trySend(Resource.Success(EditSellerProfileState(successUpdate = true)))
                } catch (e: FirebaseException) {
                    cancel()
                    trySend(Resource.Error(e.localizedMessage!!))
                }
                try {
                    awaitClose { channel.close() }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    Log.e("AddProductCase", ex.message ?: "")
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