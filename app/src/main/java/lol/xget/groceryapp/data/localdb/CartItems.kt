package lol.xget.groceryapp.data.localdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items_table")

data class CartItems(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    var itemId: String,
    var itemName: String,
    var itemPriceEach: String,
    var itemPrice: String,
    var itemQuantity: String


)