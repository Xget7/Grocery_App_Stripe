package lol.xget.groceryapp.domain.use_case.products

import android.net.Uri
import android.util.Log
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
import lol.xget.groceryapp.domain.model.ProductModel
import lol.xget.groceryapp.domain.repository.SellerRepository
import lol.xget.groceryapp.presentation.main.Seller.AddProducts.AddProductState
import org.kpropmap.asMap
import java.io.IOException
import javax.inject.Inject

class AddProductUseCase @Inject constructor(
    private val repo: SellerRepository
) {
    private val currentUser = FirebaseAuth.getInstance().currentUser!!.uid

    private val ref = FirebaseStorage.getInstance()

    operator fun invoke(
        product: ProductModel,
        productPhoto: Uri?
    ): Flow<Resource<AddProductState>> = callbackFlow {

        val productImgRef =
            product.productId?.let {
                ref.getReference("products/").child(currentUser).child(it).child("product")
            }

            try {
                trySend(Resource.Loading())
                if (productPhoto != null) {
                    try {
                        productImgRef?.putFile(productPhoto)?.addOnSuccessListener {
                            productImgRef.downloadUrl.addOnSuccessListener {
                                launch {
                                    product.productPhoto = it.toString()
                                    repo.addProduct(product.asMap(), currentUser).addOnSuccessListener {
                                        trySend(Resource.Success(AddProductState(successAdded = true)))
                                    }.addOnFailureListener {
                                        trySend(Resource.Error(it.localizedMessage!!))
                                        cancel()
                                    }
                                }
                            }.addOnFailureListener {
                                trySend(Resource.Error("Can't upload your product photo"))
                            }



                        }?.addOnFailureListener {
                            trySend(
                                Resource.Error(
                                    it.localizedMessage ?: "An unexpected error occured"
                                )
                            ).isFailure
                        }
                    } catch (e: Exception) {
                        CrashlyticsReport.Session.Event.Log.builder()
                            .setContent(e.localizedMessage!!)
                    }
                } else {
                    trySend(
                        Resource.Error(
                            "You need to upload a Product Image"
                        )
                    )
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