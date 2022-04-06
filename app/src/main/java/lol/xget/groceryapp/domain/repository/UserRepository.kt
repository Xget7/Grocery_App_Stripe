package lol.xget.groceryapp.domain.repository

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import lol.xget.groceryapp.domain.model.ShopModel
import lol.xget.groceryapp.domain.model.UserModel

interface UserRepository {

    suspend fun updateProfile(user: UserModel, userUid: String) : Task<Void>

    suspend fun getProfile(userUid: String, accountType: String) : Task<DataSnapshot>

    suspend fun getShopsList() : DatabaseReference

    suspend fun getCurrentProduct(shopId : String, currentProduct : String) : Task<DataSnapshot>


}