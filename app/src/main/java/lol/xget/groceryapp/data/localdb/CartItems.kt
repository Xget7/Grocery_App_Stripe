package lol.xget.groceryapp.data.localdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items_table")

data class CartItems(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var itemPhoto : String? = null,
    var itemId: String? = null,
    var shopId: String? = null,
    var itemQuantity: String? = null,
    var itemName: String? = null,
    var itemPriceEach: String? = null,
    var itemPriceTotal: String? = null,
    var itemAmount: Int? = null,


)