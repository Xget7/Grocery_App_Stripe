package lol.xget.groceryapp.seller.mainSeller.domain

data class ProductModel(
    val productId: String? = null,
    val productTitle: String?= null,
    val productDescription: String?= null,
    val productCategory: String?= null,
    val productQuantity: String?= null,
    var productPhoto: String? = null,
    val productPrice: String?= null,
    val discountPrice: String?= null,
    val discountNote: String?= null,
    val discountAvailable: Boolean? = null,
    var timestamp: String? = null,
    val uid: String? = null
)