package lol.xget.groceryapp.mainUser.presentation

import lol.xget.groceryapp.mainSeller.domain.ShopModel
import lol.xget.groceryapp.mainUser.domain.User

data class UserHomeScreenState(
    val successLoad : Boolean? = false,
    val loading : Boolean? = false,
    val errorMsg: String? = null,
    val user: User? = null,
    val shopModel: List<ShopModel>? = null
)
