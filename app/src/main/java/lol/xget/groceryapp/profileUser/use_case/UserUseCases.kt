package lol.xget.groceryapp.profileUser.use_case

import lol.xget.groceryapp.mainUser.use_case.GetShopsList

data class UserUseCases(
    val updateProfile: UpdateProfileUseCase,
    val getProfileInfo: GetProfileUseCase,
    val getShopsList: GetShopsList

)