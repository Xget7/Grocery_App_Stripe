package lol.xget.groceryapp.shoppingCar.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import lol.xget.groceryapp.data.localdb.CartItems
import lol.xget.groceryapp.data.repository.CartItemsRepoImpl
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ShoppingCarViewModel @Inject constructor(
    private val shoppingCartDb: CartItemsRepoImpl
) : ViewModel() {

    val _shopCartItems = mutableStateListOf<CartItems>()
     fun getTotalPriceItems() = flow{
        var totalPrice = 0.0
        for (item in _shopCartItems){
            totalPrice += (item.itemPriceEach.toFloat() * item.itemAmount)
        }
        emit(totalPrice.roundToInt())
    }
    val totalItems = flow {
        emit(_shopCartItems.size)
    }

    init {
        getItemsFromUser()
    }


     fun deleteItem(item : CartItems) {
        viewModelScope.launch(IO) {
            if (item.itemAmount > 1){
                var itemPriceInt = item.itemPriceEach.toFloat()
                item.itemAmount -= 1
                itemPriceInt -= itemPriceInt
                item.itemPriceEach = itemPriceInt.toString()
                shoppingCartDb.update(item)
            }else{
                shoppingCartDb.deleteItem(item)
            }

        }
    }

    fun addItem(item : CartItems) {
        viewModelScope.launch(IO) {
            var itemPriceInt = item.itemPriceEach.toFloat()

            item.itemAmount += 1
            itemPriceInt += itemPriceInt

            item.itemPriceEach = itemPriceInt.toString()
            shoppingCartDb.update(item)
        }
    }

    private fun getItemsFromUser() {
        viewModelScope.launch(IO) {
            shoppingCartDb.readAllItems.collect {
                _shopCartItems.clear()
                for (item in it) {
                    _shopCartItems.add(item)
                }
                getTotalPriceItems()
            }
        }
    }
}