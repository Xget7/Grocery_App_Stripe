package lol.xget.groceryapp.seller.profileSeller.presentation

import android.net.Uri
import android.util.Log
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
import lol.xget.groceryapp.user.mainUser.domain.User
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class SellerAccountViewModel @Inject constructor(
    val repo: HomeSellerUseCases,
) : ViewModel() {

    private val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid
    val currentUserInstance = FirebaseAuth.getInstance()

    var open = MutableLiveData<Boolean>()

    val state: MutableState<SellerAccountState> = mutableStateOf(SellerAccountState())

    var fullNameValue = mutableStateOf("")
    val stateValue = mutableStateOf("")
    val cityValue = mutableStateOf("")
    val addressValue = mutableStateOf("")
    val phoneValue = mutableStateOf("")
    val countryValue = mutableStateOf("")
    var accountType = mutableStateOf("")

    //shop values
    var shopImage = mutableStateOf("")
    var shopBannerImage = mutableStateOf("")
    var shopNameValue = mutableStateOf("")
    var deliveryFee = mutableStateOf("")


    init {
        getProfileData()
    }


    private fun getProfileData() {
        repo.getSellerProfile.invoke(currentUserUid, "seller").onEach { result ->
            when (result) {
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
                    userFb.shopBanner?.let {
                        shopBannerImage.value = it
                    }
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
    fun updateProfile(user: User, profilePhoto: Uri?, shopBanner: Uri?) {
        Log.e(
            "profileImageIsEmpty?VmMutableVarsFromDb",
            "${shopBannerImage.value}  + ${shopImage.value} "
        )

        repo.updateShopData.invoke(user, profilePhoto, shopBanner).onEach { result ->
            when (result) {
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