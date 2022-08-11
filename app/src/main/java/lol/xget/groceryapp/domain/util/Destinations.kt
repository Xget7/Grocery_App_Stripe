package lol.xget.groceryapp.domain.util

import lol.xget.groceryapp.common.Constants.LATITUDE
import lol.xget.groceryapp.common.Constants.LONGITUDE
import lol.xget.groceryapp.common.Constants.PARAM_ORDER
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
    object SellerMainDestinations : Destinations("seller_main_screen")
    object SellerConfigurationDestinations : Destinations("seller_configuration_screen")
    object SellerOrdersDestinations : Destinations("seller_orders_screen")
    object SellerOrdersDetailDestinations : Destinations("seller_orders_detail_screen/{$PARAM_ORDER}"){
        fun passOrder(orderId: String) : String{
            return "seller_orders_detail_screen/$orderId"
        }
    }
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
    object UserOrderDetailScreen : Destinations("user_order_detail_screen/{$PARAM_SHOP}/{$PARAM_ORDER}"){
        fun passOrder(shopId : String , orderId: String) : String{
            return "user_order_detail_screen/$shopId/$orderId"
        }
    }
    object UserReviewsScreen : Destinations("user_reviews_screen/{$PARAM_SHOP}"){
        fun passShop(shopId : String) : String{
            return "user_reviews_screen/$shopId"
        }
    }
    object ShopReviewsScreen : Destinations("shop_reviews_screen/{$PARAM_SHOP}"){
        fun passShop(shopId : String) : String{
            return "shop_reviews_screen/$shopId"
        }
    }



}
