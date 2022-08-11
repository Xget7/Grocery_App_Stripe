package lol.xget.groceryapp.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import lol.xget.groceryapp.user.mainUser.domain.Review
import lol.xget.groceryapp.user.mainUser.repository.UserRepository
import lol.xget.groceryapp.user.shoppingCar.domain.Order
import org.kpropmap.asMap
import javax.inject.Inject

class UserRepoImpl @Inject constructor(

) : UserRepository {

    //to realtimeDatabase
    private val db2 = FirebaseDatabase.getInstance()
    val coroutineScope: CoroutineScope
        get() = CoroutineScope(Dispatchers.IO)

    override suspend fun updateProfile(
        user: lol.xget.groceryapp.user.mainUser.domain.User,
        userUid: String
    ): Task<Void> {
        return db2.getReference(user.accountType + "s").child(userUid).updateChildren(user.asMap())
    }

    override suspend fun getProfile(userUid: String, accountType: String): Task<DataSnapshot> {
        return db2.getReference(accountType + "s").child(userUid).get()
    }

    override suspend fun getShopsList(): DatabaseReference {
        //get shops from sellers
        return db2.getReference("sellers")
    }

    override suspend fun getShopData(currentShop: String): DatabaseReference {
        return db2.getReference("sellers").child(currentShop)
    }

    override suspend fun placeOrder(shopId: String, orderInfo: Order): DatabaseReference {
        return db2.getReference("sellers").child(shopId).child("orders")
    }

    override suspend fun getOrders(shopId: String, orderIdFrom: String): DatabaseReference {
        return db2.getReference("sellers").child(shopId).child("orders")
    }

    override suspend fun getOrderById(shopId: String, orderId: String): Task<DataSnapshot> {
        return db2.getReference("sellers").child(shopId).child("orders").child(orderId).get()

    }

    override suspend fun getItemsByOrderId(shopId: String, orderId: String): DatabaseReference {
        return db2.getReference("sellers").child(shopId).child("orders").child(orderId)
            .child("items")
    }

    override suspend fun getCurrentProduct(
        shopId: String,
        currentProduct: String
    ): Task<DataSnapshot> {
        return db2.getReference("sellers").child(shopId).child("products").child(currentProduct)
            .get()//?
    }

    override suspend fun placeShopRating(shopId: String, review: Review): Task<Void> {
        return db2.getReference("sellers").child(shopId).child("ratings").child(review.uid!!).setValue(review)
    }

    override suspend fun getShopRatings(shopId: String): DatabaseReference {
        return db2.getReference("sellers").child(shopId).child("ratings")
    }


}