package lol.xget.groceryapp.login.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import lol.xget.groceryapp.common.Constants
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.domain.use_case.auth.AuthUseCases
import lol.xget.groceryapp.domain.util.LoginUtil.UserType.getUserType
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val useCase: AuthUseCases
) : ViewModel() {


    val state: MutableState<LoginState> = mutableStateOf(LoginState())
    val emailValue = mutableStateOf("")
    val passwordValue = mutableStateOf("")

    @ExperimentalCoroutinesApi
    fun login(email: String, password: String, navController : NavController) {
        if (verify(email, password)) {
                useCase.loginCase.invoke(email, password).onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            Log.e("MAIN", "Sucess")
                            state.value = state.value.copy(email = email, password = password)
                            state.value = state.value.copy(successLogin = true)
                            val user = FirebaseAuth.getInstance().currentUser
                            getUserType(user, navController)
                        }
                        is Resource.Error -> {
                            Log.e("MAIN", "Error")
                            state.value = state.value.copy(displayPb = false)
                            state.value = state.value.copy(errorMsg = result.message)
                        }
                        is Resource.Loading -> {
                            Log.e("MAIN", "Loading")
                            state.value = state.value.copy(displayPb = true)
                        }
                    }
                }.launchIn(viewModelScope)
        }
    }


    private fun verify(email: String, password: String): Boolean {
        return if (email.trim().isBlank()) {
            state.value = state.value.copy(errorMsg = "Email is empty.")
            false
        } else if (!Constants.EMAIL_ADDRESS_PATTERN.matcher(email).matches()) {
            state.value = state.value.copy(errorMsg = "Invalid email.")
            false
        } else if (password.isBlank()) {
            state.value = state.value.copy(errorMsg = "Password is empty.")
            false
        } else {
            true
        }
    }


    fun hideErrorDialog() {
        state.value = state.value.copy(
            errorMsg = null
        )
    }
}