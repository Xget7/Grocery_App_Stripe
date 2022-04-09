package lol.xget.groceryapp.shoppingCar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import lol.xget.groceryapp.data.repository.CartItemsRepoImpl
import javax.inject.Inject

@HiltViewModel
class ShoppingCarViewModel @Inject constructor(
    private  val shoppingCartDb : CartItemsRepoImpl
): ViewModel(){
    private fun getItemsFromUser(){
        viewModelScope.launch {
            val itemsFromUser = shoppingCartDb.readAllItems
        }
    }
}