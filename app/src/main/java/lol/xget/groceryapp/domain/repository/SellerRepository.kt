package lol.xget.groceryapp.domain.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import lol.xget.groceryapp.domain.model.ProductModel
import lol.xget.groceryapp.domain.model.UserModel

interface SellerRepository {

    //product
    suspend fun addProduct(product:Map<String , Any?>, userUid: String) : Task<Void>
    suspend fun getProductsData(currentUser : String) : DatabaseReference
    suspend fun deleteProduct(productId : String, userId: String) : Task<Void>
    suspend fun updateProduct(productId: String, userId: String, newData: Map<String, Any?>) : Task<Void>
    suspend fun getProductData(currentProduct : String, userId: String) : Task<DataSnapshot>

    //profile/shop
    suspend fun getProfile(userUid: String, accountType: String) : Task<DataSnapshot>
    suspend fun updateShopData(currentUser: String, newShopData: UserModel) :Task<Void>
    suspend fun getShopData(currentShop : String) : DatabaseReference



}