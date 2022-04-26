package lol.xget.groceryapp.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import lol.xget.groceryapp.seller.mainSeller.repository.SellerRepository
import lol.xget.groceryapp.user.mainUser.domain.User
import javax.inject.Inject


class SellerRepoImpl @Inject constructor(

) : SellerRepository {
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance()

    //clean entry of data , checked before
    override suspend fun addProduct(product: Map<String, Any?>, userUid: String): Task<Void> {
        return db.getReference("sellers")
            .child(userUid).child("products")
            .child(product["productId"].toString())
            .updateChildren(product)
    }


    override suspend fun getProductsData(currentUser: String): DatabaseReference {
        return db.getReference("sellers").child(currentUser).child("products")
    }

    override suspend fun deleteProduct(productId: String, userId: String): Task<Void> {
        return db.getReference("sellers")
            .child(userId)
            .child("products")
            .child(productId)
            .removeValue()
    }

    override suspend fun updateProduct(
        productId: String,
        userId: String,
        newData: Map<String, Any?>
    ): Task<Void> {
        return db.getReference("sellers")
            .child(userId).child("products")
            .child(productId)
            .updateChildren(newData)
    }

    override suspend fun getProductData(
        currentProduct: String,
        userId: String
    ): Task<DataSnapshot> {
        return db.getReference("sellers").child(userId).child("products").child(currentProduct).get()
    }

    override suspend fun updateShopData(currentUser: String, newShopData: User): Task<Void> {
        return db.getReference(newShopData.accountType + "s").child(currentUser)
            .updateChildren(newShopData.toMap())
    }

    override suspend fun updateShopBanners(currentUser: String, newShopData: User): Task<Void> {
        return db.getReference(newShopData.accountType + "s").child(currentUser)
            .updateChildren(newShopData.toMap())
    }

    override suspend fun getShopData(currentShop: String): DatabaseReference {
        return db.getReference("sellers").child(currentShop)
    }


    override suspend fun getProfile(userUid: String, accountType: String): Task<DataSnapshot> {
        return db.getReference(accountType + "s").child(userUid).get()
    }


}