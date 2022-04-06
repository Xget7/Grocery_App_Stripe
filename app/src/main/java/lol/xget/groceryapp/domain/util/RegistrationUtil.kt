package lol.xget.groceryapp.domain.util

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import lol.xget.groceryapp.common.Constants
import lol.xget.groceryapp.domain.model.UserModel
import lol.xget.groceryapp.presentation.auth.register_user.RegisterUserState

class RegistrationUtil {

    companion object Verify{

        val state: MutableState<RegisterUserState> = mutableStateOf(RegisterUserState())

         fun verifyUser(
            email: String,
            password: String,
            confirmPassword: String,
            user: UserModel,
        ): Boolean {
             Log.e("RegistrationUtil", "Verifing user :D")
            if (user.fullName!!.isBlank()) {
                state.value = state.value.copy(errorMsg = "Name is empty.")
                return false
            } else if (!Constants.EMAIL_ADDRESS_PATTERN.matcher(email).matches()) {
                state.value = state.value.copy(errorMsg = "Invalid email.")
                return false
            } else if (password.isBlank() || confirmPassword.isBlank()) {
                state.value = state.value.copy(errorMsg = "Passwords are empty.")
                return false
            } else if (password != confirmPassword) {
                state.value = state.value.copy(errorMsg = "Passwords don't match.")
                return false
            } else if (user.address!!.isBlank()) {
                state.value = state.value.copy(errorMsg = "Address is empty.")
                return false
            } else if (email.isBlank()) {
                state.value = state.value.copy(errorMsg = "Email is empty.")
                return false
            } else if (user.phone!!.isBlank()) {
                state.value = state.value.copy(errorMsg = "Phone is empty.")
                return false
            }else if (user.city!!.isBlank()) {
                state.value = state.value.copy(errorMsg = "City is empty.")
                return false
            }else if (user.state!!.isBlank()) {
                state.value = state.value.copy(errorMsg = "State is empty.")
                return false
            }
            else if (user.country!!.isBlank()) {
                state.value = state.value.copy(errorMsg = "Country is empty.")
                return false
            }else return true
        }
    }
}