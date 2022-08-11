package lol.xget.groceryapp.user.mainUser.presentation.ShopDetails

import lol.xget.groceryapp.seller.mainSeller.domain.ProductModel
import lol.xget.groceryapp.seller.mainSeller.domain.ShopModel
import lol.xget.groceryapp.user.mainUser.domain.Review

data class ShopDetailScreenState(
    val success : Boolean? = false,
    val loading: Boolean? = false,
    val errorMsg : String? = null,
    val specificShopModel : ShopModel?= null,
    val productsList : List<ProductModel>?= null,
    val reviewsList : List<Review>? = null,
)