package lol.xget.groceryapp.user.shoppingCar.domain

data class Order(
    val orderId : String? = null,
    val orderTime : String? = null,
    val orderStatus : String? = null,
    val orderCost : String? = null,
    val orderBy: String? = null,
    val orderTo: String? = null,
)
