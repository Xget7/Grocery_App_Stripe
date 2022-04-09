package lol.xget.groceryapp.shoppingCar.presentation

data class ShopingCarState(
    val success: Boolean = false,
    val displayPb: Boolean? = false,
    val errorMsg : String? = null,
)