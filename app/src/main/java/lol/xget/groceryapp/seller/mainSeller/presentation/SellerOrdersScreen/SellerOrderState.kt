package lol.xget.groceryapp.seller.mainSeller.presentation.SellerOrdersScreen

import lol.xget.groceryapp.user.shoppingCar.domain.Order

data class SellerOrderState(
    val isSuccess: Boolean? = false,
    val isLoading: Boolean? = false,
    val orders: List<Order>? = null,
    val errorMsg: String? = null
)
