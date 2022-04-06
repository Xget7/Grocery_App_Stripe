package lol.xget.groceryapp.domain.use_case.homeSeller

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.domain.model.ProductModel
import lol.xget.groceryapp.domain.model.UserModel
import lol.xget.groceryapp.domain.repository.SellerRepository
import lol.xget.groceryapp.presentation.main.Seller.Home.SellerHomeState
import org.kpropmap.asMap
import java.io.IOException
import java.text.DateFormat
import java.text.DateFormat.getDateTimeInstance
import java.util.*
import javax.inject.Inject

class GetShopProducts @Inject constructor(
    private val repo : SellerRepository
) {

        //TODO("get procuts ")
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(userId : String): Flow<Resource<SellerHomeState>> = callbackFlow {
        val fbProductList = mutableListOf<ProductModel>()
        try {
            try {
                trySend(Resource.Loading())
                repo.getProductsData(userId).addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                       for (ds in snapshot.children){
                           val productModel = ds.getValue(ProductModel::class.java)
                           fbProductList.add(productModel!!)
                       }
                        trySend(Resource.Success(SellerHomeState(productModel = fbProductList)))
                    }
                    override fun onCancelled(error: DatabaseError) {
                        trySend(Resource.Error(error.message ?: "An unexpected error occured"))
                    }
                })



            } catch (e: FirebaseException) {
                cancel()
                trySend(Resource.Error(e.localizedMessage!!))
            }

            try {
                awaitClose { channel.close() }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("GetProductsUseCase", ex.message ?: "")
            }
        } catch (e: FirebaseException) {
            trySend(Resource.Error(e.localizedMessage ?: "An unexpected error occured")).isFailure
            awaitClose { cancel() }
        } catch (e: IOException) {
            awaitClose { cancel() }
            trySend(Resource.Error("Couldn't reach server. Check your internet connection.")).isFailure
        }
    }

    fun getTimeDate(timeStamp: Long): String? {
        return try {
            val dateFormat: DateFormat = getDateTimeInstance()
            val netDate = Date(timeStamp)
            dateFormat.format(netDate)
        } catch (e: Exception) {
            e.message
        }
    }
}