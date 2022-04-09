package lol.xget.groceryapp.homeSeller.use_case

import android.util.Log
import com.google.firebase.FirebaseException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.homeSeller.repository.SellerRepository
import lol.xget.groceryapp.homeUser.domain.User
import lol.xget.groceryapp.homeSeller.presentation.SellerHomeState
import java.io.IOException
import javax.inject.Inject

class GetSellerProfileUseCase @Inject constructor(
    private val repo : SellerRepository
) {

    @ExperimentalCoroutinesApi
    operator fun invoke(userId: String, accountType: String): Flow<Resource<SellerHomeState>> =
        callbackFlow {
            try {
                trySend(Resource.Loading())
                repo.getProfile(userId, accountType).addOnSuccessListener {
                    if (it.exists()) {
                        val user = it.getValue(User::class.java)
                        Log.e("userwithdatabas", user.toString())
                        trySend(Resource.Success(SellerHomeState(user = user)))
                    }
                }.addOnFailureListener {
                    try {
                        trySend(Resource.Error(it.localizedMessage!!)).isSuccess
                        cancel()
                    } catch (e: Exception) {
                        trySend(Resource.Error(it.localizedMessage!!)).isFailure
                    }

                }
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