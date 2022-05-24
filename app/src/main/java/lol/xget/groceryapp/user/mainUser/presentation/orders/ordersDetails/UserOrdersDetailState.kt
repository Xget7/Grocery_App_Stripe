package lol.xget.groceryapp.user.mainUser.presentation.orders.ordersDetails

import lol.xget.groceryapp.user.shoppingCar.domain.Order

data class UserOrdersDetailState(
    val success : Boolean? = false,
    val successLoadOders : Boolean? = false,
    val loading : Boolean? = false,
    val errorMsg : String? = null,
    val orders : List<Order>? = null,
)
