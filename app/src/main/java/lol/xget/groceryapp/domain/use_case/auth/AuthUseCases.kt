package lol.xget.groceryapp.domain.use_case.auth

import lol.xget.groceryapp.auth.login.use_case.LoginUseCase
import lol.xget.groceryapp.auth.recoverPassword.use_case.RecoverPasswordUseCase
import lol.xget.groceryapp.auth.register.use_case.RegisterUseCase


data class AuthUseCases(
    val loginCase: LoginUseCase,
    val registerCase: RegisterUseCase,
    val recoverPasswordUseCase: RecoverPasswordUseCase
)
