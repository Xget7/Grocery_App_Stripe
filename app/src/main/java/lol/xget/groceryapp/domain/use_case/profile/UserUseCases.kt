package lol.xget.groceryapp.domain.use_case.profile

import lol.xget.groceryapp.homeUser.use_case.GetShopsList

data class UserUseCases(
    val updateProfile: UpdateProfileUseCase,
    val getProfileInfo: GetProfileUseCase,
    val getShopsList: GetShopsList

)