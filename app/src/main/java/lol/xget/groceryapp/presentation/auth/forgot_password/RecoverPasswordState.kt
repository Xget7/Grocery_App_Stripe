package lol.xget.groceryapp.presentation.auth.forgot_password

data class RecoverPasswordState(
    val successRecovered: Boolean? = false,
    val displayPb: Boolean = false,
    val errorMsg: String? = null
)
