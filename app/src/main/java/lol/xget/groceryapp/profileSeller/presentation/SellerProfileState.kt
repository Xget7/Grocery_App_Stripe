package lol.xget.groceryapp.profileSeller.presentation

import lol.xget.groceryapp.homeSeller.domain.ShopModel
import lol.xget.groceryapp.homeUser.domain.User

data class SellerProfileState(
    val successUpdate : Boolean? = false,
    val loading : Boolean? = false,
    val errorMsg: String? = null,
    val user: User? = null,
    val updateShopData: ShopModel? = null
)
