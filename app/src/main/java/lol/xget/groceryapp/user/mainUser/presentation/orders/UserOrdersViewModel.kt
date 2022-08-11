package lol.xget.groceryapp.user.mainUser.presentation.orders

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import lol.xget.groceryapp.common.Constants.swapList
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.seller.mainSeller.domain.ShopModel
import lol.xget.groceryapp.user.mainUser.presentation.UserHomeScreenState
import lol.xget.groceryapp.user.profileUser.use_case.UserUseCases
import lol.xget.groceryapp.user.shoppingCar.domain.Order
import javax.inject.Inject

@HiltViewModel
class UserOrdersViewModel  @Inject constructor(
    val repo: UserUseCases
) : ViewModel(){
    val state: MutableState<UserOrdersState> = mutableStateOf(UserOrdersState())
    val shopListState: MutableState<UserHomeScreenState> = mutableStateOf(UserHomeScreenState())

    val shopsList = mutableStateListOf<ShopModel>()
    val ordersList = mutableStateListOf<Order>()
    val shopTitleFromOrderList = mutableStateListOf<String>()
    private val currentUser = FirebaseAuth.getInstance().currentUser?.uid

    init {
        getShops()

    }

    private fun getShops() {
        repo.getShopsList.invoke().onEach{
                when(it){
                    is Resource.Loading -> {
                        state.value = state.value.copy(loading = true)
                    }
                    is Resource.Success -> {
                        //getting shops data into ShopList
                        shopListState.value = shopListState.value.copy(shopModel = it.data?.shopModel)
                        shopListState.value.shopModel?.let { list ->
                            shopsList.swapList(list)
                            shopListState.value = shopListState.value.copy(loading = false)
                        }
                        Log.d("ORdersViewModel", "ORders : shoplist ${shopsList}")
                        if (shopsList.isNotEmpty()){
                            getOrdersFromUser()
                        }
                    }
                    is Resource.Error -> {
                        state.value = state.value.copy(errorMsg = it.message, loading = false)
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun getOrdersFromUser(){
        repo.getOrders.invoke(shopsList, currentUser!!).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state.value = state.value.copy(loading = true)
                }
                is Resource.Success -> {
                    state.value = state.value.copy(
                        success = result.data!!.success,
                        orders = result.data.orders ,
                        successLoadOders = result.data.successLoadOders,
                        loading = result.data.loading
                    )
                    val list = state.value.orders?.toList()
                    Log.d("List", list.toString())
                    ordersList.swapList(list!!)
                    //getting shops data into ShopList

                    state.value = state.value.copy(loading = false, successLoadOders= true)

                }
                is Resource.Error -> {
                    state.value = state.value.copy(errorMsg = result.message,loading = false)
                }
            }
        }.launchIn(viewModelScope)
    }

     fun getShopTitleFromOrder(shopUid: String) : String{
        val currentShopTitle = shopsList.find { it.uid == shopUid }?.shopName
        return currentShopTitle ?: "No Title"
    }

    fun hideErrorDialog() {
        state.value = state.value.copy(
            errorMsg = null
        )
    }

}