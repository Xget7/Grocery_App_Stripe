package lol.xget.groceryapp.homeSeller.presentation.AddProducts

data class AddProductState(
    val successAdded: Boolean = false,
    val displayPb : Boolean = false,
    val errorMsg : String? = null
)