package lol.xget.groceryapp.domain.use_case.homeUser


import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.domain.model.ProductModel
import lol.xget.groceryapp.domain.model.ShopModel
import lol.xget.groceryapp.domain.repository.UserRepository
import lol.xget.groceryapp.presentation.main.Seller.Home.SellerHomeState
import lol.xget.groceryapp.presentation.main.User.Home.UserHomeScreenState
import org.kpropmap.asMap
import java.io.IOException
import javax.inject.Inject

class GetShopsList @Inject constructor(
    private val repo : UserRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Resource<UserHomeScreenState>> = callbackFlow {
        var fbShopsList = mutableListOf<ShopModel>()
        try {
            try {
                trySend(Resource.Loading())
                //TODO(Filter by city)
                repo.getShopsList().addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        fbShopsList.clear()
                        for (ds in snapshot.children){
                            val shop = ds.getValue(ShopModel::class.java)
                            fbShopsList.add(shop!!)
                        }

                        trySend(Resource.Success(UserHomeScreenState(shopModel = fbShopsList)))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        trySend(Resource.Error(error.message ?: "An unexpected error occured"))
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
            trySend(Resource.Error(e.localizedMessage ?: "An unexpected error occured")).isFailure
            awaitClose { cancel() }
        } catch (e: IOException) {
            awaitClose { cancel() }
            trySend(Resource.Error("Couldn't reach server. Check your internet connection.")).isFailure
        }
    }

}