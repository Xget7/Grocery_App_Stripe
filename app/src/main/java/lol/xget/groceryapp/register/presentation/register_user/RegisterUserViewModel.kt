package lol.xget.groceryapp.register.presentation.register_user

import android.app.Application
import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.mapLocalization.domain.LocationModel
import lol.xget.groceryapp.mainUser.domain.User
import lol.xget.groceryapp.domain.use_case.auth.AuthUseCases
import lol.xget.groceryapp.register.presentation.register_seller.RegisterSellerState
import java.lang.Exception
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

    fun verifyUser(
        email: String,
        password: String,
        confirmPassword: String,
        user: User,
    ): Boolean {

        if (user.userName!!.isBlank()) {
            _state.value = _state.value.copy(errorMsg = "Name is empty.")
            return false
        }else if (!user.userName!!.matches(regex = Regex("(?=.*[A-Z]).*")) ){
            _state.value = _state.value.copy(errorMsg = "Name need at least 1 mayus.")
            return false
        } else if (!email.trim().matches(Regex("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"))) {
            _state.value = _state.value.copy(errorMsg = "Invalid email.")
            return false
        } else if (password.isBlank() || confirmPassword.isBlank()) {
            _state.value = _state.value.copy(errorMsg = "Passwords are empty.")
            return false
        }else if (password.length < 6) {
            _state.value = _state.value.copy(errorMsg = "Password need at least 6 characters.")
            return false
        } else if (password != confirmPassword) {
            _state.value = _state.value.copy(errorMsg = "Passwords don't match.")
            return false
        } else if (user.address!!.isBlank()) {
            _state.value = _state.value.copy(errorMsg = "Address is empty.")
            return false
        } else if (email.isBlank()) {
            _state.value = _state.value.copy(errorMsg = "Email is empty.")
            return false
        } else if (user.phone!!.isBlank()) {
            _state.value = _state.value.copy(errorMsg = "Phone is empty.")
            return false
        }else if (user.city!!.isBlank()) {
            _state.value = _state.value.copy(errorMsg = "City is empty.")
            return false
        }else if (user.state!!.isBlank()) {
            _state.value = _state.value.copy(errorMsg = "State is empty.")
            return false
        }
        else if (user.country!!.isBlank()) {
            _state.value = _state.value.copy(errorMsg = "Country is empty.")
            return false
        }else return true
    }

    @ExperimentalCoroutinesApi
    fun registerUser(user: User) {
        if (verifyUser(emailValue.value, passwordValue.value, confirmPasswordValue.value, user)) {
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
        viewModelScope.launch(Dispatchers.Main) {
            open.value = true
            if (latitude != 0.0 && longitude != 0.0){
                try {
                    val geoCoder= Geocoder(application, Locale.getDefault())
                    val addresses = geoCoder.getFromLocation(latitude, longitude, 1)
                    delay(2000)
                    countryValue.value =  addresses[0].countryName
                    stateValue.value =  addresses[0].adminArea
                    cityValue.value = addresses[0].locality
                    addressValue.value =  addresses[0].getAddressLine(0)
                }catch (e: Exception){
                    _state.value = _state.value.copy(errorMsg = "Error with location")
                    hideErrorDialog()
                }
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