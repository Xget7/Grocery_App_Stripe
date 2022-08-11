package lol.xget.groceryapp.user.profileUser.use_case

import lol.xget.groceryapp.seller.mainSeller.use_case.ChangeOrderStatus
import lol.xget.groceryapp.user.mainUser.use_case.GetItemsByOrderId
import lol.xget.groceryapp.user.mainUser.use_case.GetShopsList
import lol.xget.groceryapp.user.shoppingCar.use_case.GetShopData
import lol.xget.groceryapp.user.shoppingCar.use_case.GetUserData
import lol.xget.groceryapp.user.shoppingCar.use_case.GetUserDataFromReviews
import lol.xget.groceryapp.user.shoppingCar.use_case.PlaceOrderUseCase

data class UserUseCases(
    val updateProfile: UpdateProfileUseCase,
    val getProfileInfo: GetProfileUseCase,
    val getShopsList: GetShopsList,
    val getShop: GetShopData,
    val getUSerData : GetUserData,
    val getUSerDataFromReviews : GetUserDataFromReviews,
    val placeOrder : PlaceOrderUseCase,
    val getOrders : GetOrdersUseCase,
    val getOrderById : GetOrderByIdUseCase,
    val getItemsByOrderId : GetItemsByOrderId,

    )