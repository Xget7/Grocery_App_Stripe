package lol.xget.groceryapp.homeUser.presentation

import lol.xget.groceryapp.homeSeller.domain.ShopModel
import lol.xget.groceryapp.homeUser.domain.User

data class UserHomeScreenState(
    val successLoad : Boolean? = false,
    val loading : Boolean? = false,
    val errorMsg: String? = null,
    val user: User? = null,
    val shopModel: List<ShopModel>? = null
)
