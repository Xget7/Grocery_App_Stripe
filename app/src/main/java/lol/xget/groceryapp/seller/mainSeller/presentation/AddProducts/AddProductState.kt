package lol.xget.groceryapp.seller.mainSeller.presentation.AddProducts

data class AddProductState(
    val successAdded: Boolean = false,
    val displayPb : Boolean = false,
    val errorMsg : String? = null
)