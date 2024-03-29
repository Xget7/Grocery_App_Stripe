package lol.xget.groceryapp.auth.register.presentation.register_user

import android.app.Application
import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import android.provider.Settings.Global.getString
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import lol.xget.groceryapp.BuildConfig.MAPS_API_KEY
import lol.xget.groceryapp.R
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.auth.mapLocalization.domain.LocationModel
import lol.xget.groceryapp.domain.use_case.auth.AuthUseCases
import lol.xget.groceryapp.auth.register.presentation.register_seller.RegisterSellerState
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.user.mainUser.domain.User
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

    private val _userLatLngflow = MutableSharedFlow<LatLng>(replay = 10)

    var open = MutableLiveData<Boolean>()
    var gpsStatus = MutableLiveData<Boolean>()

    private fun verifyUser(
        email: String,
        password: String,
        confirmPassword: String,
        user: User,
    ): Boolean {

        if (user.userName!!.isBlank()) {
            _state.value = _state.value.copy(errorMsg = "Name is empty.")
            return false
        }else if (!user.userName.matches(regex = Regex("(?=.*[A-Z]).*")) ){
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
        } else if (email.isBlank()) {
            _state.value = _state.value.copy(errorMsg = "Email is empty.")
            return false
        } else if (user.phone!!.isBlank()) {
            _state.value = _state.value.copy(errorMsg = "Phone is empty.")
            return false
        }else return true
    }

    @ExperimentalCoroutinesApi
    fun registerUser(user: User, navController: NavController) {
        if (verifyUser(emailValue.value, passwordValue.value, confirmPasswordValue.value, user)) {
            regUseCase.registerCase.invoke(emailValue.value, passwordValue.value, user)
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.value = _state.value.copy(successRegister = true)
                            navController.navigate(Destinations.UserMainDestination.route)
                            navController.popBackStack()
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



    fun hideErrorDialog() {
        _state.value = _state.value.copy(
            errorMsg = null
        )
    }


    private fun closeDialog() {
        open.value = false
    }
}