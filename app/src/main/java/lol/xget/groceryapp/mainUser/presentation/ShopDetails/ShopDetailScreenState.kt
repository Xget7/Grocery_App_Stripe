package lol.xget.groceryapp.mainUser.presentation.ShopDetails

import lol.xget.groceryapp.mainSeller.domain.ProductModel
import lol.xget.groceryapp.mainSeller.domain.ShopModel

data class ShopDetailScreenState(
    val success : Boolean? = false,
    val loading: Boolean? = false,
    val errorMsg : String? = null,
    val specificShopModel : ShopModel?= null,
    val productsList : List<ProductModel>?= null
)