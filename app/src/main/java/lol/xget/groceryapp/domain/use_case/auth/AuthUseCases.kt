package lol.xget.groceryapp.domain.use_case.auth

import lol.xget.groceryapp.domain.use_case.auth.login_use_case.LoginUseCase
import lol.xget.groceryapp.domain.use_case.auth.recover_pass_use_case.RecoverPasswordUseCase
import lol.xget.groceryapp.domain.use_case.auth.register_use_case.RegisterUseCase

data class AuthUseCases(
    val loginCase: LoginUseCase,
    val registerCase: RegisterUseCase,
    val recoverPasswordUseCase: RecoverPasswordUseCase
)
