package lol.xget.groceryapp.mainUser.presentation.ProductDetail

import lol.xget.groceryapp.mainSeller.domain.ProductModel


data class ProductDetailState(
    val success : Boolean? = false,
    val loading: Boolean? = false,
    val errorMsg : String? = null,
    val currentProduct : ProductModel? = null,
)
