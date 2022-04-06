package lol.xget.groceryapp.common

import androidx.compose.runtime.snapshots.SnapshotStateList
import java.util.regex.Pattern

object Constants {
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


}
