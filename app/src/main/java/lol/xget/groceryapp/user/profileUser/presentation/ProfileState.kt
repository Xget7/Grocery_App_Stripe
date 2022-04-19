package lol.xget.groceryapp.user.profileUser.presentation

data class ProfileState(
    val successUpdate : Boolean? = false,
    val loading : Boolean? = false,
    val errorMsg: String? = null,
    val user: lol.xget.groceryapp.user.mainUser.domain.User? = null
)
