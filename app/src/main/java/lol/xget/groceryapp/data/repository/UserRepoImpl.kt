package lol.xget.groceryapp.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import lol.xget.groceryapp.user.mainUser.repository.UserRepository
import lol.xget.groceryapp.user.shoppingCar.domain.Order
import org.kpropmap.asMap
import javax.inject.Inject

class UserRepoImpl @Inject constructor(

) : UserRepository {

    //to realtimeDatabase
    private val db2 = FirebaseDatabase.getInstance()


    override suspend fun updateProfile(user: lol.xget.groceryapp.user.mainUser.domain.User, userUid: String): Task<Void> {
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

    override suspend fun getOrderbyId(shopId: String, orderId: String): DatabaseReference {
        return db2.getReference("sellers").child(shopId).child("orders").child(orderId)

    }

    override suspend fun getCurrentProduct(
        shopId: String,
        currentProduct: String
    ): Task<DataSnapshot> {
        return db2.getReference("sellers").child(shopId).child("products").child(currentProduct).get()//?
    }


}