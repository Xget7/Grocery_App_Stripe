package lol.xget.groceryapp.profileUser.presentation

import lol.xget.groceryapp.mainUser.domain.User

data class ProfileState(
    val successUpdate : Boolean? = false,
    val loading : Boolean? = false,
    val errorMsg: String? = null,
    val user: User? = null
)
