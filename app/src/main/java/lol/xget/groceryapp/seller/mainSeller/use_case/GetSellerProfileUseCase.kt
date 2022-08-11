package lol.xget.groceryapp.seller.mainSeller.use_case

import com.google.firebase.FirebaseException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.seller.mainSeller.presentation.SellerHomeState
import lol.xget.groceryapp.seller.mainSeller.repository.SellerRepository
import lol.xget.groceryapp.user.mainUser.domain.User
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
                repo.getProfile(userId, accountType).addOnSuccessListener { db ->
                    if (db.exists()) {
                        val user = db.getValue(User::class.java)
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