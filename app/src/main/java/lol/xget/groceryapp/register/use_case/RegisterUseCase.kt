package lol.xget.groceryapp.register.use_case

import com.google.firebase.FirebaseException
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.domain.repository.AuthRepository
import lol.xget.groceryapp.mainUser.domain.User
import lol.xget.groceryapp.register.presentation.register_user.RegisterUserState
import java.io.IOException
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {


    @ExperimentalCoroutinesApi
    operator fun invoke (email : String, password : String, user: User) : Flow<Resource<RegisterUserState>> =
        callbackFlow {
            val db = FirebaseDatabase.getInstance()
            user.email = email
            try {
                trySend(Resource.Loading())
                repository.register(email, password).addOnSuccessListener { result ->
                    val userUid = result.user!!.uid
                    try {
                        user.uid = userUid
                        try {
                            if (user.accountType!! == "seller") {
                                db.getReference("sellers").child(userUid)
                                    .updateChildren(user.toMap()).addOnSuccessListener {
                                    try {
                                        trySend(Resource.Success(RegisterUserState(successRegister = true)))
                                    } catch (e: Exception) {
                                        trySend(Resource.Error(e.localizedMessage!!)).isFailure
                                    }
                                }
                            } else if (user.accountType == "user") {
                                db.getReference("users").child(userUid)
                                    .updateChildren(user.toMap()).addOnSuccessListener {
                                    try {
                                        trySend(Resource.Success(RegisterUserState(successRegister = true)))
                                    } catch (e: Exception) {
                                        trySend(Resource.Error(e.localizedMessage!!)).isFailure
                                    }
                                }
                            }
                        } catch (e: FirebaseException) {
                            trySend(Resource.Error(e.localizedMessage!!)).isSuccess
                            cancel()
                        }
                    } catch (e: Exception) {
                        cancel()
                    }
                }.addOnFailureListener {
                    try {
                        trySend(Resource.Error(it.localizedMessage!!)).isSuccess
                    } catch (e: Exception) {
                    }
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