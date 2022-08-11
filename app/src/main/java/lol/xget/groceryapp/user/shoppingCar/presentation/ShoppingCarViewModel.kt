package lol.xget.groceryapp.user.shoppingCar.presentation

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.constraintlayout.compose.override
import androidx.constraintlayout.compose.parseHeader
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import lol.xget.groceryapp.common.Constants
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.data.localdb.CartItems
import lol.xget.groceryapp.data.repository.CartItemsRepoImpl
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.seller.mainSeller.domain.ShopModel
import lol.xget.groceryapp.user.mainUser.domain.User
import lol.xget.groceryapp.user.profileUser.use_case.UserUseCases
import org.json.JSONObject
import javax.inject.Inject
import kotlin.math.roundToInt
import lol.xget.groceryapp.user.shoppingCar.domain.Order as Order1

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ShoppingCarViewModel @Inject constructor(
    private val shoppingCartDb: CartItemsRepoImpl,
    val repo: UserUseCases,
) : ViewModel() {

    val _shopCartItems = mutableStateListOf<CartItems>()

    var totalItemsPrice = mutableStateOf(0)

    private val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid!!
    val state: MutableState<ShopingCarState> = mutableStateOf(ShopingCarState())

    val currentShop = mutableStateOf(ShopModel())

    val timesTamp = System.currentTimeMillis()

    val currentUser = mutableStateOf(User())

    private fun getTotalPriceItems() {
        var totalPrice = 0.0
        for (item in _shopCartItems) {
            totalPrice += (item.itemPriceEach?.toFloat()?.times(item.itemAmount!!)!!)
        }
        totalItemsPrice.value = totalPrice.roundToInt()
    }

    val totalItems = mutableStateOf(0)

    init {
        getItemsFromUser()
        getProfileData()

    }

    private fun getShop(shopId: String) {
        repo.getShop.invoke(shopId).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state.value = state.value.copy(displayPb = false)
                }
                is Resource.Success -> {
                    state.value = state.value.copy(shop = result.data?.shop)
                    Log.e("SuccesShop", result.data?.shop.toString())
                    result.data?.shop?.let {
                        currentShop.value = it
                    }
                    state.value = state.value.copy(displayPb = false)
                }
                is Resource.Error -> {
                    state.value = state.value.copy(errorMsg = result.message)
                }
            }
        }.launchIn(viewModelScope)
    }


    private fun prepareNotificationMessage(orderId: String): JSONObject {
        val NOTIFICAITON_TOPIC = "/topics/" + Constants.FMC_TOPIC
        val NOTIFICATION_TITLE = "New Order" + orderId
        val NOTIFICATION_MESSAGE = "Congratulations! You have a new order. "
        val NOTIFICATION_TYPE = "NewOrder"

        //send json
        val notificationJo = JSONObject()
        val notificationBodyJo = JSONObject()
        try {
            notificationBodyJo.put("notificationType", NOTIFICATION_TYPE)
            notificationBodyJo.put("buyerUid", currentUserUid)
            notificationBodyJo.put("sellerUid", currentShop.value.uid)
            notificationBodyJo.put("orderId", orderId)
            notificationBodyJo.put("notificationTitle", NOTIFICATION_TITLE)
            notificationBodyJo.put("notificationMessage", NOTIFICATION_MESSAGE)
            notificationJo.put("to", NOTIFICAITON_TOPIC)
            notificationJo.put("data", notificationBodyJo)
        } catch (e: Exception) {
            state.value = state.value.copy(errorMsg = e.message)
        }
        return notificationJo
    }

    private fun sendFcmNotification(json: JSONObject, orderId: String, context: Context) {
        viewModelScope.launch {
            val jOr = object: JsonObjectRequest("https://fcm.googleapis.com/fcm/send", json,
                Response.Listener<JSONObject> { response ->
                    Log.d("A", "Response is: $response")
                    prepareNotificationMessage(orderId)
                },
                Response.ErrorListener { err ->
                    state.value = state.value.copy(errorMsg = err?.message ?: "Connection Error")
                })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"
                    headers["Authorization"] = "key=" + Constants.FMC_KEY
                    return headers
                }
            }

            Volley.newRequestQueue(context).add(jOr)
        }

    }


    fun placeOrder(navController: NavController, context: Context) {

        if (canPlaceOrder()) {
            val order = Order1(
                orderId = "$timesTamp",
                orderTime = "$timesTamp",
                orderStatus = "In Progress",
                orderCost = "" + totalItemsPrice.value,
                orderBy = currentUserUid,
                orderTo = currentShop.value.uid!!,
                orderShopName = currentShop.value.shopName,
                orderDeliveryAddress = currentUser.value.address,
                orderItems = totalItems.value
            )

            repo.placeOrder.invoke(
                currentShop.value.uid!!,
                order = order,
                carItemsToBuy = _shopCartItems
            ).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        state.value = state.value.copy(displayPb = true)
                    }
                    is Resource.Success -> {
                        state.value = state.value.copy(successPlacedOrder = true, displayPb = false)
                        deleteAllItems(_shopCartItems).apply {
                            if (currentShop.value.uid != null) {
                                navController.navigate(
                                    Destinations.UserOrderDetailScreen.passOrder(
                                        currentShop.value.uid!!,
                                        order.orderId!!
                                    )
                                )
                                val notificationJson = prepareNotificationMessage(order.orderId)
                                sendFcmNotification(
                                    notificationJson,
                                    order.orderId,
                                    context = context
                                )
                            }
                        }


                    }
                    is Resource.Error -> {
                        state.value = state.value.copy(errorMsg = result.message)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun canPlaceOrder(): Boolean {

        //user can not place order if he has no location(longitude,latitude)
        if (currentUser.value.longitude == 0f && currentUser.value.latitude == 0f) {
            state.value = state.value.copy(errorMsg = "You need to set your location")
            return false
        } else if (_shopCartItems.isEmpty()) {
            //user can place order if he has items in his cart
            state.value = state.value.copy(errorMsg = "You have no items in your cart")
            return false
        } else if (currentUser.value.phone == null) {
            //user can not place order if he has no phone number
            state.value = state.value.copy(errorMsg = "You need to set your phone number")
            return false
        } else if (currentShop.value.uid == null) {
            return false
        }
        return true

    }

    @ExperimentalCoroutinesApi
    fun getProfileData() {
        repo.getUSerData.invoke(userId = currentUserUid, "user").onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state.value = state.value.copy(displayPb = true)
                }
                is Resource.Success -> {
                    state.value = state.value.copy(user = result.data!!.user)
                    state.value = state.value.copy(displayPb = false)
                    state.value.user?.let {
                        currentUser.value = it
                    }

                }
                is Resource.Error -> {
                    state.value = state.value.copy(errorMsg = result.message)
                    state.value = state.value.copy(displayPb = false)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun deleteItem(item: CartItems) {
        viewModelScope.launch(IO) {
            if (item.itemAmount!! > 1) {
                var originalItemPrice = item.itemPriceEach?.toFloat()
                var newItemPriceFloat = item.itemPriceTotal?.toFloat()

                item.itemAmount = item.itemAmount?.minus(1)
                newItemPriceFloat = newItemPriceFloat?.minus(originalItemPrice!!)

                item.itemPriceTotal = newItemPriceFloat.toString()

                shoppingCartDb.update(item)

            } else {
                shoppingCartDb.deleteItem(item)
            }
        }
        getTotalPriceItems()
    }

    fun deleteAllItemsFromItem(item: CartItems) {
        TODO("REview")
        viewModelScope.launch(IO) {
            val listOfItems = arrayListOf(item.itemAmount)
            for (it in listOfItems) {
                shoppingCartDb.deleteItem(item)
                shoppingCartDb.update(item)
                getTotalPriceItems()
            }

        }

    }

    fun addItem(item: CartItems) {
        viewModelScope.launch(IO) {
            val originalItemPrice = item.itemPriceEach?.toFloat()
            var newItemPriceFloat = item.itemPriceTotal?.toFloat()

            item.itemAmount = item.itemAmount?.plus(1)
            newItemPriceFloat = originalItemPrice?.let { newItemPriceFloat?.plus(it) }


            item.itemPriceTotal = newItemPriceFloat.toString()
            shoppingCartDb.update(item)
        }
        getTotalPriceItems()
    }

    private fun deleteAllItems(_shopCartItems: List<CartItems>) {
        viewModelScope.launch(IO) {
            delay(300)
            shoppingCartDb.deleteAllItems(_shopCartItems)
            getTotalPriceItems()
        }
    }


    private fun getItemsFromUser() {
        viewModelScope.launch(IO) {
            delay(300)
            shoppingCartDb.readAllItems.collect {
                if (_shopCartItems.size >= 1) {
                    _shopCartItems.clear()
                }
                for (item in it) {
                    if (item.itemBuyerId == currentUserUid) {
                        totalItems.value = it.size
                        _shopCartItems.add(item)
                    }
                }
                if (_shopCartItems.size >= 1) {
                    _shopCartItems[0].shopId?.let { it1 -> getShop(it1) }
                    getTotalPriceItems()
                } else {
                    state.value = state.value.copy(noItemsInCart = true)
                }
            }
        }
    }
}


