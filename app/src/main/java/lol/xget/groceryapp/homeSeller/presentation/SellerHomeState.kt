package lol.xget.groceryapp.homeSeller.presentation

import lol.xget.groceryapp.homeSeller.domain.ProductModel
import lol.xget.groceryapp.homeSeller.domain.ShopModel
import lol.xget.groceryapp.homeUser.domain.User

data class SellerHomeState(
    val successLoad : Boolean? = false,
    val loading : Boolean? = false,
    val errorMsg: String? = null,
    val user: User? = null,
    val productModel: List<ProductModel>? = null,
    val shopModel : ShopModel? = null,
    val successDeleted : Boolean? = false,
)