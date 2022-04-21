package lol.xget.groceryapp.user.mainUser.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import lol.xget.groceryapp.common.Constants.swapList
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.seller.mainSeller.domain.ShopModel
import lol.xget.groceryapp.user.mainUser.presentation.components.categories.getFoodCategory
import lol.xget.groceryapp.user.profileUser.use_case.UserUseCases
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class UserHomeScreenViewModel @Inject constructor(
    val repo: UserUseCases
) : ViewModel() {

    val state: MutableState<UserHomeScreenState> = mutableStateOf(UserHomeScreenState())
    val userData = mutableStateOf(lol.xget.groceryapp.user.mainUser.domain.User())
    val shopList = mutableStateListOf<ShopModel>()

    val shopListFilteredByLocation = mutableStateListOf<ShopModel>()

    val originalShopList = mutableStateListOf<ShopModel>()


    val currentItem = mutableStateOf(ShopModel())

    val query = mutableStateOf("")


    init {
        getUser()
        getShopsList()
    }

    private fun getShopsList() {
        repo.getShopsList.invoke().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state.value = state.value.copy(loading = true)
                }
                is Resource.Success -> {
                    state.value = state.value.copy(successLoad = result.data!!.successLoad)
                    state.value = state.value.copy(shopModel = result.data.shopModel)

                    //getting shops data into ShopList
                    state.value.shopModel?.let { list ->
                        shopList.swapList(list)
                        originalShopList.swapList(list)
                    }
                }
                is Resource.Error -> {
                    state.value = state.value.copy(errorMsg = result.message)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getUser() {
        repo.getProfileInfo.invoke("user").onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state.value = state.value.copy(loading = true)
                }
                is Resource.Success -> {
                    state.value = state.value.copy(successLoad = result.data!!.successUpdate)
                    state.value = state.value.copy(user = result.data.user)

                    //getting user firebase data into UserData
                    state.value.user?.let {
                        userData.value = it
                        Log.e("userData", userData.value.toString())
                    }

                }
                is Resource.Error -> {
                    state.value = state.value.copy(errorMsg = result.message)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun newSearch() {
        viewModelScope.launch {
            query.value.lowercase(Locale.ROOT)
            if (query.value.isNotEmpty()) {
                shopList.swapList(originalShopList)
                val result = shopList.filter {
                    it.shopName!!.lowercase(Locale.ROOT).contains(query.value, true)
                }
                if (result.isNotEmpty()) {
                    shopList.swapList(result)
                    state.value = state.value.copy(searchError = false)
                } else if (query.value == "" || query.value == " "){
                    shopList.swapList(originalShopList)
                    state.value = state.value.copy(searchError = false)

                }else{
                    state.value = state.value.copy(searchError = true)
                }
            } else if (query.value == ""){
                state.value = state.value.copy(searchError = false)
                shopList.swapList(originalShopList)
            }
        }
    }

    fun onQueryChanged(query: String) {
        viewModelScope.launch {
            this@UserHomeScreenViewModel.query.value = query
            Log.e("onQueryChanged", " = $query")
        }
    }


    fun hideErrorDialog() {
        state.value = state.value.copy(
            errorMsg = null
        )
    }


}