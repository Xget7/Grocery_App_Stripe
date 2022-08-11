package lol.xget.groceryapp.user.profileUser.use_case


import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.seller.mainSeller.domain.ShopModel
import lol.xget.groceryapp.user.mainUser.presentation.orders.UserOrdersState
import lol.xget.groceryapp.user.mainUser.repository.UserRepository
import lol.xget.groceryapp.user.shoppingCar.domain.Order
import java.io.IOException
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val repo: UserRepository
) {
    private val allOrdersList = mutableStateListOf<Order>()


    operator fun invoke(shopsList: List<ShopModel>, userId : String): Flow<Resource<UserOrdersState>> =
        callbackFlow {
            try {
                try {
                    trySend(Resource.Loading())
                    for (shopId in shopsList) {
                        allOrdersList.clear()
                        repo.getOrders(shopId.uid!!, userId)
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (ds in snapshot.children) {
                                        ds.getValue(Order::class.java)?.let {
                                            allOrdersList.add(it)
                                        }
                                    }
                                    if (allOrdersList.isNotEmpty()){
                                        val filteredOrders = allOrdersList.filter { it.orderBy == userId }
                                        trySend(Resource.Success(UserOrdersState(orders = filteredOrders, successLoadOders = true, loading = false)))

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