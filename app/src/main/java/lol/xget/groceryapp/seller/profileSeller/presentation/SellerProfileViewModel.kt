package lol.xget.groceryapp.seller.profileSeller.presentation

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.seller.mainSeller.use_case.HomeSellerUseCases
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class SellerProfileViewModel @Inject constructor(
    val repo : HomeSellerUseCases,
) : ViewModel(){

    val currentUser = FirebaseAuth.getInstance().currentUser!!.uid

    var open = MutableLiveData<Boolean>()

    val state : MutableState<SellerProfileState> = mutableStateOf(SellerProfileState())

    var fullNameValue = mutableStateOf("")
    var stateValue = mutableStateOf("")
    var cityValue = mutableStateOf("")
    var addressValue = mutableStateOf("")
    var phoneValue = mutableStateOf("")
    var countryValue = mutableStateOf("")
    var accountType = mutableStateOf("")
    //shop values
    var shopImage = mutableStateOf("")
    var shopNameValue  = mutableStateOf("")
    var deliveryFee = mutableStateOf("")



    init {
        getProfileData()
    }


    private fun getProfileData(){
        repo.getSellerProfile.invoke(currentUser, "seller").onEach { result ->
            when(result){
                is Resource.Loading -> {
                    state.value = state.value.copy(loading = true)
                }
                is Resource.Success -> {
                    state.value = state.value.copy(user = result.data!!.user)
                    state.value = state.value.copy(loading = false)
                    val userFb = state.value.user
                    shopNameValue.value = userFb?.shopName!!
                    accountType.value = userFb.accountType!!
                    fullNameValue.value = userFb.userName!!
                    stateValue.value = userFb.state!!

                    userFb.deliveryFee?.let {
                        deliveryFee.value = it
                    }
                    cityValue.value = userFb.city!!
                    countryValue.value = userFb.country!!
                    addressValue.value = userFb.address!!
                    phoneValue.value = userFb.phone!!

                    userFb.profilePhoto?.let {
                        shopImage.value = it
                    }

                }
                is Resource.Error -> {
                    state.value = state.value.copy(errorMsg = result.message)
                    state.value = state.value.copy(loading = false)
                }
            }
        }.launchIn(viewModelScope)
    }


    @ExperimentalCoroutinesApi
    fun updateProfile(user : lol.xget.groceryapp.user.mainUser.domain.User, profilePhoto: Uri?){
            repo.updateShopData.invoke(user, profilePhoto).onEach { result ->
                when(result){
                    is Resource.Loading -> {
                        state.value = state.value.copy(loading = true)
                    }
                    is Resource.Success -> {
                        state.value = state.value.copy(successUpdate = result.data!!.successUpdate)
                        state.value = state.value.copy(loading = false)
                    }
                    is Resource.Error -> {
                        state.value = state.value.copy(errorMsg = result.message)
                    }
                }
            }.launchIn(viewModelScope)

    }


    private fun closeDialog() {
        open.value = false
    }


    fun hideErrorDialog() {
        state.value = state.value.copy(
            errorMsg = null
        )
    }


}