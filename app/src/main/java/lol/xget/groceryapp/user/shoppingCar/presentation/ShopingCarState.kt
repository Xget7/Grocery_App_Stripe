package lol.xget.groceryapp.user.shoppingCar.presentation

import lol.xget.groceryapp.seller.mainSeller.domain.ShopModel
import lol.xget.groceryapp.user.mainUser.domain.User

data class ShopingCarState(
    val successPlacedOrder: Boolean? = false,
    val displayPb: Boolean? = false,
    val shop : ShopModel? = null,
    val user : User? = null,
    val errorMsg : String? = null,
)