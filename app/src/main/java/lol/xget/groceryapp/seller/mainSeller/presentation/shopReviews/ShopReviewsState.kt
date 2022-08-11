package lol.xget.groceryapp.seller.mainSeller.presentation.shopReviews

data class ShopReviewsState(
    val isSuccess: Boolean? = false,
    val isLoading: Boolean? = false,
    val errorMsg: String? = null
)
