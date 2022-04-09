package lol.xget.groceryapp.domain.util

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import lol.xget.groceryapp.common.Constants
import lol.xget.groceryapp.homeUser.domain.User
import lol.xget.groceryapp.homeUser.domain.UserEmail
import lol.xget.groceryapp.homeUser.domain.UserPassword
import lol.xget.groceryapp.register.presentation.register_user.RegisterUserState

class RegistrationUtil {

    companion object {

        val state: MutableState<RegisterUserState> = mutableStateOf(RegisterUserState())



         fun verifyUser(
             email: String,
             password: UserPassword,
             confirmPassword: UserPassword,
             user: User,
        ): Boolean {
            if (user.fullName!!.value.isBlank()) {
                state.value = state.value.copy(errorMsg = "Name is empty.")
                return false
            }else if (user.fullName!!.validateMayus()){
                state.value = state.value.copy(errorMsg = "Name need at least 1 mayus.")
                return false
            } else if (UserEmail(email).verifyEmail()) {
                state.value = state.value.copy(errorMsg = "Invalid email.")
                return false
            } else if (password.value.isBlank() || confirmPassword.value.isBlank()) {
                state.value = state.value.copy(errorMsg = "Passwords are empty.")
                return false
            }else if (password.verifyLength()) {
                state.value = state.value.copy(errorMsg = "Password need at least 6 characters.")
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