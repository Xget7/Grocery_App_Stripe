package lol.xget.groceryapp.presentation.main.User.Account

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.domain.model.UserModel
import lol.xget.groceryapp.domain.use_case.profile.UserUseCases
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ProfileViewModel @Inject constructor(
    val repo : UserUseCases
) : ViewModel(){

    val state : MutableState<ProfileState> = mutableStateOf(ProfileState())
    val currentUser = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid).id
    var profilePhoto = mutableStateOf("")
    var fullNameValue = mutableStateOf("")
    var stateValue = mutableStateOf("")
    var cityValue = mutableStateOf("")
    var addressValue = mutableStateOf("")
    var phoneValue = mutableStateOf("")
    var country = mutableStateOf("")
    val firebaseAuthCurrentUser = FirebaseAuth.getInstance().currentUser?.uid

    init {
        Log.e("currentUser", currentUser)
        getProfileData(currentUser, "user")
    }


    @ExperimentalCoroutinesApi

    fun updateUserProfile(user : UserModel, profilePhoto: Uri? ){
            repo.updateProfile.invoke(user, profilePhoto).onEach { result ->
                when(result){
                    is Resource.Loading -> {
                        state.value = state.value.copy(loading = true)
                    }
                    is Resource.Success -> {
                        state.value = state.value.copy(successUpdate = result.data!!.successUpdate)
                        state.value = state.value.copy(loading = false)
                    }
                    is Resource.Error -> {
                        state.value = state.value.copy(errorMsg = result.message)
                    }
                }
            }.launchIn(viewModelScope)


    }

    @ExperimentalCoroutinesApi
    fun getProfileData(userid : String, accountType : String){
        repo.getProfileInfo.invoke(accountType).onEach { result ->
            when(result){
                is Resource.Loading -> {
                    state.value = state.value.copy(loading = true)
                }
                is Resource.Success -> {
                    state.value = state.value.copy(userModel = result.data!!.userModel)
                    state.value = state.value.copy(loading = false)
                    fullNameValue.value = state.value.userModel!!.fullName.toString()
                    phoneValue.value = state.value.userModel!!.phone.toString()
                    country.value = state.value.userModel!!.country.toString()
                    stateValue.value = state.value.userModel!!.state.toString()
                    cityValue.value = state.value.userModel!!.city.toString()
                    addressValue.value = state.value.userModel!!.address.toString()

                    state.value.userModel!!.profilePhoto?.let{
                        profilePhoto.value = it
                    }

                }
                is Resource.Error -> {
                    state.value = state.value.copy(errorMsg = result.message)
                    state.value = state.value.copy(loading = false)
                    Log.e("ProfileViewModel", "Error ${result.message}")
                }
            }
        }.launchIn(viewModelScope)
    }

    fun hideErrorDialog() {
        state.value = state.value.copy(
            errorMsg = null
        )
    }


}