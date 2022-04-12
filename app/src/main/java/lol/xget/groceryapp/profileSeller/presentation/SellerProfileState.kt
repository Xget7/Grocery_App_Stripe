package lol.xget.groceryapp.profileSeller.presentation

import lol.xget.groceryapp.mainSeller.domain.ShopModel
import lol.xget.groceryapp.mainUser.domain.User

data class SellerProfileState(
    val successUpdate : Boolean? = false,
    val loading : Boolean? = false,
    val errorMsg: String? = null,
    val user: User? = null,
    val updateShopData: ShopModel? = null
)
