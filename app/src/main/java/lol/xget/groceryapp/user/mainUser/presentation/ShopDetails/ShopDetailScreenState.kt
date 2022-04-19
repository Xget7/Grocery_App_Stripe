package lol.xget.groceryapp.user.mainUser.presentation.ShopDetails

data class ShopDetailScreenState(
    val success : Boolean? = false,
    val loading: Boolean? = false,
    val errorMsg : String? = null,
    val specificShopModel : lol.xget.groceryapp.seller.mainSeller.domain.ShopModel?= null,
    val productsList : List<lol.xget.groceryapp.seller.mainSeller.domain.ProductModel>?= null
)