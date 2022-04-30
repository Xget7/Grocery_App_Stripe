package lol.xget.groceryapp.user.mainUser.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import lol.xget.groceryapp.common.Constants.swapList
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.seller.mainSeller.domain.ShopModel
import lol.xget.groceryapp.user.mainUser.domain.User
import lol.xget.groceryapp.user.profileUser.use_case.UserUseCases
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class UserHomeScreenViewModel @Inject constructor(
    val repo: UserUseCases
) : ViewModel() {

    val state: MutableState<UserHomeScreenState> = mutableStateOf(UserHomeScreenState())
    val userData = mutableStateOf(User())
    val shopList = mutableStateListOf<ShopModel>()

    var shopListFilteredByLocation = mutableStateListOf<ShopModel>()
    val shopListFilteredByRating = mutableStateListOf<ShopModel>()

    private val _originalShopList = mutableStateListOf<ShopModel>()

    val currentItem = mutableStateOf(ShopModel())

    val query = mutableStateOf("")


    init {
        getUser()
        getShopsList()
    }



    //ADD FILTERSSSS
    private fun getShopsList() {
        repo.getShopsList.invoke().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state.value = state.value.copy(loading = true)
                }
                is Resource.Success -> {

                    state.value = state.value.copy(successLoad = result.data!!.successLoad)
                    state.value = state.value.copy(shopModel = result.data.shopModel)

                    //getting shops data into ShopList
                    state.value.shopModel?.let { list ->
                        shopList.swapList(list)
                        _originalShopList.swapList(list)
                        shopListFilteredByLocation.swapList(filterListByLocation(list,userData.value))
                        state.value = state.value.copy(loading = false)
                    }
                }
                is Resource.Error -> {
                    state.value = state.value.copy(loading = false)
                    state.value = state.value.copy(errorMsg = result.message)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun filterListByLocation(shopList: List<ShopModel> , user: User) : List<ShopModel>{
        if (user.latitude == 0f && user.longitude == 0f){
            return emptyList()
        }

        val filteredList = mutableStateListOf<ShopModel>()

        for (shop in shopList){
            val distanceBetweenUserAndShop = distance(
                user.latitude!!.toDouble(),
                user.longitude!!.toDouble(),
                shop.latitude!!.toDouble(),
                shop.longitude!!.toDouble()
            )
            Log.e("DistanceBEtweenShop = ", distanceBetweenUserAndShop.toString())
            if (distanceBetweenUserAndShop < 15){
                filteredList.add(shop)
            }
        }

        return filteredList
    }

    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    fun bannersFromShopListFilteredByRating() : List<String> {
        val mutableList = mutableListOf<String>()
            for (banner in _originalShopList){
                banner.shopAdBanner?.let {
                    mutableList.add(it)
                }
            }
        return mutableList
    }


    private fun getUser() {
        repo.getProfileInfo.invoke("user").onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state.value = state.value.copy(loading = true)
                }
                is Resource.Success -> {
                    state.value = state.value.copy(successLoad = result.data!!.successUpdate)
                    state.value = state.value.copy(user = result.data.user)
                    //getting user firebase data into UserData
                    state.value.user?.let {
                        userData.value = it
                        Log.e("userData", userData.value.toString())
                    }

                }
                is Resource.Error -> {
                    state.value = state.value.copy(loading = false)
                    state.value = state.value.copy(errorMsg = result.message)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun newSearch() {
        viewModelScope.launch {
            query.value.lowercase(Locale.ROOT)
            if (query.value.isNotEmpty()) {
                shopList.swapList(_originalShopList)
                val result = shopList.filter {
                    it.shopName!!.lowercase(Locale.ROOT).contains(query.value, true)
                }
                state.value = state.value.copy(searching = true)

                if (result.isNotEmpty()) {
                    shopList.swapList(result)

                    state.value = state.value.copy(searchError = false)
                } else if (query.value == "" || query.value == " "){
                    shopList.swapList(_originalShopList)
                    state.value = state.value.copy(searchError = false)

                }else{
                    state.value = state.value.copy(searching = false)
                    state.value = state.value.copy(searchError = true)
                }
            } else if (query.value == ""){
                state.value = state.value.copy(searchError = false)
                shopList.swapList(_originalShopList)
            }
        }
    }

    private var lastScrollIndex = 30
    private val _scrollUp = MutableLiveData(false)
    val scrollUp: LiveData<Boolean> = _scrollUp

    fun updateScrollPosition(newScrollIndex: Int) {
        TODO("Last scroll fix")

        if (newScrollIndex == lastScrollIndex) return

        Log.e("ScrollIndex" , newScrollIndex.toString())
        _scrollUp.value = newScrollIndex > lastScrollIndex
        lastScrollIndex = newScrollIndex
    }


    fun onQueryChanged(query: String) {
        viewModelScope.launch {
            state.value = state.value.copy(searching = true)
            this@UserHomeScreenViewModel.query.value = query
            Log.e("onQueryChanged", " = $query")
        }
    }


    fun hideErrorDialog() {
        state.value = state.value.copy(
            errorMsg = null
        )
    }


}