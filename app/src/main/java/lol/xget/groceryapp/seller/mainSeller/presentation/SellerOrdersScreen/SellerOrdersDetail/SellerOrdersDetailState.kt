package lol.xget.groceryapp.seller.mainSeller.presentation.SellerOrdersScreen.SellerOrdersDetail

import lol.xget.groceryapp.data.localdb.CartItems
import lol.xget.groceryapp.user.mainUser.presentation.orders.ordersDetails.UserOrdersDetailState
import lol.xget.groceryapp.user.shoppingCar.domain.Order

data class SellerOrdersDetailState(
    val success : Boolean? = false,
    val loading : Boolean? = false,
    val successStatusChanged : Boolean? = false,
    val noItems : Boolean? = false,
    val errorMsg : String? = null,
    val order : Order? = null,
    val orderItems : List<CartItems>? = null,
) {
    companion object{
        fun orderStatusChanged(boolean: Boolean) = SellerOrdersDetailState(successStatusChanged = boolean)
    }
}