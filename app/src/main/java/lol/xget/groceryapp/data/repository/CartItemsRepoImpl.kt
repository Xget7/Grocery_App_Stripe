package lol.xget.groceryapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import lol.xget.groceryapp.data.localdb.CartItems
import lol.xget.groceryapp.data.localdb.CartItemsDao
import javax.inject.Inject

class CartItemsRepoImpl @Inject constructor(
    private val cartItemsDao: CartItemsDao
    ) {

    val readAllItems : Flow<List<CartItems>>
        get() = cartItemsDao.getAllCartItems()


    suspend fun deleteAllItems(listOfItems : List<CartItems>){
        cartItemsDao.deleteAllCartItems(listOfItems)
    }
    suspend fun addItem(item: CartItems){
        cartItemsDao.add(item)
    }

    suspend fun deleteItem(item : CartItems){
        cartItemsDao.delete(item)
    }

    suspend fun update(item: CartItems){
        cartItemsDao.update(item)
    }



}