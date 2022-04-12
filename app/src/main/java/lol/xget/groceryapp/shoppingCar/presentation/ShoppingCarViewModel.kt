package lol.xget.groceryapp.shoppingCar.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lol.xget.groceryapp.data.localdb.CartItems
import lol.xget.groceryapp.data.repository.CartItemsRepoImpl
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ShoppingCarViewModel @Inject constructor(
    private val shoppingCartDb: CartItemsRepoImpl
) : ViewModel() {

    val shopCartItems = mutableListOf<CartItems>()


    //flow?
    val totalItemsPrice = fun() : Int{
        var totalPrice = 0.0
        for (item in shopCartItems){
            totalPrice += item.itemPriceEach.toFloat()
        }
        return totalPrice.roundToInt()
    }
    val totalItems = shopCartItems.size

    init {
        TODO("show items totalprice , add remove with realTime view and db")
        getItemsFromUser()
    }


     fun deleteItem(item : CartItems) {
        viewModelScope.launch {
            if (item.itemAmount > 1){
                item.itemAmount = item.itemAmount - 1
            }
            shoppingCartDb.deleteItem(item)
        }
    }

    private fun getItemsFromUser() {
        viewModelScope.launch {
            shoppingCartDb.readAllItems.collect {
                for (item in it) {
                    shopCartItems.add(item)
                }
            }
        }
    }
}