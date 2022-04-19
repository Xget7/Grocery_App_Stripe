package lol.xget.groceryapp.seller.profileSeller.presentation

data class SellerProfileState(
    val successUpdate : Boolean? = false,
    val loading : Boolean? = false,
    val errorMsg: String? = null,
    val user: lol.xget.groceryapp.user.mainUser.domain.User? = null,
    val updateShopData: lol.xget.groceryapp.seller.mainSeller.domain.ShopModel? = null
)
