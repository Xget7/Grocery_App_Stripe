package lol.xget.groceryapp.homeUser.presentation.ProductDetail

import lol.xget.groceryapp.homeSeller.domain.ProductModel


data class ProductDetailState(
    val success : Boolean? = false,
    val loading: Boolean? = false,
    val errorMsg : String? = null,
    val currentProduct : ProductModel? = null,
)
