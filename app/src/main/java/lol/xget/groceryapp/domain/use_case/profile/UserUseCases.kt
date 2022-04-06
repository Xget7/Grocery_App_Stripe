package lol.xget.groceryapp.domain.use_case.profile

import lol.xget.groceryapp.domain.use_case.homeUser.GetShopsList

data class UserUseCases(
    val updateProfile: UpdateProfileUseCase,
    val getProfileInfo: GetProfileUseCase,
    val getShopsList: GetShopsList

)