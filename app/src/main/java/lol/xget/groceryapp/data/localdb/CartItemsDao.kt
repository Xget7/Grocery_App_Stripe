package lol.xget.groceryapp.data.localdb

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(item: CartItems)

    // Delete function is used to
    // delete data in database.
    @Delete
    suspend fun delete(item: CartItems)

    // getAllCartItems function is used to get
    // all the data of database.
    @Query("SELECT * FROM cart_items_table ORDER BY id ASC")
    fun getAllCartItems(): Flow<List<CartItems>>
}