package lol.xget.groceryapp.seller.mainSeller.presentation.AddProducts

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.domain.use_case.products.AddProductUseCase
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val useCase : AddProductUseCase
) : ViewModel(){
    val state: MutableState<AddProductState> = mutableStateOf(AddProductState())

    val productTitle = mutableStateOf("")
    var productId = mutableStateOf("")
    val productDescription = mutableStateOf("")
    val productCategory = mutableStateOf("")
    val productQuantity = mutableStateOf("")
    val productPrice = mutableStateOf("")
    val productPhoto = mutableStateOf("")
    val productDiscountPrice = mutableStateOf("")
    val productDiscountNote = mutableStateOf("")
    val productDiscountAvalide= mutableStateOf(false)

    val msg= mutableStateOf("Add Product")

    var open = MutableLiveData<Boolean>()



    fun uploadProductToFb(product : lol.xget.groceryapp.seller.mainSeller.domain.ProductModel, productPhoto : Uri?){
        productId.value = System.currentTimeMillis().toString()
        if (validateProduct()){
            useCase.invoke(product, productPhoto).onEach { result ->
                when(result) {
                    is Resource.Success -> {
                        state.value = state.value.copy(successAdded  = true)
                        state.value = state.value.copy(displayPb  = false)
                    }
                    is Resource.Loading -> {
                        state.value = state.value.copy(displayPb  = true)
                    }
                    is Resource.Error -> {
                        state.value = state.value.copy(errorMsg  = result.message)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }


    private fun validateProduct() : Boolean{
        if (productTitle.value.isBlank()){
            state.value = state.value.copy(errorMsg = "Product need a title.")
            return false
        }else if (productDescription.value.isBlank()){
            state.value = state.value.copy(errorMsg = "Product need a description.")
            return false
        }else if (productPrice.value.isBlank()){
            state.value = state.value.copy(errorMsg = "Product need a price.")
            return false
        }else if (productCategory.value.isBlank()){
            state.value = state.value.copy(errorMsg = "Category is required.")
            return false
        }else if (productQuantity.value.isBlank()){
        state.value = state.value.copy(errorMsg = "Quantity is required.")
        return false
        }
        if (productDiscountAvalide.value){
            if (productDiscountPrice.value.isBlank()){
                state.value = state.value.copy(errorMsg = "Discount price is required.")
                return false
            }
        }else{
            productDiscountPrice.value = "0"
            productDiscountNote.value = ""
        }
        return true
    }


    fun hideErrorDialog() {
        state.value = state.value.copy(
            errorMsg = null
        )
    }

    private fun closeDialog() {
        open.value = false
    }




}