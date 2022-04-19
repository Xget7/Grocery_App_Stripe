package lol.xget.groceryapp.seller.mainSeller.presentation.EditProducts

data class EditProductState(
    val successUpdated: Boolean = false,
    val loadedSuccess: Boolean = false,
    val displayPb : Boolean = false,
    val specificProduct : lol.xget.groceryapp.seller.mainSeller.domain.ProductModel? = null,
    val errorMsg : String? = null
)