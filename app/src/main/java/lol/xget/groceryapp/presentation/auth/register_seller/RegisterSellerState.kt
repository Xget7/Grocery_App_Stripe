package lol.xget.groceryapp.presentation.auth.register_seller

data class RegisterSellerState(
    val successRegister: Boolean = false,
    val displayPb: Boolean? = false,
    val errorMsg : String? = null,
    val successMap : Boolean? = false,
    val latitude : Double? = null,
    val longitude : Double? = null,

)
