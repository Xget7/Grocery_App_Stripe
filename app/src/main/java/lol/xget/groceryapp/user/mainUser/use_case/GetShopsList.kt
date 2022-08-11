package lol.xget.groceryapp.user.mainUser.use_case

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.seller.mainSeller.domain.ShopModel
import lol.xget.groceryapp.user.mainUser.presentation.UserHomeScreenState
import lol.xget.groceryapp.user.mainUser.repository.UserRepository
import java.io.IOException
import javax.inject.Inject

class GetShopsList @Inject constructor(
    private val repo : UserRepository
) {

    operator fun invoke(): Flow<Resource<UserHomeScreenState>> = callbackFlow {
        var fbShopsList = mutableListOf<ShopModel>()
        try {
            try {
                trySend(Resource.Loading())
                repo.getShopsList().addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        fbShopsList.clear()
                        for (ds in snapshot.children) {
                            val shop = ds.getValue(ShopModel::class.java)
                            fbShopsList.add(shop!!)
                        }
                        launch {
                            trySend(Resource.Success(UserHomeScreenState(shopModel = fbShopsList , loading = false)))
                        }
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