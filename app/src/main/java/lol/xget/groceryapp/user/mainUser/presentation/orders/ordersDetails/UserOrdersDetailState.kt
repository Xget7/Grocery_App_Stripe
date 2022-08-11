package lol.xget.groceryapp.user.mainUser.presentation.orders.ordersDetails

import lol.xget.groceryapp.data.localdb.CartItems
import lol.xget.groceryapp.user.shoppingCar.domain.Order

data class UserOrdersDetailState(
    val success : Boolean? = false,
    val successStatusChanged : Boolean? = false,
    val loading : Boolean? = false,
    val noItems : Boolean? = false,
    val errorMsg : String? = null,
    val order : Order? = null,
    val orderItems : List<CartItems>? = null,
){

}
