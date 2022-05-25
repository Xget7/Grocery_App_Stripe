package lol.xget.groceryapp.user.mainUser.use_case

import androidx.compose.runtime.mutableStateListOf
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
import lol.xget.groceryapp.user.mainUser.presentation.orders.ordersDetails.UserOrdersDetailState
import lol.xget.groceryapp.user.mainUser.repository.UserRepository
import java.io.IOException
import javax.inject.Inject

class GetItemsByOrderId  @Inject constructor(
    private val repo: UserRepository
) {
    private val currentItems : MutableList<CartItems> = mutableStateListOf(CartItems())



    operator fun invoke(
        currentShopId: String,
        currentOrderId: String
    ): Flow<Resource<UserOrdersDetailState>> =
        callbackFlow {
            try {
                try {
                    trySend(Resource.Loading())
                    repo.getItemsByOrderId(currentShopId, currentOrderId)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (ds in snapshot.children) {
                                    ds.getValue(CartItems::class.java)?.let {
                                        currentItems.add(it)
                                    }
                                }
                                if (currentItems.size > 0) {
                                    trySend(
                                        Resource.Success(
                                            UserOrdersDetailState(
                                                success = true,
                                                orderItems = currentItems
                                            )
                                        )
                                    )
                                }else{
                                    trySend(
                                        Resource.Success(
                                            UserOrdersDetailState(
                                                success = true,
                                                noItems = true
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
