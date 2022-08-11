package lol.xget.groceryapp.seller.profileSeller.presentation.config

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import lol.xget.groceryapp.GroceryApp
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.data.repository.SellerRepoImpl
import lol.xget.groceryapp.seller.mainSeller.use_case.HomeSellerUseCases
import javax.inject.Inject

@HiltViewModel
class SellerSettingsViewModel @Inject constructor(
    val repository: HomeSellerUseCases,
): ViewModel(){
    val state = mutableStateOf(SellerSettingsState())


    val notificationState = mutableStateOf(false)


     fun subscribeToNotifications(sp_editor: SharedPreferences.Editor){
        viewModelScope.launch {
            repository.subscribeToOrders(sp_editor).onEach { result ->
                when(result){
                    is Resource.Error -> {
                        state.value = state.value.copy(isError = true, errorMessage = result.message)
                    }
                    is Resource.Loading -> {
                        state.value = state.value.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        state.value = state.value.copy(isSuccess = true, isLoading = false)
                    }
                }

            }.launchIn(viewModelScope)
        }
    }
     fun unsubscribeToNotifications(sp_editor: SharedPreferences.Editor){
        viewModelScope.launch {
            repository.unsubscribeToOrders.invoke(sp_editor) .onEach { result ->
                when(result){
                    is Resource.Error -> {
                        state.value = state.value.copy(isError = true, errorMessage = result.message)
                    }
                    is Resource.Loading -> {
                        state.value = state.value.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        state.value = state.value.copy(isSuccess = true, isLoading = false)
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

}