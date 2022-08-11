package lol.xget.groceryapp.user.mainUser.presentation.ShopDetails

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import lol.xget.groceryapp.common.Constants
import lol.xget.groceryapp.common.Constants.swapList
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.seller.mainSeller.domain.ShopModel
import lol.xget.groceryapp.seller.mainSeller.use_case.HomeSellerUseCases
import lol.xget.groceryapp.user.mainUser.domain.Review
import lol.xget.groceryapp.user.mainUser.presentation.components.categories.Categories
import lol.xget.groceryapp.user.mainUser.presentation.components.categories.getFoodCategory
import lol.xget.groceryapp.user.mainUser.use_case.HomeUserUseCases
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class ShopDetailViewModel @Inject constructor(
    private val sellerCase: HomeSellerUseCases,
    private val userCase: HomeUserUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val scope = CoroutineScope(Dispatchers.IO)

    val state: MutableState<ShopDetailScreenState> = mutableStateOf(ShopDetailScreenState())

    val currentShop = mutableStateOf(ShopModel())

    val query = mutableStateOf("")

    val productList = mutableStateListOf<lol.xget.groceryapp.seller.mainSeller.domain.ProductModel>()

    val currentProduct = mutableStateOf(lol.xget.groceryapp.seller.mainSeller.domain.ProductModel())

    val averageShopRating = mutableStateOf(0F)

    var productListOriginal = mutableListOf<lol.xget.groceryapp.seller.mainSeller.domain.ProductModel>()

    //shopMutables
    val shopOpen = mutableStateOf(false)
    val deliveryFee = mutableStateOf("")
    val shopIdSaved = mutableStateOf("")
    val shopName = mutableStateOf("")
    val phone = mutableStateOf("")
    val gmail = mutableStateOf("")
    val address = mutableStateOf("")
    val profilePhoto = mutableStateOf("")

    val shopBannerImage = mutableStateOf("")


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

    private fun getShopRatings(shopId: String){
        userCase.getRatingFromShopUserUseCase.invoke(shopId).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state.value = state.value.copy(loading = true)
                }
                is Resource.Success -> {
                    state.value = state.value.copy(success = true,loading = false)
                    result.data?.reviewsList?.let { calculateAverageRating(it) }

                }
                is Resource.Error -> {
                    state.value = state.value.copy(errorMsg = result.message)
                }
            }
        }.launchIn(viewModelScope)
    }
    private fun calculateAverageRating(revs: List<Review>){
        viewModelScope.launch {
            try {
                val average = mutableStateOf(0F)
                val totalRating = mutableStateOf(0F)
                val reviewsSize = revs.size
                for (review in revs){
                    val rating = review.rating?.toFloat()
                    if (rating != null) {
                        totalRating.value += rating
                    }
                }
                average.value = totalRating.value / reviewsSize
                averageShopRating.value = average.value
                cancel()
            }catch (e: Exception){
                state.value = state.value.copy(errorMsg = e.localizedMessage)
            }


        }
    }

    //execute only once
    private fun getShop(shopId: String) {
            sellerCase.getSpecificShop.invoke(shopId).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        state.value = state.value.copy(loading = true)
                    }
                    is Resource.Success -> {
                        state.value = state.value.copy(specificShopModel = result.data?.specificShopModel,success = true, loading = false)
                        Log.e("PArseResultsError", result.data?.specificShopModel.toString())
                        parseResults(result.data?.specificShopModel)
                    }
                    is Resource.Error -> {
                        state.value = state.value.copy(errorMsg = result.message)
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun parseResults(shopFb: ShopModel?) {
        viewModelScope.launch {
            shopOpen.value = shopFb?.shopOpen!!
            deliveryFee.value = shopFb.deliveryFee!!
            shopName.value = shopFb.shopName!!
            phone.value = shopFb.phone!!
            shopFb.shopBanner?.let {
                shopBannerImage.value = it
            }
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
    }

    private fun getProductsFromShop(shopId: String) {
        sellerCase.getShopProducts.invoke(shopId).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state.value = state.value.copy(loading = true)
                }
                is Resource.Success -> {
                    state.value = state.value.copy(productsList = result.data?.productModel)
                    result.data?.let { it ->
                        it.productModel?.let { list ->
                            productList.swapList(list)
                            productListOriginal = list.toMutableList()
                        }
                    }
                    getShopRatings(shopId)
                    state.value = state.value.copy(success = true, loading = false)


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