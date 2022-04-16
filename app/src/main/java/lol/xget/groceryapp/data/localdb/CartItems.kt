package lol.xget.groceryapp.data.localdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items_table")

data class CartItems(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var itemPhoto : String? = null,
    var itemId: String,
    var itemQuantity: String,
    var itemName: String,
    var itemPriceEach: String,
    var itemPriceTotal: String,
    var itemAmount: Int


)