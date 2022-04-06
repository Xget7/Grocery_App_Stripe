package lol.xget.groceryapp.presentation.main.Seller.AddProducts

data class AddProductState(
    val successAdded: Boolean = false,
    val displayPb : Boolean = false,
    val errorMsg : String? = null
)