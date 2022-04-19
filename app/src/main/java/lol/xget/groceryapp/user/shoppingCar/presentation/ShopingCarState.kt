package lol.xget.groceryapp.user.shoppingCar.presentation

data class ShopingCarState(
    val success: Boolean = false,
    val displayPb: Boolean? = false,
    val errorMsg : String? = null,
)