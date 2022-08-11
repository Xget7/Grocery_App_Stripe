package lol.xget.groceryapp.seller.mainSeller.use_case

import androidx.compose.animation.core.snap
import com.google.firebase.FirebaseException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.data.localdb.CartItems
import lol.xget.groceryapp.seller.mainSeller.presentation.SellerOrdersScreen.SellerOrdersDetail.SellerOrdersDetailState
import lol.xget.groceryapp.seller.mainSeller.presentation.SellerOrdersScreen.SellerOrdersDetail.SellerOrdersDetailState.Companion.orderStatusChanged
import lol.xget.groceryapp.seller.mainSeller.repository.SellerRepository
import lol.xget.groceryapp.user.mainUser.presentation.orders.ordersDetails.UserOrdersDetailState
import lol.xget.groceryapp.user.mainUser.repository.UserRepository
import java.io.IOException
import javax.inject.Inject

class ChangeOrderStatus @Inject constructor(
    private val repo: SellerRepository
) {


    operator fun invoke(
        currentShopId: String,
        currentOrderId: String,
        newStatus: String
    ): Flow<Resource<SellerOrdersDetailState>> =
        callbackFlow {
            try {
                try {
                    trySend(Resource.Loading())
                    repo.changeOrderStatus(currentShopId, currentOrderId).setValue(newStatus).addOnSuccessListener {
                        trySend(Resource.Success(orderStatusChanged(true )))
                    }.addOnFailureListener { exception ->
                        trySend(Resource.Error(exception.localizedMessage ?: "Error Changing Order Status"))
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
                    trySend(Resource.Error(ex.localizedMessage!!))
                }
            } catch (e: FirebaseException) {
                trySend(
                    Resource.Error(
                        e.localizedMessage ?: "An unexpected error occurred"
                    )
                ).isFailure
                awaitClose { cancel() }
            } catch (e: IOException) {
                awaitClose { cancel() }
                trySend(Resource.Error("Couldn't reach server. Check your internet connection.")).isFailure
            }

        }
}