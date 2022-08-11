package lol.xget.groceryapp.seller.mainSeller.presentation

import lol.xget.groceryapp.seller.mainSeller.domain.ProductModel
import lol.xget.groceryapp.seller.mainSeller.domain.ShopModel
import lol.xget.groceryapp.user.mainUser.domain.User

data class SellerHomeState(
    val successLoad : Boolean? = false,
    val loading : Boolean? = false,
    val errorMsg: String? = null,
    val user: User? = null,
    val productModel: List<ProductModel>? = null,
    val shopModel : ShopModel? = null,
    val successDeleted : Boolean? = false,
)