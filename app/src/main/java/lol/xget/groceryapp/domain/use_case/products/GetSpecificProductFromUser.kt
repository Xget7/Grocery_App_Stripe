package lol.xget.groceryapp.domain.use_case.products

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.homeSeller.domain.ProductModel
import lol.xget.groceryapp.homeUser.repository.UserRepository
import lol.xget.groceryapp.homeUser.presentation.ProductDetail.ProductDetailState
import java.io.IOException
import javax.inject.Inject

class GetSpecificProductFromUser @Inject constructor(
    private val repo: UserRepository
) {

    private val currentUser = FirebaseAuth.getInstance().currentUser!!.uid

    operator fun invoke(shopId : String,currentProduct: String): Flow<Resource<ProductDetailState>> = callbackFlow {
        try {
            try {
                trySend(Resource.Loading())
                repo.getCurrentProduct(shopId,currentProduct).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val specificProduct = task.result.getValue(ProductModel::class.java)
                        Log.e("specificProduct", specificProduct.toString())
                        trySend(
                            Resource.Success(
                                ProductDetailState(
                                    success = true,
                                    currentProduct = specificProduct
                                )
                            )
                        )
                    } else {
                        trySend(Resource.Error("Error getting product data"))
                        cancel()
                    }
                }.addOnFailureListener {
                    trySend(Resource.Error(it.localizedMessage!!))
                    cancel()
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
            trySend(Resource.Error(e.localizedMessage ?: "An unexpected error occured")).isFailure
            awaitClose { cancel() }
        } catch (e: IOException) {
            awaitClose { cancel() }
            trySend(Resource.Error("Couldn't reach server. Check your internet connection.")).isFailure
        }
    }
}