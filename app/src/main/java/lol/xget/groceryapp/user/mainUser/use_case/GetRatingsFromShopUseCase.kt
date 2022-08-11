package lol.xget.groceryapp.user.mainUser.use_case

import android.util.Log
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
import lol.xget.groceryapp.seller.mainSeller.domain.ShopModel
import lol.xget.groceryapp.seller.mainSeller.repository.SellerRepository
import lol.xget.groceryapp.user.mainUser.domain.Review
import lol.xget.groceryapp.user.mainUser.presentation.ShopDetails.ShopDetailScreenState
import lol.xget.groceryapp.user.mainUser.repository.UserRepository
import java.io.IOException
import javax.inject.Inject

class GetRatingsFromShopUseCase @Inject constructor(
    private val repo : UserRepository
) {


    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke (currentShop: String): Flow<Resource<ShopDetailScreenState>> =
        callbackFlow {
            val reviewList = mutableListOf<Review>()
            try {
                try {
                    trySend(Resource.Loading())
                    repo.getShopRatings(currentShop)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                reviewList.clear()
                                for (ds in snapshot.children){
                                    val review = ds.getValue(Review::class.java)
                                    review?.let { reviewList.add(it) }
                                }
                                trySend(Resource.Success(ShopDetailScreenState(reviewsList = reviewList )))
                                Log.e("REviews", reviewList.toString())
                            }

                            override fun onCancelled(error: DatabaseError) {
                                trySend(
                                    Resource.Error(
                                        error.message ?: "An unexpected error occured"
                                    )
                                )
                                cancel()
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
                    Log.e("GetProductsUseCase", ex.message ?: "")
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