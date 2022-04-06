package lol.xget.groceryapp.presentation.auth.register_user

import android.app.Activity
import android.app.Application
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.domain.model.LocationModel
import lol.xget.groceryapp.domain.model.UserModel
import lol.xget.groceryapp.domain.use_case.auth.AuthUseCases
import lol.xget.groceryapp.domain.use_case.auth.LocationUseCase
import lol.xget.groceryapp.domain.util.RegistrationUtil
import lol.xget.groceryapp.presentation.auth.map_select_location.SelectLocationState
import lol.xget.groceryapp.presentation.auth.register_seller.RegisterSellerState
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RegisterUserViewModel @Inject constructor(
    private val regUseCase: AuthUseCases,
    val application: Application
) : ViewModel() {

    val _state =  mutableStateOf(RegisterSellerState())

    private val _stateFlow  = MutableStateFlow(RegisterUserState())
    val stateFlow = _stateFlow.asStateFlow()

    val fullNameValue = mutableStateOf("")
    val emailValue = mutableStateOf("")
    val stateValue = mutableStateOf("")
    val userUid = mutableStateOf("")
    val countryValue = mutableStateOf("")
    val cityValue = mutableStateOf("")
    val addressValue = mutableStateOf("")
    val phoneValue = mutableStateOf("")
    val passwordValue = mutableStateOf("")
    val confirmPasswordValue = mutableStateOf("")

    val locationStateFlow = MutableStateFlow(LocationModel(0.0,0.0))
    private val _userLatLngflow = MutableSharedFlow<LatLng>(replay = 10)
    val userLatLngflow = _userLatLngflow.asSharedFlow()

    var open = MutableLiveData<Boolean>()
    var gpsStatus = MutableLiveData<Boolean>()


    @ExperimentalCoroutinesApi
    fun registerUser(user: UserModel) {
        if (RegistrationUtil.verifyUser(emailValue.value, passwordValue.value, confirmPasswordValue.value, user)) {
            regUseCase.registerCase.invoke(emailValue.value, passwordValue.value, user)
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.value = _state.value.copy(successRegister = true)
                        }
                        is Resource.Loading -> {
                            _state.value = _state.value.copy(displayPb = true)
                        }
                        is Resource.Error -> {
                            _state.value = _state.value.copy(successRegister = false)
                            _state.value = _state.value.copy(displayPb = false)
                            _state.value = _state.value.copy(errorMsg = result.message)
                        }
                    }
                }.launchIn(viewModelScope)
        }

    }


    fun setLocationAddresses(latitude : Double, longitude: Double){
        viewModelScope.launch {
            open.value = true
            if (latitude != 0.0 && longitude != 0.0){
                val geoCoder= Geocoder(application, Locale.getDefault())
                val addresses = geoCoder.getFromLocation(latitude, longitude, 1)
                countryValue.value =  addresses[0].countryName
                stateValue.value =  addresses[0].adminArea
                cityValue.value = addresses[0].locality
                addressValue.value =  addresses[0].getAddressLine(0)
            }else{
                _state.value = _state.value.copy(errorMsg = "Cannot get location")
                hideErrorDialog()
            }
            closeDialog()
        }

    }

    fun locationEnabled() {
        val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsStatus.value = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun hideErrorDialog() {
        _state.value = _state.value.copy(
            errorMsg = null
        )
    }


    private fun closeDialog() {
        open.value = false
    }
}