package lol.xget.groceryapp.homeUser.domain

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var uid : String? = null,
    var profilePhoto: String? = null,
    val country: String? = null,
    val fullName: UserName? = null,
    val phone: String? = null,
    val latitude : Float? = null,
    val longitude :Float? = null,
    val state: String? = null,
    val city: String? = null,
    val address: String? = null,
    val accountType: String? = null,
    val email: UserEmail? = null,
    val online :Boolean? = null,
    val shopOpen : Boolean? = null,
    val deliveryFee : String? = null,
    val shopName: String? = null,
)