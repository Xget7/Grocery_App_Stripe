package lol.xget.groceryapp.seller.profileSeller.presentation.config

data class SellerSettingsState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
) {

}
