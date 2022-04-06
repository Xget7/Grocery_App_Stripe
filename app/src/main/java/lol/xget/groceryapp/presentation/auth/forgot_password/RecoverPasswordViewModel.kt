package lol.xget.groceryapp.presentation.auth.forgot_password

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.domain.repository.AuthRepository
import lol.xget.groceryapp.domain.use_case.auth.AuthUseCases
import lol.xget.groceryapp.presentation.auth.login.LoginState
import javax.inject.Inject


@HiltViewModel
class RecoverPasswordViewModel @Inject constructor(
    private val repo: AuthUseCases
) : ViewModel() {

    val state : MutableState<RecoverPasswordState> = mutableStateOf(RecoverPasswordState())
    val emailValue  = mutableStateOf("")


    @ExperimentalCoroutinesApi
    fun recoveryPassword(email: String) {
        if (email.isNotEmpty()) {
                repo.recoverPasswordUseCase.invoke(email).onEach { result ->
                    when (result) {
                        is Resource.Loading -> {
                            state.value = state.value.copy(displayPb = true)
                        }
                        is Resource.Success ->{
                            state.value = state.value.copy(displayPb = false)
                            state.value = state.value.copy(successRecovered = true)
                        }
                        is Resource.Error ->{
                            state.value = state.value.copy(displayPb = false)
                            state.value = state.value.copy(errorMsg = result.message)
                        }
                    }
                }.launchIn(viewModelScope)

        }else{
            state.value = state.value.copy(errorMsg = "Need a valid Email")
        }
    }

    fun hideErrorDialog() {
        state.value = state.value.copy(
            errorMsg = null
        )
    }
}