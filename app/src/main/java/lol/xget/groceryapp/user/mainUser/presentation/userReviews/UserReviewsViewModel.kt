package lol.xget.groceryapp.user.mainUser.presentation.userReviews

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import lol.xget.groceryapp.common.Constants
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.data.repository.UserRepoImpl
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.user.mainUser.domain.Review
import lol.xget.groceryapp.user.profileUser.use_case.UserUseCases
import lol.xget.groceryapp.user.shoppingCar.use_case.GetShopData
import javax.inject.Inject
@ExperimentalCoroutinesApi
@HiltViewModel
class UserReviewsViewModel @Inject constructor(
    val repo : UserRepoImpl,
    val useCases: UserUseCases,
    savedStateHandle: SavedStateHandle

): ViewModel() {
    val currentShopId = mutableStateOf("")
    val state = mutableStateOf(UserReviewsState())
    val currentUser = FirebaseAuth.getInstance().currentUser
    val currentUserId = currentUser?.uid
    val shopImage = mutableStateOf("")
    val shopName = mutableStateOf("")

    init {
        savedStateHandle.get<String>(Constants.PARAM_SHOP)?.let { shopId ->
            currentShopId.value = shopId
        }
        Log.e("CURRENSHOPID", currentShopId.value)
        getShop(currentShopId.value)
    }


    private fun getShop(shopId: String){

            useCases.getShop.invoke(shopId).onEach { result ->
                when(result){
                    is Resource.Loading -> {
                        state.value = state.value.copy(isLoading = true)

                    }
                    is Resource.Success -> {
                        state.value = state.value.copy(isLoading = false)
                        result.data?.shop?.shopBanner?.let {
                            shopImage.value = it
                        }
                        result.data?.shop?.shopName?.let {
                            shopName.value = it
                        }
                    }
                    is Resource.Error ->{
                        state.value = state.value.copy(errorMsg = result.message, isLoading = false)

                    }
                }
            }.launchIn(viewModelScope)
    }


    fun uploadReview(review: Review){
        viewModelScope.launch {
            if (!review.review.isNullOrEmpty()){
                repo.placeShopRating(currentShopId.value,review).addOnSuccessListener {
                    state.value = state.value.copy(isSucess = true)
                }.addOnFailureListener {
                    state.value = state.value.copy(errorMsg =it.localizedMessage ?: "Something went wrong")
                }
            }else{
                state.value = state.value.copy(errorMsg = "Review is empty, write something")
            }
        }

    }

    fun finished(navController: NavController) {
        viewModelScope.launch {
            delay(1000)
            navController.navigate(Destinations.UserHomeDestinations.route)
        }
    }


}