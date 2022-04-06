package lol.xget.groceryapp.presentation.main.Seller.Home

import lol.xget.groceryapp.domain.model.ProductModel
import lol.xget.groceryapp.domain.model.ShopModel
import lol.xget.groceryapp.domain.model.UserModel

data class SellerHomeState(
    val successLoad : Boolean? = false,
    val loading : Boolean? = false,
    val errorMsg: String? = null,
    val userModel: UserModel? = null,
    val productModel: List<ProductModel>? = null,
    val shopModel : ShopModel? = null,
    val successDeleted : Boolean? = false,
)