package lol.xget.groceryapp.presentation.main.User.Home.ShopDetails

import lol.xget.groceryapp.domain.model.ProductModel
import lol.xget.groceryapp.domain.model.ShopModel

data class ShopDetailScreenState(
    val success : Boolean? = false,
    val loading: Boolean? = false,
    val errorMsg : String? = null,
    val specificShopModel : ShopModel?= null,
    val productsList : List<ProductModel>?= null
)