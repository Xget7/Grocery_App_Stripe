package lol.xget.groceryapp.domain.use_case.auth

import lol.xget.groceryapp.login.use_case.LoginUseCase
import lol.xget.groceryapp.recoverPassword.use_case.RecoverPasswordUseCase
import lol.xget.groceryapp.register.use_case.RegisterUseCase

data class AuthUseCases(
    val loginCase: LoginUseCase,
    val registerCase: RegisterUseCase,
    val recoverPasswordUseCase: RecoverPasswordUseCase
)
