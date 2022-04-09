package lol.xget.groceryapp.homeSeller.presentation.EditProducts

import lol.xget.groceryapp.homeSeller.domain.ProductModel

data class EditProductState(
    val successUpdated: Boolean = false,
    val loadedSuccess: Boolean = false,
    val displayPb : Boolean = false,
    val specificProduct : ProductModel? = null,
    val errorMsg : String? = null
)