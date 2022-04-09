package lol.xget.groceryapp.domain.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult


interface AuthRepository {

    suspend fun signInWithEmailPassword(email : String, password: String) : Task<AuthResult>

    suspend fun register(email : String, password: String) : Task<AuthResult>

    suspend fun recoverPassword(email: String): Task<Void>

}