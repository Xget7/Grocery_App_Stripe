package lol.xget.groceryapp.mainSeller.use_case

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.mainSeller.repository.SellerRepository
import lol.xget.groceryapp.mainSeller.presentation.SellerHomeState
import java.io.IOException
import javax.inject.Inject

class DeleteProduct @Inject constructor(
    private val repo : SellerRepository
) {
    private val ref = FirebaseStorage.getInstance()

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(productId : String, userId : String) : Flow<Resource<SellerHomeState>> =
        callbackFlow {
            try {
                try {
                    repo.deleteProduct(productId, userId).addOnSuccessListener {
                        Log.e("DeleteCase", "Deleted?")
                        try {
                            ref.getReference("/products/$productId").delete()
                        } catch (e: Exception) {
                            CrashlyticsReport.Session.Event.Log.builder()
                                .setContent(e.localizedMessage!!)
                        }
                        trySend(Resource.Success(SellerHomeState(successDeleted = true)))
                    }.addOnFailureListener {
                        trySend(Resource.Error(it.localizedMessage!!))
                        cancel()
                    }
                } catch (e: FirebaseException) {
                    trySend(Resource.Error(e.localizedMessage!!))
                    cancel()
                }
                try {
                    awaitClose { channel.close() }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    Log.e("DeleteProduct", ex.message ?: "")
                }
            } catch (e: FirebaseException) {
                trySend(
                    Resource.Error(
                        e.localizedMessage ?: "An unexpected error occured"
                    )
                ).isFailure
                awaitClose { cancel() }
            } catch (e: IOException) {
                trySend(Resource.Error("Couldn't reach server. Check your internet connection.")).isFailure
                awaitClose { cancel() }
            }
        }
}