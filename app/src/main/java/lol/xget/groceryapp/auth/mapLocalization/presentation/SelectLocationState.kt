package lol.xget.groceryapp.auth.mapLocalization.presentation

data class SelectLocationState (
    var success : Boolean? = null,
    val displayPb : Boolean? = null,
    val errorMsg: String? = null,
    val longitude : Double? = null,
    val latitude : Double? = null,
    val country : String? = null,
    val state : String? = null,
    val city : String? = null,
    val address : String? = null,

    )