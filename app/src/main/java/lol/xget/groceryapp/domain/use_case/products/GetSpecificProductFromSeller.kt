package lol.xget.groceryapp.domain.use_case.products

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.mainSeller.domain.ProductModel
import lol.xget.groceryapp.mainSeller.repository.SellerRepository
import lol.xget.groceryapp.mainSeller.presentation.EditProducts.EditProductState
import java.io.IOException
import javax.inject.Inject

class GetSpecificProductFromSeller @Inject constructor(
    private val repo : SellerRepository
) {
    private val currentUser = FirebaseAuth.getInstance().currentUser!!.uid

    operator fun invoke (currentProduct: String): Flow<Resource<EditProductState>> = callbackFlow {
        try {
            try {
                trySend(Resource.Loading())
                repo.getProductData(currentProduct,currentUser).addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        val specificProduct = task.result.getValue(ProductModel::class.java)
                        trySend(Resource.Success(EditProductState(loadedSuccess = true, specificProduct = specificProduct)))
                    }else{
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