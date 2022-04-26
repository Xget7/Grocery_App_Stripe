package lol.xget.groceryapp.seller.profileSeller.presentation

import lol.xget.groceryapp.seller.mainSeller.domain.ShopModel
import lol.xget.groceryapp.user.mainUser.domain.User

data class SellerAccountState(
    val successUpdate : Boolean? = false,
    val loading : Boolean? = false,
    val errorMsg: String? = null,
    val user: User? = null,
    val updateShopData: ShopModel? = null
)
