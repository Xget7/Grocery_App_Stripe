package lol.xget.groceryapp.seller.mainSeller.use_case


import android.content.SharedPreferences
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
import kotlinx.coroutines.joinAll
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.seller.mainSeller.domain.ShopModel
import lol.xget.groceryapp.seller.mainSeller.repository.SellerRepository
import lol.xget.groceryapp.seller.profileSeller.presentation.config.SellerSettingsState
import lol.xget.groceryapp.user.mainUser.presentation.ShopDetails.ShopDetailScreenState
import java.io.IOException
import javax.inject.Inject

class SubscribeToOrdersUseCase @Inject constructor(
    private val repo : SellerRepository
) {


    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke (sp_editor : SharedPreferences.Editor): Flow<Resource<SellerSettingsState>> =
        callbackFlow {
            var shop: ShopModel?
            try {
                try {
                    trySend(Resource.Loading())
                    repo.subscribeToOrders().addOnSuccessListener {

                        sp_editor.putBoolean("FMC_ENABLED",true)
                        sp_editor.apply()
                        trySend(Resource.Success(SellerSettingsState(isSuccess = true)))
                    }.addOnFailureListener {
                        trySend(Resource.Error(it.message!!))
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


class UnsubscribeToOrdersUseCase @Inject constructor(
    private val repo : SellerRepository
) {


    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke (sp_editor : SharedPreferences.Editor): Flow<Resource<SellerSettingsState>> =
        callbackFlow {
            var shop: lol.xget.groceryapp.seller.mainSeller.domain.ShopModel?
            try {
                try {
                    trySend(Resource.Loading())
                    repo.subscribeToOrders().addOnSuccessListener {
                        sp_editor.putBoolean("FMC_ENABLED",false)
                        sp_editor.apply()
                        trySend(Resource.Success(SellerSettingsState(isSuccess = true)))
                    }.addOnFailureListener {
                        trySend(Resource.Error(it.message!!))
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
