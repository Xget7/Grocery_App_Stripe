package lol.xget.groceryapp.domain.use_case.auth.register_use_case

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.domain.model.UserModel
import lol.xget.groceryapp.domain.repository.AuthRepository
import lol.xget.groceryapp.presentation.auth.register_user.RegisterUserState
import org.kpropmap.asMap
import java.io.IOException
import javax.inject.Inject


class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {


    @ExperimentalCoroutinesApi
    operator fun invoke (email : String,password : String, userModel: UserModel) : Flow<Resource<RegisterUserState>> = callbackFlow{
        val db = FirebaseDatabase.getInstance()
        userModel.gmail = email
        try {
            trySend(Resource.Loading())
            repository.register(email,password).addOnSuccessListener { result ->
                val userUid = result.user!!.uid
                try {
                    userModel.uid = userUid
                    try {
                        Log.e("userModelAccounttypoe", userModel.accountType!!)
                        if (userModel.accountType!! == "seller"){
                            db.getReference("sellers").child(userUid).updateChildren(userModel.asMap()).addOnSuccessListener {
                                try {
                                    trySend(Resource.Success(RegisterUserState(successRegister = true )))
                                }catch (e:Exception){
                                    trySend(Resource.Error(e.localizedMessage!!)).isFailure
                                }
                            }
                        }else if (userModel.accountType == "user"){
                            db.getReference("users").child(userUid).updateChildren(userModel.asMap()).addOnSuccessListener {
                                try {
                                    trySend(Resource.Success(RegisterUserState(successRegister = true )))
                                }catch (e:Exception){
                                    trySend(Resource.Error(e.localizedMessage!!)).isFailure
                                }
                            }
                        }
                    }catch (e : FirebaseException){
                        trySend(Resource.Error(e.localizedMessage!!)).isSuccess
                        cancel()
                    }
                }catch (e: Exception){
                    cancel()
                }
            }.addOnFailureListener {
                try {
                    trySend(Resource.Error(it.localizedMessage!!)).isSuccess
                } catch (e:Exception){
                    Log.e("LoginUseCAse", it.localizedMessage!!)
                }
            }

            try {
                awaitClose { channel.close() }
            }catch (ex: Exception) {
                ex.printStackTrace()
            }
        } catch(e: FirebaseException) {
            trySend(Resource.Error(e.localizedMessage ?: "An unexpected error occured")).isFailure
            awaitClose { cancel() }
        } catch(e: IOException) {
            awaitClose { cancel() }
            trySend(Resource.Error("Couldn't reach server. Check your internet connection.")).isFailure
        }
    }
}