package lol.xget.groceryapp.register.presentation.register_seller

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
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.mainUser.domain.User
import lol.xget.groceryapp.register.use_case.RegisterUseCase
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

    val userNameValue = mutableStateOf("")
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



    fun setLocationAddresses(latitude : Double, longitude: Double){
        viewModelScope.launch(Main) {
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

    fun verifyUser(
        email: String,
        password: String,
        confirmPassword: String,
        user: User,
    ): Boolean {
        if (user.userName!!.isBlank()) {
            _state.value = state.value.copy(errorMsg = "Name is empty.")
            return false
        }else if (!user.userName.matches(regex = Regex("(?=.*[A-Z]).*")) ){
            _state.value = state.value.copy(errorMsg = "Name need at least 1 mayus.")
            return false
        } else if (!email.trim().matches(Regex("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"))) {
            _state.value = state.value.copy(errorMsg = "Invalid email.")
            return false
        } else if (password.isBlank() || confirmPassword.isBlank()) {
            _state.value = state.value.copy(errorMsg = "Passwords are empty.")
            return false
        }else if (password.length < 6) {
            _state.value = state.value.copy(errorMsg = "Password need at least 6 characters.")
            return false
        } else if (password != confirmPassword) {
            _state.value = state.value.copy(errorMsg = "Passwords don't match.")
            return false
        } else if (user.address!!.isBlank()) {
            _state.value = state.value.copy(errorMsg = "Address is empty.")
            return false
        } else if (email.isBlank()) {
            _state.value = state.value.copy(errorMsg = "Email is empty.")
            return false
        } else if (user.phone!!.isBlank()) {
            _state.value = state.value.copy(errorMsg = "Phone is empty.")
            return false
        }else if (user.city!!.isBlank()) {
            _state.value = state.value.copy(errorMsg = "City is empty.")
            return false
        }else if (user.state!!.isBlank()) {
            _state.value = state.value.copy(errorMsg = "State is empty.")
            return false
        }
        else if (user.country!!.isBlank()) {
            _state.value = state.value.copy(errorMsg = "Country is empty.")
            return false
        }else return true
    }

    @ExperimentalCoroutinesApi
    fun registerUser(user: User) {
        if (verifyUser(
                emailValue.value ,
                passwordValue.value,
                confirmPasswordValue.value,
                user
            )
        ) {
            regUseCase.invoke(emailValue.value.trim(), passwordValue.value, user).onEach { result ->
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
