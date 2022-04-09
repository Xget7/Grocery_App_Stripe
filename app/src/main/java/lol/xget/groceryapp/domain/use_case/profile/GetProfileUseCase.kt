package lol.xget.groceryapp.domain.use_case.profile

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.homeUser.domain.User
import lol.xget.groceryapp.homeUser.repository.UserRepository
import lol.xget.groceryapp.profileUser.presentation.ProfileState
import java.io.IOException
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repo : UserRepository
) {



    @ExperimentalCoroutinesApi
    operator fun invoke( accountType: String): Flow<Resource<ProfileState>> = callbackFlow {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        trySend(Resource.Loading())
        try {
                repo.getProfile(currentUserUid!!, accountType).addOnSuccessListener {
                    if (it.exists()){
                        val user = it.getValue(User::class.java)
                        Log.e("userwithdatabas", user.toString())
                        trySend(Resource.Success(ProfileState(user = user)))
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
                awaitClose {  channel.close()}
            }catch (e : Exception){
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
