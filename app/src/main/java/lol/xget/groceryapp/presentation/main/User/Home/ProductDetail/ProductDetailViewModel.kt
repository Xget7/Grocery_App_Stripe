package lol.xget.groceryapp.presentation.main.User.Home.ProductDetail

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import lol.xget.groceryapp.common.Constants
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.data.localdb.CartItems
import lol.xget.groceryapp.data.repository.CartItemsRepoImpl
import lol.xget.groceryapp.domain.model.ProductModel
import lol.xget.groceryapp.domain.use_case.products.GetSpecificProductFromUser
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val dbCartRepo: CartItemsRepoImpl,
    private val getProductUseCase: GetSpecificProductFromUser,
    savedStateHandle: SavedStateHandle
) : ViewModel(){

    val state: MutableState<ProductDetailState> = mutableStateOf(ProductDetailState())


    val productDiscountAvailable = mutableStateOf(false)
    val productDiscountNote = mutableStateOf("")

    val productTitle = mutableStateOf("")
    val productQuantity = mutableStateOf("")
    val productDescription = mutableStateOf("")
    val productId = mutableStateOf("")
    val productPrice = mutableStateOf("")
    val productPhoto = mutableStateOf("")

    val currentProductAmount = MutableStateFlow(1)
    val productFinalPrice = MutableStateFlow(0f)

    init {
        val currentShopId = savedStateHandle.get<String>(Constants.PARAM_SHOP)
        val currentProductId = savedStateHandle.get<String>(Constants.PARAM_PRODUCT)
        if (currentProductId != null && currentShopId != null){
            getProduct(currentShopId, currentProductId)

        }
    }
    private fun getProduct(shopId: String,productId: String){
        getProductUseCase.invoke(shopId,productId).onEach { result ->
            when(result){
                is Resource.Success -> {
                    state.value = state.value.copy(success = true, loading = false)
                    state.value = state.value.copy( currentProduct = result.data?.currentProduct)
                    result.data?.currentProduct?.let { parseResults(it) }
                }
                is Resource.Loading -> {
                    state.value = state.value.copy(loading = true)
                }
                is Resource.Error -> {
                    state.value = state.value.copy(errorMsg = result.message)

                }

            }
        }.launchIn(viewModelScope)
    }


    fun addItem(){
        viewModelScope.launch {
            currentProductAmount.value = currentProductAmount.value + 1
            productFinalPrice.value = currentProductAmount.value * productPrice.value.toFloat()
            Log.e("producFinalprice", productFinalPrice.value.toString())
        }
    }

    fun deleteItem(){
        viewModelScope.launch {
            if (currentProductAmount.value >= 1){
                currentProductAmount.value = currentProductAmount.value - 1
                productFinalPrice.value = currentProductAmount.value * productPrice.value.toFloat()
                Log.e("producFinalprice", productFinalPrice.value.toString())
            }
        }
    }



    private fun parseResults(product : ProductModel){
        productPrice.value = product.productPrice!!
        productFinalPrice.value = productPrice.value.toInt().toFloat()
        Log.e("producFinalpriceTRolleadoParsed", productFinalPrice.value.toString())
        productId.value = product.productId!!
        productTitle.value = product.productTitle ?: "Error getting product title"
        productDescription.value = product.productDescription ?: "Error fetching product description"
        productQuantity.value = product.productQuantity ?: "Error getting quantity"
        productPhoto.value = product.productPhoto!!
        productDiscountAvailable.value = product.discountAvailable!!
        productDiscountNote.value = product.discountNote ?: "Error fetching discount note"
    }

    fun hideErrorDialog() {
        state.value = state.value.copy(
            errorMsg = null
        )
    }

    fun addToCart(cartItems: CartItems){
        viewModelScope.launch(Dispatchers.IO) {
            dbCartRepo.addItem(cartItems)


        }
    }



}