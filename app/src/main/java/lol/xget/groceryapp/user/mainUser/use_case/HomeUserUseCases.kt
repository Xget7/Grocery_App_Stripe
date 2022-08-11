package lol.xget.groceryapp.user.mainUser.use_case

data class HomeUserUseCases (
        val getShopsList : GetShopsList,
        val getRatingFromShopUserUseCase: GetRatingsFromShopUseCase,
    )