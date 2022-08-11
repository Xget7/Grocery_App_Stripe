package lol.xget.groceryapp.seller.mainSeller.presentation.shopReviews

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import lol.xget.groceryapp.common.Constants
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.data.repository.SellerRepoImpl
import lol.xget.groceryapp.seller.mainSeller.presentation.SellerOrdersScreen.SellerOrderState
import lol.xget.groceryapp.user.mainUser.domain.Review
import lol.xget.groceryapp.user.mainUser.domain.User
import lol.xget.groceryapp.user.mainUser.presentation.userReviews.UserReviewsState
import lol.xget.groceryapp.user.mainUser.use_case.HomeUserUseCases
import lol.xget.groceryapp.user.profileUser.use_case.UserUseCases
import javax.inject.Inject

@HiltViewModel
class ShopReviewsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val repoShop: UserUseCases,
    val repoRating: HomeUserUseCases,
) : ViewModel() {
    private val currentShopId = mutableStateOf("")
    val state: MutableState<UserReviewsState> = mutableStateOf(UserReviewsState())
    val shopReviewsList: MutableList<Review> = mutableListOf()

    init {
        savedStateHandle.get<String>(Constants.PARAM_SHOP)?.let { shopId ->
            currentShopId.value = shopId
        }
        getShopData()
    }

    val shopImage = mutableStateOf("")
    val shopName = mutableStateOf("")

    fun getShopData() {
        repoShop.getShop.invoke(currentShopId.value).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state.value = state.value.copy(isLoading = true)
                }
                is Resource.Success -> {

                    result.data?.shop?.shopBanner?.let {
                        shopImage.value = it
                    }
                    result.data?.shop?.shopName?.let {
                        shopName.value = it
                    }
                    getShopReviews()
                    state.value = state.value.copy(isLoading = false)

                }
                is Resource.Error -> {
                    state.value = state.value.copy(errorMsg = result.message, isLoading = false)

                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getShopReviews() {
        repoRating.getRatingFromShopUserUseCase.invoke(currentShopId.value).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state.value = state.value.copy(isLoading = true)
                }
                is Resource.Success -> {
                    state.value = state.value.copy(isLoading = false)

                    result.data?.reviewsList?.let {
                        shopReviewsList.addAll(it)
                    }
                    state.value = state.value.copy(isLoading = false)

                }
                is Resource.Error -> {
                    state.value = state.value.copy(errorMsg = result.message, isLoading = false)

                }
            }
        }.launchIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getUserData(reviewerUid: String)  = flow {
        val user = mutableStateOf(User())
        Log.d("USER", "started")
        repoShop.getUSerDataFromReviews.invoke(reviewerUid, "user").onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state.value = state.value.copy(isLoading = true)
                    Log.d("USER", "loading")
                }
                is Resource.Success -> {
                    Log.d("USER", "success")
                    Log.d("USER", result.data!!.user?.email.toString())
                    emit(result.data!!.user)
                    state.value = state.value.copy(isLoading = false, isSucess = true, user = result.data.user)

                }
                is Resource.Error -> {
                    Log.d("USER", "error")
                    state.value = state.value.copy(errorMsg = result.message, isLoading = false)

                }
            }
        }.collect()
    }
}