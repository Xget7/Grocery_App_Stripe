package lol.xget.groceryapp.seller.mainSeller.presentation

data class SellerHomeState(
    val successLoad : Boolean? = false,
    val loading : Boolean? = false,
    val errorMsg: String? = null,
    val user: lol.xget.groceryapp.user.mainUser.domain.User? = null,
    val productModel: List<lol.xget.groceryapp.seller.mainSeller.domain.ProductModel>? = null,
    val shopModel : lol.xget.groceryapp.seller.mainSeller.domain.ShopModel? = null,
    val successDeleted : Boolean? = false,
)