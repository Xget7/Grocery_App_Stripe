package lol.xget.groceryapp.presentation.main.User.Home

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
import lol.xget.groceryapp.domain.model.ProductModel
import lol.xget.groceryapp.domain.model.ShopModel
import lol.xget.groceryapp.domain.model.UserModel
import lol.xget.groceryapp.domain.use_case.profile.UserUseCases
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class UserHomeScreenViewModel @Inject constructor(
    val repo : UserUseCases
) : ViewModel(){

    val state : MutableState<UserHomeScreenState> = mutableStateOf(UserHomeScreenState())
    val userData = mutableStateOf(UserModel())
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

                    //getting user firebase data into UserData
                    state.value.shopModel?.let { list ->
                       shopList.swapList(list)
                        Log.e("shopList", shopList.toString())
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
                    state.value = state.value.copy(userModel = result.data.userModel)

                    //getting user firebase data into UserData
                    state.value.userModel?.let {
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