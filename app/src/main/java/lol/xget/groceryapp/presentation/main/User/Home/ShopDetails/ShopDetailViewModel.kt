package lol.xget.groceryapp.presentation.main.User.Home.ShopDetails

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import lol.xget.groceryapp.common.Constants
import lol.xget.groceryapp.common.Constants.swapList
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.data.localdb.CartItems
import lol.xget.groceryapp.data.localdb.CartItemsDao
import lol.xget.groceryapp.domain.model.ProductModel
import lol.xget.groceryapp.domain.model.ShopModel
import lol.xget.groceryapp.domain.use_case.homeSeller.HomeSellerUseCases
import lol.xget.groceryapp.presentation.main.Seller.EditProducts.EditProductState
import lol.xget.groceryapp.presentation.main.categories.Categories
import lol.xget.groceryapp.presentation.main.categories.getFoodCategory
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class ShopDetailViewModel @Inject constructor(
    private val useCase: HomeSellerUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    val state: MutableState<ShopDetailScreenState> = mutableStateOf(ShopDetailScreenState())

    val currentShop = mutableStateOf(ShopModel())

    val query = mutableStateOf("")

    val productList = mutableStateListOf<ProductModel>()

    val currentProduct = mutableStateOf(ProductModel())

    val finalCost = mutableStateOf(0)

    var productListOriginal = mutableListOf<ProductModel>()

    //shopMutables
    val shopOpen = mutableStateOf(false)
    val deliveryFee = mutableStateOf("")
    val shopIdSaved = mutableStateOf("")
    val shopName = mutableStateOf("")
    val phone = mutableStateOf("")
    val gmail = mutableStateOf("")
    val address = mutableStateOf("")
    val profilePhoto = mutableStateOf("")

    //TODO(upload custom background image)
    val backgGroundImage = mutableStateOf("https://thumbs.dreamstime.com/b/homemade-black-burger-cheese-cheeseburger-black-bun-dark-wooden-background-close-up-shot-delicious-meat-92838092.jpg")


    val longitude = mutableStateOf(0.0)
    val latitude = mutableStateOf(0.0)
    val gmmIntentUri = mutableStateOf(Uri.EMPTY)

    val selectedCategory: MutableState<Categories?> = mutableStateOf(null)


    private var lastScrollIndex = 0

    private val _scrollUp = MutableLiveData(false)
    val scrollUp: LiveData<Boolean>
        get() = _scrollUp

    fun updateScrollPosition(newScrollIndex: Int) {
        if (newScrollIndex == lastScrollIndex) return

        _scrollUp.value = newScrollIndex > lastScrollIndex
        lastScrollIndex = newScrollIndex
    }

    init {
        savedStateHandle.get<String>(Constants.PARAM_SHOP)?.let { shopId ->
            shopIdSaved.value = shopId
        }
        getShop(shopIdSaved.value)
        getProductsFromShop(shopIdSaved.value)
        newSearch()
    }


    //execute only once
    private fun getShop(shopId: String) {

        useCase.getSpecificShop.invoke(shopId).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state.value = state.value.copy(loading = true)
                }
                is Resource.Success -> {
                    state.value = state.value.copy(success = true)
                    state.value =
                        state.value.copy(specificShopModel = result.data?.specificShopModel)
                    parseResults(state.value.specificShopModel)
                    state.value = state.value.copy(loading = false)
                }
                is Resource.Error -> {
                    state.value = state.value.copy(errorMsg = result.message)
                }
            }
        }.launchIn(viewModelScope)

    }

    private fun parseResults(shopFb: ShopModel?) {
        shopOpen.value = shopFb?.shopOpen!!
        deliveryFee.value = shopFb.deliveryFee!!
        shopName.value = shopFb.shopName!!
        phone.value = shopFb.phone!!
        shopFb.gmail?.let {
            gmail.value = it
        }
        address.value = shopFb.address!!
        shopFb.profilePhoto?.let {
            profilePhoto.value = it
        }
        latitude.value = shopFb.latitude!!.toDouble()
        longitude.value = shopFb.longitude!!.toDouble()
        gmmIntentUri.value =
            Uri.parse("http://maps.google.com/maps?q=loc:" + latitude.value + "," + longitude.value)
    }

    private fun getProductsFromShop(shopId: String) {
        useCase.getShopProducts.invoke(shopId).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state.value = state.value.copy(loading = true)
                }
                is Resource.Success -> {
                    state.value = state.value.copy(success = true)
                    state.value = state.value.copy(productsList = result.data?.productModel)
                    result.data?.let { it ->
                        it.productModel?.let { list ->
                            productList.swapList(list)
                            productListOriginal = list.toMutableList()
                        }
                    }


                }
                is Resource.Error -> {
                    state.value = state.value.copy(errorMsg = result.message)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun newSearch() {
        viewModelScope.launch {
            if (query.value.isNotEmpty()) {

                productList.swapList(productListOriginal)


                val result = productList.filter {
                    it.productCategory!!.contains(query.value, true)
                }


                if (result.isNotEmpty()) {
                    productList.swapList(result)

                } else {
                    state.value = state.value.copy(errorMsg = "Don't found ")
                }
            } else {
                productList.swapList(productListOriginal)
            }
        }
    }

    private fun onQueryChanged(query: String) {
        this.query.value = query
    }

    fun onSelectedCategoryChanged(category: String) {
        val newCategory = getFoodCategory(category)
        selectedCategory.value = newCategory
        onQueryChanged(category)
    }

    fun hideErrorDialog() {
        state.value = state.value.copy(
            errorMsg = null
        )
    }

}