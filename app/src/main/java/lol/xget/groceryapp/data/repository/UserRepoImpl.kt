package lol.xget.groceryapp.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import lol.xget.groceryapp.mainUser.domain.User
import lol.xget.groceryapp.mainUser.repository.UserRepository
import org.kpropmap.asMap
import javax.inject.Inject

class UserRepoImpl @Inject constructor(

) : UserRepository {

    //to realtimeDatabase
    private val db2 = FirebaseDatabase.getInstance()


    override suspend fun updateProfile(user: User, userUid: String): Task<Void> {
        return db2.getReference(user.accountType + "s").child(userUid).updateChildren(user.asMap())
    }

    override suspend fun getProfile(userUid: String, accountType: String): Task<DataSnapshot> {
        return db2.getReference(accountType + "s").child(userUid).get()
    }

    override suspend fun getShopsList(): DatabaseReference {
        //get shops from sellers
        return db2.getReference("sellers")
    }

    override suspend fun getCurrentProduct(
        shopId: String,
        currentProduct: String
    ): Task<DataSnapshot> {
        return db2.getReference("sellers").child(shopId).child("products").child(currentProduct).get()//?
    }


}