package lol.xget.groceryapp.presentation.main.User.Home.ProductDetail

import lol.xget.groceryapp.domain.model.ProductModel


data class ProductDetailState(
    val success : Boolean? = false,
    val loading: Boolean? = false,
    val errorMsg : String? = null,
    val currentProduct : ProductModel? = null,
)
