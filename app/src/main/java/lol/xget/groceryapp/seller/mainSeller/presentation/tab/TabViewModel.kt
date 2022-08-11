package lol.xget.groceryapp.seller.mainSeller.presentation.tab

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.seller.mainSeller.use_case.HomeSellerUseCases
import javax.inject.Inject

@HiltViewModel
class TabViewModel @Inject constructor(
    val repo: HomeSellerUseCases,

    ) : ViewModel() {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val userName = mutableStateOf("")
    init {
        getSellerData()
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getSellerData() {
        repo.getSellerProfile.invoke(user!!.uid, "seller").onEach { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.user?.userName?.let {
                        userName.value =it
                    }

                }
            }
        }.launchIn(viewModelScope)
    }
}