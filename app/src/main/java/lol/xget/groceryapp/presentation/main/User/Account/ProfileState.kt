package lol.xget.groceryapp.presentation.main.User.Account

import lol.xget.groceryapp.domain.model.UserModel

data class ProfileState(
    val successUpdate : Boolean? = false,
    val loading : Boolean? = false,
    val errorMsg: String? = null,
    val userModel: UserModel? = null
)
