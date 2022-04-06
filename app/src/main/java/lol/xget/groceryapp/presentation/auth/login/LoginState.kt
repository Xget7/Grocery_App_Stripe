package lol.xget.groceryapp.presentation.auth.login

import androidx.annotation.StringRes

data class LoginState(
    val email: String = "",
    val password: String = "",
    val successLogin: Boolean = false,
    val displayPb: Boolean = false,
   val errorMsg : String? = null
)