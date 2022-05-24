package lol.xget.groceryapp.common

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.regex.Pattern

object Constants {
    val gson = Gson()

    val LONGITUDE = "longitude"
    val LATITUDE= "latitude"
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
