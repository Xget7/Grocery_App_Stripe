package lol.xget.groceryapp.mainUser.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import lol.xget.groceryapp.common.Constants.swapList
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.mainSeller.domain.ShopModel
import lol.xget.groceryapp.mainUser.domain.User
import lol.xget.groceryapp.profileUser.use_case.UserUseCases
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class UserHomeScreenViewModel @Inject constructor(
    val repo : UserUseCases
) : ViewModel(){

    val state : MutableState<UserHomeScreenState> = mutableStateOf(UserHomeScreenState())
    val userData = mutableStateOf(User())
    val shopList = mutableStateListOf<ShopModel>()
    val currentItem = mutableStateOf(ShopModel())



    init {
        getUser()
        getShopsList()
    }

    private fun getShopsList(){
        repo.getShopsList.invoke().onEach { result ->
            when(result){
                is Resource.Loading -> {
                    state.value = state.value.copy(loading = true)
                }
                is Resource.Success -> {
                    state.value = state.value.copy(successLoad = result.data!!.successLoad)
                    state.value = state.value.copy(shopModel = result.data.shopModel)

                    //getting shops data into ShopList
                    state.value.shopModel?.let { list ->
                       shopList.swapList(list)
                    }
                }
                is Resource.Error -> {
                    state.value = state.value.copy(errorMsg = result.message)
                }
            }
        }.launchIn(viewModelScope)
    }

     private fun getUser(){
        repo.getProfileInfo.invoke("user").onEach { result ->
            when(result){
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

    fun hideErrorDialog() {
        state.value = state.value.copy(
            errorMsg = null
        )
    }


}