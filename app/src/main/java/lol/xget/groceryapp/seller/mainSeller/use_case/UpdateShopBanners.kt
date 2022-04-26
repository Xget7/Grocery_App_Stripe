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
import lol.xget.groceryapp.seller.profileSeller.presentation.BannerOption.BannerState
import lol.xget.groceryapp.user.mainUser.domain.User
import java.io.IOException
import javax.inject.Inject

class UpdateShopBanners @Inject constructor(
    private val repo: SellerRepository
) {
    private val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    private val ref = FirebaseStorage.getInstance()

    operator fun invoke(
        user: User,
        adBanner: Uri?,
        shopBanner: Uri?,
    ): Flow<Resource<BannerState>> =
        callbackFlow {
            try {
                try {
                    trySend(Resource.Loading())

                    //adBanner photo upload
                    val addBannerImgRefChild =
                        ref.getReference("adBannersImages").child(user.accountType!!)
                            .child(user.uid!!).child("banner")
                    val addBannerRef =
                        ref.getReference("adBannersImages").child(user.accountType)
                            .child(user.uid!!)
                    //banner photo upload
                    val bannerImgRefChild =
                        ref.getReference("bannersImages").child(user.accountType!!)
                            .child(user.uid!!).child("banner")
                    val bannerImgRef =
                        ref.getReference("bannersImages").child(user.accountType)
                            .child(user.uid!!)

                    if (adBanner != null) {
                        try {
                            addBannerRef.listAll().addOnSuccessListener {
                                if (it.items.size > 0) {
                                    //if exist profile image
                                    addBannerImgRefChild.putFile(adBanner)
                                        .addOnSuccessListener {
                                            // Get the download URL and upload to
                                            addBannerImgRefChild.downloadUrl.addOnSuccessListener {
                                                launch {
                                                    user.shopAdBanner = it.toString()
                                                    repo.updateShopBanners(currentUser, user)
                                                        .addOnSuccessListener {
                                                            trySend(
                                                                Resource.Success(
                                                                    BannerState(
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
                                    addBannerImgRefChild.putFile(adBanner).addOnSuccessListener {
                                        addBannerImgRefChild.downloadUrl.addOnSuccessListener {
                                            launch {
                                                user.shopAdBanner = it.toString()
                                                repo.updateShopBanners(currentUser, user)
                                                    .addOnSuccessListener {
                                                        trySend(
                                                            Resource.Success(
                                                                BannerState(
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
                        trySend(Resource.Error("Can't update your banner"))
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
                                                    repo.updateShopBanners(currentUser, user)
                                                        .addOnSuccessListener {
                                                            trySend(
                                                                Resource.Success(
                                                                    BannerState(
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
                                                repo.updateShopBanners(currentUser, user)
                                                    .addOnSuccessListener {
                                                        trySend(
                                                            Resource.Success(
                                                                BannerState(
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
                    }

                    trySend(Resource.Success(BannerState(successUpdate = true)))
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
