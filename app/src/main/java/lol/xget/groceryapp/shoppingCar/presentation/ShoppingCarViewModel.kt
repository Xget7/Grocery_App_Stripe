package lol.xget.groceryapp.shoppingCar.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
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
    var totalItemsPrice = mutableStateOf(0)

    fun getTotalPriceItems(){
        var totalPrice = 0.0
        for (item in _shopCartItems) {
            totalPrice += (item.itemPriceEach.toFloat() * item.itemAmount)
        }
      totalItemsPrice.value = totalPrice.roundToInt()
    }


    val totalItems = flow {
        emit(_shopCartItems.size)
    }

    init {
        getItemsFromUser()

    }


    fun deleteItem(item: CartItems) {
        viewModelScope.launch(IO) {
            if (item.itemAmount > 1) {
                var originalItemPrice = item.itemPriceEach.toFloat()
                var newItemPriceFloat = item.itemPriceTotal.toFloat()

                item.itemAmount -= 1
                newItemPriceFloat -= originalItemPrice

                item.itemPriceTotal = newItemPriceFloat.toString()


                shoppingCartDb.update(item)

            } else {
                shoppingCartDb.deleteItem(item)
            }
        }
        getTotalPriceItems()
    }

    fun addItem(item: CartItems) {
        viewModelScope.launch(IO) {
            var originalItemPrice = item.itemPriceEach.toFloat()
            var newItemPriceFloat = item.itemPriceTotal.toFloat()


            item.itemAmount += 1
            newItemPriceFloat += originalItemPrice


            item.itemPriceTotal = newItemPriceFloat.toString()
            shoppingCartDb.update(item)
        }
        getTotalPriceItems()
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