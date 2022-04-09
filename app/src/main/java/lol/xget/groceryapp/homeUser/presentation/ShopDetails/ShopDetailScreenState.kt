package lol.xget.groceryapp.homeUser.presentation.ShopDetails

import lol.xget.groceryapp.homeSeller.domain.ProductModel
import lol.xget.groceryapp.homeSeller.domain.ShopModel

data class ShopDetailScreenState(
    val success : Boolean? = false,
    val loading: Boolean? = false,
    val errorMsg : String? = null,
    val specificShopModel : ShopModel?= null,
    val productsList : List<ProductModel>?= null
)