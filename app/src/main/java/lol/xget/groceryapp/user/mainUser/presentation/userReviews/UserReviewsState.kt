package lol.xget.groceryapp.user.mainUser.presentation.userReviews

import lol.xget.groceryapp.user.mainUser.domain.User

data class UserReviewsState(
    val isSucess: Boolean? = false,
    val isLoading: Boolean? = false,
    val errorMsg: String? = null,
    val user: User? = null
)
