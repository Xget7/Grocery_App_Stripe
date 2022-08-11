package lol.xget.groceryapp.seller.mainSeller.presentation.SellerOrdersScreen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import lol.xget.groceryapp.common.Constants.swapList
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.seller.mainSeller.use_case.HomeSellerUseCases
import lol.xget.groceryapp.user.profileUser.use_case.UserUseCases
import lol.xget.groceryapp.user.shoppingCar.domain.Order
import javax.inject.Inject

@HiltViewModel
class SellerOrdersViewModel @Inject constructor(
    val repo: HomeSellerUseCases,
    val userRepo : UserUseCases
) :ViewModel() {
    val user = FirebaseAuth.getInstance().currentUser
    val ordersList = mutableStateListOf<Order>()
    var ordersListOriginal = mutableListOf<Order>()
    val selectedOrderFilter = mutableStateOf("")

    val state: MutableState<SellerOrderState> = mutableStateOf(SellerOrderState())
    init {
        getAllOrders()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getAllOrders() {
        repo.getAllOrders.invoke(user?.uid!!).onEach { result ->
            when(result){
                is Resource.Loading -> {
                    state.value = state.value.copy(isLoading = true)
                }
                is Resource.Success -> {

                    result.data?.let { it ->
                        it.orders?.let { list ->
                            ordersList.swapList(list)
                            ordersListOriginal = list.toMutableList()
                        }
                    }
                }
                is Resource.Error ->{
                    state.value = state.value.copy(errorMsg = result.message)
                }

            }
        }.launchIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
     fun getUserGmail(uid: String) = flow {
        val userGmail = mutableStateOf("")
        userRepo.getUSerData.invoke(uid, "user")
            .onEach { result ->
                when(result){
                    is Resource.Loading -> {
                        state.value = state.value.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        result.data?.let { it ->
                            it.user?.let { user ->
                                user.email?.let {

                                    userGmail.value =it
                                }
                            }
                        }
                        emit(userGmail.value)
                    }
                    is Resource.Error ->{
                        state.value = state.value.copy(errorMsg = result.message)
                    }

                }
        }.collect()
    }

    fun filterOrdersBy(str : String){
        viewModelScope.launch {
            ordersList.swapList(ordersListOriginal)
            if (str == "All"){
                ordersList.swapList(ordersListOriginal)
                selectedOrderFilter.value = str
                return@launch
            }
            selectedOrderFilter.value = str
            val filteredList = ordersList.filter { it.orderStatus == str  }
            ordersList.swapList(filteredList)
        }
    }

    fun hideErrorDialog() {
        state.value = state.value.copy(
            errorMsg = null
        )
    }


}