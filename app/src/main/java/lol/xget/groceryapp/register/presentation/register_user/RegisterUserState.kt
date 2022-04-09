package lol.xget.groceryapp.register.presentation.register_user

data class RegisterUserState(
    val successRegister: Boolean = false,
    val displayPb: Boolean = false,
    val errorMsg : String? = null
)
