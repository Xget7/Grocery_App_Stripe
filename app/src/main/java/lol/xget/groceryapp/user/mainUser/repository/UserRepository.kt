package lol.xget.groceryapp.user.mainUser.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import lol.xget.groceryapp.user.shoppingCar.domain.Order

interface UserRepository {

    suspend fun updateProfile(user: lol.xget.groceryapp.user.mainUser.domain.User, userUid: String) : Task<Void>

    suspend fun getProfile(userUid: String, accountType: String) : Task<DataSnapshot>

    suspend fun getShopsList() : DatabaseReference

    suspend fun getShopData(currentShop : String) : DatabaseReference

    suspend fun placeOrder(shopId: String, orderInfo : Order) : DatabaseReference

    suspend fun getOrders(shopId: String, orderIdFrom: String) : DatabaseReference

    suspend fun getOrderById(shopId: String, orderId: String) : DatabaseReference

    suspend fun getItemsByOrderId(shopId: String, orderId: String) : DatabaseReference

    suspend fun getCurrentProduct(shopId : String, currentProduct : String) : Task<DataSnapshot>


}