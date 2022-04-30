package lol.xget.groceryapp.user.mainUser.presentation

data class UserHomeScreenState(
    val successLoad : Boolean? = false,
    val loading : Boolean? = false,
    val errorMsg: String? = null,
    val searchError: Boolean? = false,
    val searching: Boolean? = false,
    val user: lol.xget.groceryapp.user.mainUser.domain.User? = null,
    val shopModel: List<lol.xget.groceryapp.seller.mainSeller.domain.ShopModel>? = null
)
