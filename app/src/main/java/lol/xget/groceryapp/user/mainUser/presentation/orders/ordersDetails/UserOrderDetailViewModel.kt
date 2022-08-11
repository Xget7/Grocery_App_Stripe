package lol.xget.groceryapp.user.mainUser.presentation.orders.ordersDetails

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import lol.xget.groceryapp.common.Constants
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.data.localdb.CartItems
import lol.xget.groceryapp.user.profileUser.use_case.UserUseCases
import lol.xget.groceryapp.user.shoppingCar.domain.Order
import javax.inject.Inject

@HiltViewModel
class UserOrderDetailViewModel @Inject constructor(
    val repo: UserUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val state: MutableState<UserOrdersDetailState> = mutableStateOf(UserOrdersDetailState())

    val currentOrderId = mutableStateOf("")
    val currentShopId = mutableStateOf("")
    val currentOrder = mutableStateOf(Order())
    val currentItemsList = mutableStateListOf(CartItems())
    private val currentUser = FirebaseAuth.getInstance().currentUser?.uid

    init {

        Log.e("TAG", "${currentOrderId.value}+ ${currentShopId.value}")
        viewModelScope.launch {
            savedStateHandle.get<String>(Constants.PARAM_SHOP)?.let { shopId ->
                currentShopId.value = shopId
            }
            savedStateHandle.get<String>(Constants.PARAM_ORDER)?.let { orderId ->
                currentOrderId.value = orderId
            }

            getOrdersFromId()
        }


    }

    private fun getOrdersFromId() {
        repo.getOrderById.invoke( currentShopId.value,currentOrderId.value).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state.value = state.value.copy(loading = true)
                }
                is Resource.Success -> {
                    //getting shops data into ShopList
                    result.data?.order?.let { order ->
                        currentOrder.value = order
                    }
                    getItemsByOrderId(currentOrder.value.orderId!!)
                    state.value = state.value.copy(
                        success = result.data!!.success,
                        order = result.data.order,
                    )
                }
                is Resource.Error -> {
                    state.value = state.value.copy(errorMsg = result.message, loading = false)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getItemsByOrderId(orderId: String) {
        repo.getItemsByOrderId.invoke(currentShopId.value, orderId).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state.value = state.value.copy(loading = true)
                }
                is Resource.Success -> {
                    result.data?.orderItems.let { list ->
                        list?.let {
                            currentItemsList.clear()
                            currentItemsList.addAll(it)
                        }
                    }
                    state.value = state.value.copy(
                        success = result.data!!.success,
                        orderItems = result.data.orderItems,
                        noItems = result.data.noItems,
                        loading = false
                    )
                }
                is Resource.Error -> {
                    state.value = state.value.copy(errorMsg = result.message, loading = false)
                }

            }
        }.launchIn(viewModelScope)
    }


}