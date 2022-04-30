package lol.xget.groceryapp.user.shoppingCar.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.data.localdb.CartItems
import lol.xget.groceryapp.data.repository.CartItemsRepoImpl
import lol.xget.groceryapp.seller.mainSeller.domain.ShopModel
import lol.xget.groceryapp.user.mainUser.domain.User
import lol.xget.groceryapp.user.profileUser.use_case.UserUseCases
import javax.inject.Inject
import kotlin.math.roundToInt
import lol.xget.groceryapp.user.shoppingCar.domain.Order as Order1

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ShoppingCarViewModel @Inject constructor(
    private val shoppingCartDb: CartItemsRepoImpl,
    val repo: UserUseCases,
) : ViewModel() {

    val _shopCartItems = mutableStateListOf<CartItems>()

    var totalItemsPrice = mutableStateOf(0)

    private val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid!!
    val state: MutableState<ShopingCarState> = mutableStateOf(ShopingCarState())

    val currentShop = mutableStateOf(ShopModel())

    val timesTamp = System.currentTimeMillis()

    val currentUser = mutableStateOf(User())

    private fun getTotalPriceItems() {
        var totalPrice = 0.0
        for (item in _shopCartItems) {
            totalPrice += (item.itemPriceEach.toFloat() * item.itemAmount)
        }
        totalItemsPrice.value = totalPrice.roundToInt()
    }

    val totalItems = mutableStateOf(0)

    init {
        getItemsFromUser()
        getProfileData()

    }

    private fun getShop(shopId: String) {
        repo.getShop.invoke(shopId).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state.value = state.value.copy(displayPb = false)
                }
                is Resource.Success -> {
                    state.value = state.value.copy(shop = result.data?.shop)
                    Log.e("SuccesShop", result.data?.shop.toString())
                    result.data?.shop?.let {
                        currentShop.value = it
                    }
                    state.value = state.value.copy(displayPb = false)
                }
                is Resource.Error -> {
                    state.value = state.value.copy(errorMsg = result.message)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun placeOrder() {
        if (canPlaceOrder()) {
            val order = Order1(
                orderId = "$timesTamp",
                orderTime = "$timesTamp",
                orderStatus = "In Progress",
                orderCost = "" + totalItemsPrice.value,
                orderBy = currentUserUid,
                orderTo = currentShop.value.uid!!,
            )

            repo.placeOrder.invoke(currentShop.value.uid!!, order = order, carItemsToBuy =_shopCartItems).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        state.value = state.value.copy(displayPb = true)
                    }
                    is Resource.Success -> {
                        state.value = state.value.copy(successPlacedOrder = true)
                        state.value = state.value.copy(displayPb = false)
                        //Manaege ssucess

                    }
                    is Resource.Error -> {
                        state.value = state.value.copy(errorMsg = result.message)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun canPlaceOrder(): Boolean {

        //user can not place order if he has no location(longitude,latitude)
        if (currentUser.value.longitude == 0f && currentUser.value.latitude == 0f) {
            state.value = state.value.copy(errorMsg = "You need to set your location")
            return false
        } else if (_shopCartItems.isEmpty()) {
            //user can place order if he has items in his cart
            state.value = state.value.copy(errorMsg = "You have no items in your cart")
            return false
        } else if (currentUser.value.phone == null) {
            //user can not place order if he has no phone number
            state.value = state.value.copy(errorMsg = "You need to set your phone number")
            return false
        } else if (currentShop.value.uid == null) {
            return false
        }
        return true

    }

    @ExperimentalCoroutinesApi
    fun getProfileData() {
        repo.getUSerData.invoke("user").onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state.value = state.value.copy(displayPb = true)
                }
                is Resource.Success -> {
                    state.value = state.value.copy(user = result.data!!.user)
                    state.value = state.value.copy(displayPb = false)
                    state.value.user?.let {
                        currentUser.value = it
                    }

                }
                is Resource.Error -> {
                    state.value = state.value.copy(errorMsg = result.message)
                    state.value = state.value.copy(displayPb = false)
                }
            }
        }.launchIn(viewModelScope)
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
                    totalItems.value = it.size
                    _shopCartItems.add(item)
                }
                getShop(_shopCartItems[0].shopId)
                getTotalPriceItems()
            }
        }
    }


}