package lol.xget.groceryapp.user.shoppingCar.use_case

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.data.localdb.CartItems
import lol.xget.groceryapp.user.mainUser.repository.UserRepository
import lol.xget.groceryapp.user.shoppingCar.domain.Order
import lol.xget.groceryapp.user.shoppingCar.presentation.ShopingCarState
import java.io.IOException
import javax.inject.Inject

class PlaceOrderUseCase @Inject constructor(
    private val repo: UserRepository
) {
    private val currentUser = FirebaseAuth.getInstance().currentUser?.uid

    operator fun invoke(shopId: String, order: Order, carItemsToBuy : List<CartItems>): Flow<Resource<ShopingCarState>> =
        callbackFlow {
            val ref = FirebaseDatabase.getInstance().getReference("sellers").child(shopId).child("orders")
            try {
                try {
                    trySend(Resource.Loading())
                    repo.placeOrder(shopId, order).child(order.orderId!!).setValue(order)
                        .addOnSuccessListener {
                            try {
                                for (item in carItemsToBuy) {
                                    ref.child(order.orderId).child("items").child(item.itemId!!).setValue(item)
                                }
                                trySend(Resource.Success(ShopingCarState(successPlacedOrder = true)))

                            }catch (e : FirebaseException){
                                cancel()
                                trySend(Resource.Error(e.localizedMessage!!))
                            }
                        }.addOnFailureListener {
                            cancel()
                            trySend(Resource.Error(it.localizedMessage!!))
                        }

                } catch (e: FirebaseException) {
                    awaitClose { channel.close() }
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