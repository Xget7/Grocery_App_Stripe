package lol.xget.groceryapp.user.shoppingCar.domain

data class Order(
    val orderId : String,
    val orderTime : String,
    val orderStatus : String,
    val orderCost : String,
    val orderBy: String,
    val orderTo: String,
)
