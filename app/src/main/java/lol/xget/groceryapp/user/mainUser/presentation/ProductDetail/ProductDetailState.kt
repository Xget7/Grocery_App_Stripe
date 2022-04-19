package lol.xget.groceryapp.user.mainUser.presentation.ProductDetail


data class ProductDetailState(
    val success : Boolean? = false,
    val loading: Boolean? = false,
    val errorMsg : String? = null,
    val currentProduct : lol.xget.groceryapp.seller.mainSeller.domain.ProductModel? = null,
)
