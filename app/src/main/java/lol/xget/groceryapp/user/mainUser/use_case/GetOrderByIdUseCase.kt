package lol.xget.groceryapp.user.profileUser.use_case


import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.FirebaseException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.seller.mainSeller.domain.ShopModel
import lol.xget.groceryapp.user.mainUser.presentation.orders.UserOrdersState
import lol.xget.groceryapp.user.mainUser.presentation.orders.ordersDetails.UserOrdersDetailState
import lol.xget.groceryapp.user.mainUser.repository.UserRepository
import lol.xget.groceryapp.user.shoppingCar.domain.Order
import java.io.IOException
import javax.inject.Inject

class GetOrderByIdUseCase @Inject constructor(
    private val repo: UserRepository
) {
    private val currentOrder = mutableStateOf(Order())


    operator fun invoke(
        currentShopId: String,
        currentOrderId: String
    ): Flow<Resource<UserOrdersDetailState>> =
        callbackFlow {
            try {
                try {
                    trySend(Resource.Loading())

                        repo.getOrderbyId(currentShopId, currentOrderId,)
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (ds in snapshot.children) {
                                        //ya ando ahora hay problema aca
                                        ds.getValue(Order::class.java)?.let {
                                            currentOrder.value = it
                                        }
                                    }
                                    if (currentOrder.value.orderId != null) {
                                        trySend(
                                            Resource.Success(
                                                UserOrdersDetailState(
                                                    successLoadOders = true
                                                )
                                            )
                                        )

                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    trySend(
                                        Resource.Error(
                                            error.message ?: "An unexpected error occured"
                                        )
                                    )
                                }
                            })
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