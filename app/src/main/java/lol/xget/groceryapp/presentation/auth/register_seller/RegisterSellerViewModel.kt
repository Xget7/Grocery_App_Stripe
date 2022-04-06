package lol.xget.groceryapp.presentation.auth.register_seller

import android.app.Activity
import android.app.Application
import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.domain.model.UserModel
import lol.xget.groceryapp.domain.use_case.auth.LocationUseCase
import lol.xget.groceryapp.domain.use_case.auth.register_use_case.RegisterUseCase
import lol.xget.groceryapp.domain.util.RegistrationUtil
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RegisterSellerViewModel @Inject constructor(
    private val regUseCase: RegisterUseCase,
    val application: Application,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val _state =  mutableStateOf(RegisterSellerState())
    val state : State<RegisterSellerState> = _state

    var open = MutableLiveData<Boolean>()
    var gpsStatus = MutableLiveData<Boolean>()

    val fullNameValue = mutableStateOf("")
    val uidValue = mutableStateOf("")
    val emailValue = mutableStateOf("")
    val shopNameValue = mutableStateOf("")
    val countryValue = mutableStateOf("")
    val longitude = mutableStateOf(0.0)
    val latitude = mutableStateOf(0.0)
    var stateValue = mutableStateOf("")
    val cityValue = mutableStateOf("")
    val addressValue = mutableStateOf("")
    val deliveryFee = mutableStateOf("")
    val phoneValue = mutableStateOf("")
    val passwordValue = mutableStateOf("")
    val confirmPasswordValue = mutableStateOf("")

    private val _stateFlow  = MutableStateFlow(RegisterSellerState())
    val stateFlow = _stateFlow.asStateFlow()

    private val _userLatLngflow = MutableSharedFlow<LatLng>(replay = 1)
    val userLatLngflow = _userLatLngflow.asSharedFlow()



    fun getLocationLive(activity: Activity) {
        LocationUseCase().invoke(activity, application).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _userLatLngflow.emit(LatLng(result.data?.latitude!!,result.data.longitude!! ))
                }
                is Resource.Loading -> {
                    _stateFlow.emit(RegisterSellerState(displayPb = true))
                }
                is Resource.Error -> {
                    _stateFlow.emit(RegisterSellerState(errorMsg = "Error gettin location"))

                }
            }
        }.launchIn(viewModelScope)

    }

    fun setLocationAddresses(latitude : Double, longitude: Double){
        viewModelScope.launch(IO) {
            open.value = true
            val geoCoder= Geocoder(application, Locale.getDefault())
            val addresses = geoCoder.getFromLocation(latitude, longitude, 1)
            countryValue.value =  addresses[0].countryName
            stateValue.value =  addresses[0].adminArea
            cityValue.value = addresses[0].locality
            addressValue.value =  addresses[0].getAddressLine(0)
            closeDialog()
        }
    }

    fun locationEnabled() {
        viewModelScope.launch {
            val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            gpsStatus.value = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }
    }

    @ExperimentalCoroutinesApi
    fun registerUser(user: UserModel) {
        if (RegistrationUtil.verifyUser(
                emailValue.value,
                passwordValue.value,
                confirmPasswordValue.value,
                user
            )
        ) {
            regUseCase.invoke(emailValue.value, passwordValue.value, user).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(successRegister = true, displayPb = false)
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(displayPb = true)
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(displayPb = false)
                        _state.value = _state.value.copy(errorMsg = result.message)
                    }
                }
            }.launchIn(viewModelScope)
        }
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
