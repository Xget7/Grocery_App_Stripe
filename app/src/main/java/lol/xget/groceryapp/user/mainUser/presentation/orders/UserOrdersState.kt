package lol.xget.groceryapp.user.mainUser.presentation.orders

import lol.xget.groceryapp.user.shoppingCar.domain.Order

data class UserOrdersState(
    val success : Boolean? = false,
    val successLoadOders : Boolean? = false,
    val loading : Boolean? = false,
    val errorMsg : String? = null,
    val orders : List<Order>? = null,
)
