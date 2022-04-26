package lol.xget.groceryapp.seller.profileSeller.presentation.EditProfile

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
class EditSellerProfileViewModel @Inject constructor(
    val repo : HomeSellerUseCases,
) : ViewModel(){

    val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    val currentUserGmail = FirebaseAuth.getInstance().currentUser!!.email


    var open = MutableLiveData<Boolean>()

    val state : MutableState<EditSellerProfileState> = mutableStateOf(EditSellerProfileState())

    var fullNameValue = mutableStateOf("")
    var stateValue = mutableStateOf("")
    var cityValue = mutableStateOf("")
    var addressValue = mutableStateOf("")
    var phoneValue = mutableStateOf("")
    var countryValue = mutableStateOf("")
    var accountType = mutableStateOf("")
    //shop values
    var shopImage = mutableStateOf("")
    var shopBannerImage = mutableStateOf("")
    var shopAdBannerImage = mutableStateOf("")

    var shopNameValue  = mutableStateOf("")
    var deliveryFee = mutableStateOf("")
    var country = mutableStateOf("")
    var latitude = mutableStateOf(0f)
    var longitude = mutableStateOf(0f)


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
                    parseDbData(userFb)

                }
                is Resource.Error -> {
                    state.value = state.value.copy(errorMsg = result.message)
                    state.value = state.value.copy(loading = false)
                }
            }
        }.launchIn(viewModelScope)
    }


    @ExperimentalCoroutinesApi
    fun updateProfile(user : User, profilePhoto: Uri?, shopBanner : Uri?){
        Log.e("profileImageIsEmpty?VmMutableVarsFromDb", "${shopBannerImage.value}  + ${shopImage.value} ")

        repo.updateShopData.invoke(user, profilePhoto, shopBanner).onEach { result ->
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

    fun parseDbData(user: User?) {
        user?.shopBanner?.let {
            shopBannerImage.value = it
        }
        user?.shopAdBanner?.let {
            shopAdBannerImage.value = it
        }
        user?.profilePhoto?.let{
            shopImage.value = it
        }
        fullNameValue.value = user?.userName.toString()
        phoneValue.value = user?.phone.toString()
        country.value = user?.country.toString()
        stateValue.value = user?.state.toString()
        cityValue.value = user?.city.toString()
        addressValue.value = user?.address.toString()
        latitude.value = user?.latitude!!.toFloat()
        longitude.value = user.longitude!!.toFloat()
        shopNameValue.value = user.shopName!!.toString()
        deliveryFee.value = user.deliveryFee!!.toString()
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