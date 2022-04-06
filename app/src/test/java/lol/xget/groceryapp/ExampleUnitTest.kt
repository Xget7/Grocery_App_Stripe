package lol.xget.groceryapp


import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.domain.model.ProductModel
import lol.xget.groceryapp.domain.model.ShopModel
import lol.xget.groceryapp.domain.repository.SellerRepository
import lol.xget.groceryapp.domain.repository.UserRepository
import lol.xget.groceryapp.domain.use_case.homeSeller.GetShopProducts
import lol.xget.groceryapp.presentation.main.Seller.Home.SellerHomeState
import org.junit.Test

import org.junit.Assert.*
import java.io.IOException
import javax.inject.Inject

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }





}

