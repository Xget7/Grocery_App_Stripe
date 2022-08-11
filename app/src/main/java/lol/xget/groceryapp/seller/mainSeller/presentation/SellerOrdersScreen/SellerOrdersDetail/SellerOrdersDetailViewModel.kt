package lol.xget.groceryapp.seller.mainSeller.presentation.SellerOrdersScreen.SellerOrdersDetail

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import lol.xget.groceryapp.common.Constants
import lol.xget.groceryapp.common.Constants.FMC_TOPIC
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.data.localdb.CartItems
import lol.xget.groceryapp.seller.mainSeller.use_case.HomeSellerUseCases
import lol.xget.groceryapp.user.mainUser.presentation.orders.ordersDetails.UserOrdersDetailState
import lol.xget.groceryapp.user.profileUser.use_case.UserUseCases
import lol.xget.groceryapp.user.shoppingCar.domain.Order
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class SellerOrdersDetailViewModel @Inject constructor(
    val repo: UserUseCases,
    val sellerRepo: HomeSellerUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val state: MutableStateFlow<SellerOrdersDetailState> =  MutableStateFlow(SellerOrdersDetailState())
    val currentOrder = mutableStateOf(Order())
    val currentItemsList = mutableStateListOf(CartItems())

    val currentBuyerEmail = mutableStateOf("")
    val currentBuyerPhone = mutableStateOf("")


    private val currentOrderId = mutableStateOf("")
    private val currentShopId = FirebaseAuth.getInstance().currentUser!!.uid

    init {
        savedStateHandle.get<String>(Constants.PARAM_ORDER)?.let { orderId ->
            currentOrderId.value = orderId
        }
        getOrdersFromId()

    }

    private fun getOrdersFromId() {
        repo.getOrderById.invoke(currentShopId, currentOrderId.value).onEach { result ->
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
                        loading = false
                    )
                }
                is Resource.Error -> {
                    state.value = state.value.copy(errorMsg = result.message, loading = false)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun changeOrderStatus(newStatus: String) {
        sellerRepo.changeOrderStatus.invoke(currentShopId,currentOrderId.value,newStatus).onEach { result ->
            when(result){
                is Resource.Loading -> {
                    state.value = state.value.copy(loading = true)
                }
                is Resource.Success -> {
                    state.value = state.value.copy(loading = false, successStatusChanged = result.data!!.successStatusChanged)
                }
                is Resource.Error -> {
                    state.value = state.value.copy(errorMsg = result.message, loading = false)
                }
            }
        }.launchIn(viewModelScope)
    }
    private fun getItemsByOrderId(orderId: String) {
        repo.getItemsByOrderId.invoke(currentShopId, orderId).onEach { result ->
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
                    getBuyerData()
                }
                is Resource.Error -> {
                    state.value = state.value.copy(errorMsg = result.message, loading = false)
                }

            }
        }.launchIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getBuyerData() {
        val buyerId = currentOrder.value.orderBy
        if (buyerId != null) {
            repo.getUSerData.invoke(buyerId, "user").onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        state.value = state.value.copy(loading = true)
                    }
                    is Resource.Success -> {
                        result.data?.user?.let {
                            currentBuyerEmail.value = it.email!!
                            currentBuyerPhone.value = it.phone!!
                        }
                        state.value = state.value.copy(loading = false)
                    }
                    is Resource.Error -> {
                        state.value = state.value.copy(errorMsg = result.message, loading = false)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }


}