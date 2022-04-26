package lol.xget.groceryapp.auth.recoverPassword.presentation

data class RecoverPasswordState(
    val successRecovered: Boolean? = false,
    val displayPb: Boolean = false,
    val errorMsg: String? = null
)
