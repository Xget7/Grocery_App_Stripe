package lol.xget.groceryapp.seller.mainSeller.use_case

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.FirebaseException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.seller.mainSeller.presentation.SellerHomeState
import lol.xget.groceryapp.seller.mainSeller.presentation.SellerOrdersScreen.SellerOrderState
import lol.xget.groceryapp.seller.mainSeller.repository.SellerRepository
import lol.xget.groceryapp.user.shoppingCar.domain.Order
import java.io.IOException
import javax.inject.Inject

class GetAllOrders @Inject constructor(
    private val repo: SellerRepository
) {

    @ExperimentalCoroutinesApi
    operator fun invoke(shopId: String): Flow<Resource<SellerOrderState>> =
        callbackFlow {
            val listOfOrders = mutableStateListOf<Order>()
            try {
                trySend(Resource.Loading())
                repo.getAllOrders(shopId).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        listOfOrders.clear()
                        for (ds in snapshot.children) {
                            val order = ds.getValue(Order::class.java)
                            if (order?.orderTo == shopId) {
                                listOfOrders.add(order)
                            }
                        }
                        trySend(
                            Resource.Success(
                                SellerOrderState(
                                    isSuccess = true, isLoading = false, orders = listOfOrders
                                )
                            )
                        )
                    }

                    override fun onCancelled(error: DatabaseError) {
                        try {
                            trySend(Resource.Error(error.message)).isSuccess
                            cancel()
                        } catch (e: Exception) {
                            trySend(Resource.Error(error.message)).isFailure
                        }
                    }


                })
                try {
                    awaitClose { channel.close() }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } catch (e: FirebaseException) {
                trySend(Resource.Error(e.localizedMessage!!)).isFailure
                cancel()
            } catch (e: IOException) {
                trySend(Resource.Error("Couldn't reach server. Check your internet connection.")).isFailure
                awaitClose { cancel() }
            }
        }

}