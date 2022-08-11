package lol.xget.groceryapp.user.shoppingCar.use_case

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.user.mainUser.domain.User
import lol.xget.groceryapp.user.mainUser.presentation.userReviews.UserReviewsState
import lol.xget.groceryapp.user.mainUser.repository.UserRepository
import lol.xget.groceryapp.user.profileUser.presentation.ProfileState
import lol.xget.groceryapp.user.shoppingCar.presentation.ShopingCarState
import java.io.IOException
import javax.inject.Inject

class GetUserData @Inject constructor(
    private val repo : UserRepository
) {

    @ExperimentalCoroutinesApi
    operator  fun  invoke(userId : String,accountType: String): Flow<Resource<ShopingCarState>> = callbackFlow {
        trySend(Resource.Loading())
        try {
            repo.getProfile(userId, accountType).addOnSuccessListener { db ->
                if (db.exists()) {
                    val user = db.getValue(User::class.java)
                    trySend(Resource.Success(ShopingCarState(user = user)))
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

class GetUserDataFromReviews @Inject constructor(
    private val repo : UserRepository
) {

    @ExperimentalCoroutinesApi
    suspend operator fun invoke(userId : String, accountType: String): Flow<Resource<UserReviewsState>> = callbackFlow {
        try {
            repo.getProfile(userId, accountType).addOnSuccessListener { db ->
                if (db.exists()) {
                    val user = db.getValue(User::class.java)
                    trySend(Resource.Success(UserReviewsState(user = user)))
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