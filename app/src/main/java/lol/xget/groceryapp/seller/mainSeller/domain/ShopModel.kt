package lol.xget.groceryapp.seller.mainSeller.domain

data class ShopModel(
    val uid : String? = null,
    val shopOpen :Boolean? = null,
    val shopBanner :String? = null,
    val shopAdBanner :String? = null,
    val deliveryFee : String? = null,
    val shopName: String? = null,
    val phone : String? = null,
    val gmail : String? = null,
    val state: String? = null,
    val city: String? = null,
    val country : String? = null,
    val address: String? = null,
    val profilePhoto : String? = null,
    val longitude : Float? = null,
    val latitude : Float? = null,
) {
    companion object{
        fun from(map : Map<String, Any>) = object {
            val uid by map
            val online by map
            val deliveryFee by map
            val shopName by map
            val phone by map
            val state by map
            val city by map
            val address by map
            val country by map
            val photo by map


            val shop = lol.xget.groceryapp.seller.mainSeller.domain.ShopModel(
                uid as String?,
                online as Boolean?,
                deliveryFee as String?,
                shopName as String?,
                phone as String?,
                state as String?,
                city as String?,
                address as String?,
                country as String?,
                photo as String?

            )
        }.shop
    }
}