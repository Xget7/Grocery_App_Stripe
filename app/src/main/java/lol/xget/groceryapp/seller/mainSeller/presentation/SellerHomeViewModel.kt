package lol.xget.groceryapp.seller.mainSeller.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import lol.xget.groceryapp.common.Constants.swapList
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.seller.mainSeller.use_case.HomeSellerUseCases
import lol.xget.groceryapp.user.mainUser.domain.User
import javax.inject.Inject

@HiltViewModel
class SellerHomeViewModel @Inject constructor(
    val repo: HomeSellerUseCases,
) : ViewModel() {


    private val db = Firebase.firestore

    val user = FirebaseAuth.getInstance().currentUser
    val state: MutableState<SellerHomeState> = mutableStateOf(SellerHomeState())
    val currentUser = db.collection("sellers").document(user!!.uid).get()

    val query = mutableStateOf("")

    val shopName = mutableStateOf("")
    val userName = mutableStateOf("")
    val userGmail = mutableStateOf("")
    val shopPhoto = mutableStateOf("")
    val currentItem = mutableStateOf(lol.xget.groceryapp.seller.mainSeller.domain.ProductModel())
    val myList = mutableStateListOf<lol.xget.groceryapp.seller.mainSeller.domain.ProductModel>()
    var productListOriginal = mutableListOf<lol.xget.groceryapp.seller.mainSeller.domain.ProductModel>()


    var open = MutableLiveData<Boolean>()
    var productClicked = mutableStateOf(false)


    init {
        getSellerData()
        getProductsList()
    }

    private fun getProductsList() {
        repo.getShopProducts.invoke(user!!.uid).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state.value = state.value.copy(loading = true)
                }
                is Resource.Success -> {
                    state.value = state.value.copy(successLoad = true, loading = false)
                    result.data?.let { it ->
                        it.productModel?.let { list ->
                            myList.swapList(list)
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getSellerData() {
        repo.getSellerProfile.invoke(user!!.uid, "seller").onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state.value = state.value.copy(loading = true)
                }
                is Resource.Success -> {
                    state.value = state.value.copy(successLoad = true, loading = false)
                    showData(result.data!!.user!!)
                }
                is Resource.Error -> {
                    state.value = state.value.copy(errorMsg = result.message)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun deleteProduct(productId: String) {
        repo.deleteProducts.invoke(productId, user!!.uid).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state.value = state.value.copy(loading = true)
                }
                is Resource.Success -> {
                    state.value = state.value.copy(successDeleted = true, loading = false)
                    myList.remove(currentItem.value)
                }
                is Resource.Error -> {
                    state.value = state.value.copy(errorMsg = result.message)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun showData(user: User?) {
        viewModelScope.launch {
            if (user != null) {
                shopName.value = user.shopName!!
                userName.value = user.userName!!
                userGmail.value = FirebaseAuth.getInstance().currentUser!!.email!!
                user.profilePhoto?.let {
                    shopPhoto.value = it
                }
            } else {
                state.value = state.value.copy(errorMsg = "User is null")
            }
        }

    }


    fun newSearch(query: String) {
        viewModelScope.launch {
            if (query.isNotEmpty()) {
                val result = myList.filter { it.productTitle!!.contains(query, true) }
                if (result.isNotEmpty()) {
                    myList.swapList(result)
                    Log.e("mylistswapResult", myList.toString())

                } else {
                    state.value = state.value.copy(errorMsg = "Don't found ")
                }
            } else {
                myList.swapList(productListOriginal)

            }
        }
    }


    fun onQueryChanged(query: String) {
        this.query.value = query
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