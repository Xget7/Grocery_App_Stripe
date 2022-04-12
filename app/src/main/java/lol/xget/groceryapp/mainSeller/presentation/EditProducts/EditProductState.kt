package lol.xget.groceryapp.mainSeller.presentation.EditProducts

import lol.xget.groceryapp.mainSeller.domain.ProductModel

data class EditProductState(
    val successUpdated: Boolean = false,
    val loadedSuccess: Boolean = false,
    val displayPb : Boolean = false,
    val specificProduct : ProductModel? = null,
    val errorMsg : String? = null
)