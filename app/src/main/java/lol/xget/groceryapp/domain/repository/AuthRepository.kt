package lol.xget.groceryapp.domain.repository

import android.app.Activity
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import lol.xget.groceryapp.domain.model.ShopModel
import lol.xget.groceryapp.domain.model.UserModel


interface AuthRepository {

    suspend fun signInWithEmailPassword(email : String, password: String) : Task<AuthResult>

    suspend fun register(email : String, password: String) : Task<AuthResult>

    suspend fun recoverPassword(email: String): Task<Void>

}