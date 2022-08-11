package lol.xget.groceryapp.common

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.regex.Pattern

object Constants {
    val gson = Gson()

    const val LONGITUDE = "longitude"
    const val LATITUDE= "latitude"

    const val FMC_KEY = "AAAAhmX8WrU:APA91bEzp71R3ubbnVaJNWPLYK7Vou4lTOMVOJuILSyE3HtbjGyM5hDNoHWDACek_z_kS9FWesGMD_KGXx-veUkVwiIm5GMGqgEKL-bKD3VUUMCMRKSTUUMmihYMEK--xbcr6oWnhR4h"
    const val FMC_TOPIC = "PUSH_NOTIFICATIONS"

    val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )


    const val PARAM_PRODUCT = "productId"
    const val PARAM_SHOP = "shopId"
    const val PARAM_ORDER= "orderId"


    val listOfCategories = listOf(
        "Beverages",
        "Beauty & Personal Care",
        "Snacks",
        "Breakfast",
        "Baby",
        "Cooking Needs",
        "Frozen Food",
        "Fruits",
        "Alcohol",
        "Butcher",
        "Vegetables",
        "Dairy"
        )

    val listOfOrdersState = listOf(
        "All",
        "In Progress",
        "Canceled",
        "Completed"
    )

    const val NOTIFICATION_REQUEST_CODE = 1001
    const val NOTIFICATION_CHANNEL_CODE = "notification_channel"
    const val NOTIFICATION_CHANNEL_NAME = "lol.xget.groceryapp"
    fun <T> SnapshotStateList<T>.swapList(newList: List<T>){
        clear()
        addAll(newList)
    }

     inline fun <reified T> Map<String, Any?>.toDataClass(): T {

        return convert()
    }

    //convert an object of type I to type O
    inline fun <I, reified O> I.convert(): O {
        val json = gson.toJson(this)
        return gson.fromJson(json, object : TypeToken<O>() {}.type)
    }




}
