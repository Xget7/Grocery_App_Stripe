package lol.xget.groceryapp.presentation.main.User.Home

import lol.xget.groceryapp.domain.model.ShopModel
import lol.xget.groceryapp.domain.model.UserModel

data class UserHomeScreenState(
    val successLoad : Boolean? = false,
    val loading : Boolean? = false,
    val errorMsg: String? = null,
    val userModel: UserModel? = null,
    val shopModel: List<ShopModel>? = null
)
