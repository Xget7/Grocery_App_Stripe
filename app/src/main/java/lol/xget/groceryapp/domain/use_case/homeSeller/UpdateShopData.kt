package lol.xget.groceryapp.domain.use_case.homeSeller

import android.net.Uri
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.domain.model.ShopModel
import lol.xget.groceryapp.domain.model.UserModel
import lol.xget.groceryapp.domain.repository.SellerRepository
import lol.xget.groceryapp.presentation.main.Seller.AddProducts.AddProductState
import lol.xget.groceryapp.presentation.main.Seller.Home.SellerHomeState
import lol.xget.groceryapp.presentation.main.Seller.Profile.SellerProfileState
import lol.xget.groceryapp.presentation.main.User.Account.ProfileState
import org.kpropmap.asMap
import java.io.IOException
import javax.inject.Inject

class UpdateShopData @Inject constructor(
    private val repo: SellerRepository
) {
    private val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    private val ref = FirebaseStorage.getInstance()

    operator fun invoke (userModel: UserModel, shopPhoto : Uri?): Flow<Resource<SellerProfileState>> = callbackFlow {
        try {
            try {
                trySend(Resource.Loading())
                    //Profile photo upload
                    val profImgRef = ref.getReference("profileImages").child(userModel.accountType!!).child(userModel.uid!!).child("photo")
                    val imgRef =
                        ref.getReference("profileImages").child(userModel.accountType).child(userModel.uid!!)
                    if (shopPhoto != null ) {
                        try {
                            imgRef.listAll().addOnSuccessListener {
                                if (it.items.size > 0) {
                                    //if exist profile image
                                    profImgRef.putFile(shopPhoto)
                                        .addOnSuccessListener {
                                            // Get the download URL and upload to
                                            profImgRef.downloadUrl.addOnSuccessListener {
                                                launch {
                                                    userModel.profilePhoto = it.toString()
                                                    repo.updateShopData(currentUser,userModel).addOnSuccessListener {
                                                        trySend(Resource.Success(SellerProfileState(successUpdate = true, userModel = userModel)))
                                                    }.addOnFailureListener{
                                                        trySend(Resource.Error("Can't change your profile "))
                                                    }
                                                }
                                            }
                                        }.addOnFailureListener {
                                            trySend(Resource.Error("Can't change your profile photo"))
                                        }

                                } else {
                                    //If no exist profile image
                                    profImgRef.putFile(shopPhoto).addOnSuccessListener {
                                        profImgRef.downloadUrl.addOnSuccessListener {
                                            launch {
                                                userModel.profilePhoto = it.toString()
                                                repo.updateShopData(currentUser,userModel).addOnSuccessListener {
                                                    trySend(Resource.Success(SellerProfileState(successUpdate = true, userModel = userModel)))
                                                }.addOnFailureListener{
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
                    }else {
                        launch {
                            repo.updateShopData(currentUser,userModel).addOnSuccessListener {
                                trySend(
                                    Resource.Success(
                                        SellerProfileState(
                                            successUpdate = true,
                                            loading = false
                                        )
                                    )
                                )
                            }.addOnFailureListener {
                                trySend(Resource.Error("Can't update your profile"))
                            }
                        }
                    }

//                addOnFailureListener {
//                    trySend(Resource.Error(it.localizedMessage!!))
//                    cancel()
//                }

                    trySend(Resource.Success(SellerProfileState(successUpdate = true)))
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
            trySend(Resource.Error(e.localizedMessage ?: "An unexpected error occured")).isFailure
            awaitClose { cancel() }
        } catch (e: IOException) {
            awaitClose { cancel() }
            trySend(Resource.Error("Couldn't reach server. Check your internet connection.")).isFailure
        }
    }
}