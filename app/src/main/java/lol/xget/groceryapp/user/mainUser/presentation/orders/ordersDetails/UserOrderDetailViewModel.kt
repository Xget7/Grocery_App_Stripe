package lol.xget.groceryapp.user.mainUser.presentation.orders.ordersDetails

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import lol.xget.groceryapp.common.Constants
import lol.xget.groceryapp.common.Constants.swapList
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.seller.mainSeller.domain.ShopModel
import lol.xget.groceryapp.user.mainUser.presentation.UserHomeScreenState
import lol.xget.groceryapp.user.profileUser.use_case.UserUseCases
import lol.xget.groceryapp.user.shoppingCar.domain.Order
import javax.inject.Inject

@HiltViewModel
class UserOrderDetailViewModel  @Inject constructor(
    val repo: UserUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel(){
    val state: MutableState<UserOrdersDetailState> = mutableStateOf(UserOrdersDetailState())

    val currentOrderId = mutableStateOf("")
    private val currentUser = FirebaseAuth.getInstance().currentUser?.uid



    init {
        savedStateHandle.get<String>(Constants.PARAM_ORDER)?.let { orderId ->
            currentOrderId.value = orderId
        }
    }

    private fun getOrdersFromId(){
        repo.getOrderById.invoke(currentOrderId, currentUser!!).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state.value = state.value.copy(loading = true)
                }
                is Resource.Success -> {
                    state.value = state.value.copy(
                        success = result.data!!.success,
                        orders = result.data.orders ,
                        successLoadOders = result.data.successLoadOders,
                    )
                    //getting shops data into ShopList
                    result.data.orders?.let { list ->
                        ordersList.swapList(list)
                        state.value = state.value.copy(loading = false)
                    }


                }
                is Resource.Error -> {
                    state.value = state.value.copy(loading = false)
                    state.value = state.value.copy(errorMsg = result.message)
                }
            }
        }.launchIn(viewModelScope)
    }

     fun getShopTitleFromOrder(shopUid: String) : String{
        val currentShopTitle = shopsList.find { it.uid == shopUid }?.shopName
        return currentShopTitle ?: "No Title"
    }


}