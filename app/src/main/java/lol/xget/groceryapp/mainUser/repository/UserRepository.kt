package lol.xget.groceryapp.mainUser.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import lol.xget.groceryapp.mainUser.domain.User

interface UserRepository {

    suspend fun updateProfile(user: User, userUid: String) : Task<Void>

    suspend fun getProfile(userUid: String, accountType: String) : Task<DataSnapshot>

    suspend fun getShopsList() : DatabaseReference

    suspend fun getCurrentProduct(shopId : String, currentProduct : String) : Task<DataSnapshot>


}