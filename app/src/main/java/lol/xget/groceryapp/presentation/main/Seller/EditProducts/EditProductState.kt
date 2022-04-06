package lol.xget.groceryapp.presentation.main.Seller.EditProducts

import lol.xget.groceryapp.domain.model.ProductModel

data class EditProductState(
    val successUpdated: Boolean = false,
    val loadedSuccess: Boolean = false,
    val displayPb : Boolean = false,
    val specificProduct : ProductModel? = null,
    val errorMsg : String? = null
)