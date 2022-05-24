package lol.xget.groceryapp.domain.util

import lol.xget.groceryapp.common.Constants.LATITUDE
import lol.xget.groceryapp.common.Constants.LONGITUDE
import lol.xget.groceryapp.common.Constants.PARAM_PRODUCT
import lol.xget.groceryapp.common.Constants.PARAM_SHOP


sealed class Destinations(val route : String, val productModel: lol.xget.groceryapp.seller.mainSeller.domain.ProductModel? = null){
    object SplashDestinations : Destinations("splash_screen")
    object LoginDestinations : Destinations("login_screen")
    object RegisterUserDestinations : Destinations("register_user_screen")
    object RegisterSellerDestinations : Destinations("register_seller_screen/{$LONGITUDE}/{$LATITUDE}"){
        fun passLatitudeLatitude(longitude : Double,latitude : Double ) : String{
            return "register_seller_screen/$longitude/$latitude"
        }
    }
    object RecoverPasswordDestinations : Destinations("recover_password_screen")
    object ProfileUserDestinations : Destinations("profile_user_screen")
    object SellerAddProductDestinations : Destinations("seller_add_product_screen")
    object SellerHomeDestinations : Destinations("seller_home_screen")
    object SellerAccountDestinations : Destinations("seller_profile_screen")
    object SellerEditProfileDestinations : Destinations("seller_edit_profile_screen")
    object SellerEditProductDestinations : Destinations("seller_edit_product_screen")
    object SellerBannerDestination : Destinations("seller_banner_screen")
    object UserMainDestination : Destinations("user_main_screen")
    object UserHomeDestinations : Destinations("user_home_screen")
    object ShopDetailDestinations : Destinations("user_shop_detail_screen")
    object ProductDetailDestinations : Destinations("product_detail_screen/{$PARAM_SHOP}/{$PARAM_PRODUCT}"){
        fun passProduct(shopId : String , productId: String) : String{
            return "product_detail_screen/$shopId/$productId"
        }
    }
    object ShoppingCar : Destinations("shopping_car_screen")
    object UserOrdersScreen : Destinations("user_orders_screen")


}
