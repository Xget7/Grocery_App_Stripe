package lol.xget.groceryapp.user.mainUser.domain

import java.sql.Timestamp

data class Review(
    val rating: String? = null,
    val review: String? = null,
    val timestamp: Long? = null,
    val uid: String?=null
)
