package lol.xget.groceryapp.presentation.main.Seller.Profile

import lol.xget.groceryapp.domain.model.ShopModel
import lol.xget.groceryapp.domain.model.UserModel
import lol.xget.groceryapp.domain.use_case.homeSeller.UpdateShopData

data class SellerProfileState(
    val successUpdate : Boolean? = false,
    val loading : Boolean? = false,
    val errorMsg: String? = null,
    val userModel: UserModel? = null,
    val updateShopData: ShopModel? = null
)
