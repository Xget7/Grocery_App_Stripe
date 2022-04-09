package lol.xget.groceryapp.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import lol.xget.groceryapp.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(

) : AuthRepository {
    private val mAuth = com.google.firebase.auth.FirebaseAuth.getInstance()

    override suspend fun signInWithEmailPassword(
        email: String,
        password: String
    ): Task<AuthResult> {
        return mAuth.signInWithEmailAndPassword(email, password)
    }

    override suspend fun register(
        email: String,
        password: String,
    ): Task<AuthResult> {
        return mAuth.createUserWithEmailAndPassword(email, password)
    }

    override suspend fun recoverPassword(email: String): Task<Void> {
        return mAuth.sendPasswordResetEmail(email)
    }


}