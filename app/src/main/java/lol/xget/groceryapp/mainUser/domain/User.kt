package lol.xget.groceryapp.mainUser.domain

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var uid : String? = null,
    var profilePhoto: String? = null,
    var shopPhoto: String? = null,
    val country: String? = null,
    val userName: String? = null,
    val phone: String? = null,
    val latitude : Float? = null,
    val longitude :Float? = null,
    val state: String? = null,
    val city: String? = null,
    val address: String? = null,
    val accountType: String? = null,
    var email: String? = null,
    val online :Boolean? = null,
    val shopOpen : Boolean? = null,
    val deliveryFee : String? = null,
    val shopName: String? = null,
){

    companion object {


        fun fromMap(map: Map<String, Any?>) = object {
            val uid by map
            val profilePhoto by map
            val shopPhoto by map
            val country by map
            val userName by map
            val phone by map
            val latitude by map
            val longitude by map
            val state by map
            val city by map
            val address by map
            val accountType by map
            val email by map
            val online by map
            val shopOpen by map
            val deliveryFee by map
            val shopName by map

            val data = User(
                uid as String?,
                profilePhoto as String?,
                shopPhoto as String?,
                country as String?,
                userName as String?,
                phone as String?,
                latitude as Float?,
                longitude as Float?,
                state as String?,
                city as String?,
                address as String?,
                accountType as String?,
                email as String?,
                online as Boolean?,
                shopOpen as Boolean?,
                deliveryFee as String?,
                shopName as String?,
            )


        }.data


    }

    fun toMap() : HashMap<String, Any?> = hashMapOf(
        "uid" to uid,
        "profilePhoto" to profilePhoto,
        "shopPhoto" to shopPhoto,
        "accountType" to accountType,
        "userName" to userName,
        "email" to email,
        "phone" to phone,
        "latitude" to latitude,
        "longitude" to longitude,
        "state" to state,
        "city" to city,
        "address" to address,
        "country" to country,
        "online" to online,
        "shopOpen" to shopOpen,
        "deliveryFee" to deliveryFee,
        "shopName" to shopName,
    )
}