package lol.xget.groceryapp.domain.use_case.products

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
import lol.xget.groceryapp.homeSeller.domain.ProductModel
import lol.xget.groceryapp.homeSeller.repository.SellerRepository
import lol.xget.groceryapp.homeSeller.presentation.EditProducts.EditProductState
import org.kpropmap.asMap
import java.io.IOException
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(
    private val repo: SellerRepository
) {
    private val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    private val ref = FirebaseStorage.getInstance()

    operator fun invoke(
        product: ProductModel,
        productPhoto: Uri?
    ): Flow<Resource<EditProductState>> = callbackFlow {
        try {
            try {

                    trySend(Resource.Loading())
                    val imgRef = ref.getReference("products/").child(currentUser)
                        .child(product.productId!!)
                    val productImgRef = ref.getReference("products/").child(currentUser)
                        .child(product.productId).child("product")
                    if (productPhoto != null) {
                        try {
                            imgRef.listAll().addOnSuccessListener {
                                if (it.items.size > 0) {
                                    productImgRef.putFile(productPhoto)
                                        .addOnSuccessListener {
                                            productImgRef.downloadUrl.addOnSuccessListener { uri ->
                                                product.productPhoto = uri.toString()
                                                launch {
                                                    repo.updateProduct(product.productId,currentUser,product.asMap())
                                                    trySend(
                                                        Resource.Success(
                                                            EditProductState(
                                                                successUpdated = true,
                                                                displayPb = false
                                                            )
                                                        )
                                                    )
                                                }
                                            }.addOnFailureListener {
                                                trySend(Resource.Error("Can't change your product photo"))

                                            }

                                        }.addOnFailureListener {
                                            trySend(Resource.Error("Can't change your product photo"))
                                        }

                                } else {
                                    productImgRef.putFile(productPhoto).addOnSuccessListener {
                                        trySend(
                                            Resource.Success(
                                                EditProductState(
                                                    successUpdated = true,
                                                    displayPb = false
                                                )
                                            )
                                        )
                                    }.addOnFailureListener {
                                        trySend(Resource.Error("Can't upload your product photo"))
                                    }
                                }
                            }


                        } catch (e: Exception) {
                            CrashlyticsReport.Session.Event.Log.builder()
                                .setContent(e.localizedMessage!!)
                        }
                    } else {
                        trySend(
                            Resource.Success(
                                EditProductState(
                                    successUpdated = true,
                                    displayPb = false
                                )
                            )
                        )
                    }
            } catch (e: FirebaseException) {
                cancel()
                trySend(Resource.Error(e.localizedMessage!!))
            }

            try {
                awaitClose { channel.close() }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("UpdateProductCase", ex.message ?: "")
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