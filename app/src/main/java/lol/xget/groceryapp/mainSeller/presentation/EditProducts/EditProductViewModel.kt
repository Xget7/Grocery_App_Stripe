package lol.xget.groceryapp.mainSeller.presentation.EditProducts

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import lol.xget.groceryapp.common.Constants
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.mainSeller.domain.ProductModel
import lol.xget.groceryapp.mainSeller.use_case.HomeSellerUseCases
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class EditProductViewModel @Inject constructor(
    private val useCase : HomeSellerUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel(){

    val state: MutableState<EditProductState> = mutableStateOf(EditProductState())

    val currentProduct = mutableStateOf(ProductModel())

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

    val msg= mutableStateOf("Update Product")

    var open = MutableLiveData<Boolean>()


    init {
        viewModelScope.launch(Dispatchers.Default) {
            state.value = state.value.copy(displayPb  = true)
            delay(3000)
            savedStateHandle.get<String>(Constants.PARAM_PRODUCT)?.let { product ->
                getProduct(id = product)
            }
        }

    }

    private fun getProduct(id : String){
        useCase.getSpecificProduct.invoke(id).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    state.value = state.value.copy(loadedSuccess  = true)
                    state.value = state.value.copy(displayPb  = false)
                    result.data?.let {
                      it.specificProduct?.let { specificProduct ->
                          currentProduct.value = specificProduct
                          mapProductToMutableVariables()
                      }
                    }
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

    private fun mapProductToMutableVariables(){
        viewModelScope.launch {
            productId.value = currentProduct.value.productId!!
            productTitle.value = currentProduct.value.productTitle!!
            productDescription.value = currentProduct.value.productDescription!!
            productCategory.value = currentProduct.value.productCategory!!
            productQuantity.value = currentProduct.value.productQuantity!!
            productPrice.value = currentProduct.value.productPrice!!
            productPhoto.value = currentProduct.value.productPhoto!!
            productDiscountPrice.value = currentProduct.value.discountPrice!!
            productDiscountNote.value = currentProduct.value.discountNote!!
            productDiscountAvalide.value = currentProduct.value.discountAvailable!!
            Log.e("mappingProductPhoto", currentProduct.value.productPhoto.toString())
        }

    }


     fun uploadProductToFb(product : ProductModel, productPhoto : Uri?){
         open.value = true
        if (validateProduct()){
            useCase.updateProduct.invoke(product, productPhoto).onEach { result ->
                when(result) {
                    is Resource.Success -> {
                        state.value = state.value.copy(successUpdated  = true)
                        state.value = state.value.copy(displayPb  = false)
                        closeDialog()
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

    private fun closeDialog() {
        open.value = false
    }

    fun hideErrorDialog() {
        state.value = state.value.copy(
            errorMsg = null
        )
    }




}