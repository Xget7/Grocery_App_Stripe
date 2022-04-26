package lol.xget.groceryapp.seller.mainSeller.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import lol.xget.groceryapp.user.mainUser.domain.User

interface SellerRepository {

    //product
    suspend fun addProduct(product:Map<String , Any?>, userUid: String) : Task<Void>
    suspend fun getProductsData(currentUser : String) : DatabaseReference
    suspend fun deleteProduct(productId : String, userId: String) : Task<Void>
    suspend fun updateProduct(productId: String, userId: String, newData: Map<String, Any?>) : Task<Void>
    suspend fun getProductData(currentProduct : String, userId: String) : Task<DataSnapshot>

    //profile/shop
    suspend fun getProfile(userUid: String, accountType: String) : Task<DataSnapshot>
    suspend fun updateShopData(currentUser: String, newShopData: User) : Task<Void>
    suspend fun updateShopBanners(currentUser: String, newShopData: User) : Task<Void>
    suspend fun getShopData(currentShop : String) : DatabaseReference



}