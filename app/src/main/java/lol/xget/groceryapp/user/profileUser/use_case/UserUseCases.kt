package lol.xget.groceryapp.user.profileUser.use_case

import lol.xget.groceryapp.user.mainUser.use_case.GetShopsList

data class UserUseCases(
    val updateProfile: UpdateProfileUseCase,
    val getProfileInfo: GetProfileUseCase,
    val getShopsList: GetShopsList

)