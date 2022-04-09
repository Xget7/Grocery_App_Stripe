package lol.xget.groceryapp.recoverPassword.use_case

import android.util.Log
import com.google.firebase.FirebaseException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.domain.repository.AuthRepository
import lol.xget.groceryapp.recoverPassword.presentation.RecoverPasswordState
import java.io.IOException
import javax.inject.Inject

class RecoverPasswordUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    @ExperimentalCoroutinesApi
     operator fun invoke(email: String): Flow<Resource<RecoverPasswordState>> = callbackFlow {
        try {
            trySend(Resource.Loading())

            repository.recoverPassword(email).addOnSuccessListener {
                try {
                    trySend(Resource.Success(RecoverPasswordState(successRecovered = true)))
                } catch (e: Exception) {
                    cancel()
                }
            }.addOnFailureListener {
                try {
                    trySend(Resource.Error(it.localizedMessage!!)).isSuccess
                } catch (e: Exception) {
                    Log.e("LoginUseCAse", it.localizedMessage!!)
                }
            }

            try {
                awaitClose { channel.close() }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("LoginUseCAse", ex.message ?: "")
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