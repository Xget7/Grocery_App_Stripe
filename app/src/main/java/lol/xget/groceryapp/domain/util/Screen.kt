package lol.xget.groceryapp.domain.util

import lol.xget.groceryapp.common.Constants.LATITUDE
import lol.xget.groceryapp.common.Constants.LONGITUDE
import lol.xget.groceryapp.common.Constants.PARAM_PRODUCT
import lol.xget.groceryapp.common.Constants.PARAM_SHOP
import lol.xget.groceryapp.domain.model.ProductModel


sealed class Screen(val route : String, val productModel: ProductModel? = null){
    object SplashScreen : Screen("splash_screen")
    object LoginScreen : Screen("login_screen")
    object RegisterUserScreen : Screen("register_user_screen")
    object RegisterSellerScreen : Screen("register_seller_screen/{$LONGITUDE}/{$LATITUDE}"){
        fun passLatitudeLatitude(longitude : Double,latitude : Double ) : String{
            return "register_seller_screen/$longitude/$latitude"
        }
    }
    object RecoverPasswordScreen : Screen("recover_password_screen")
    object ProfileUserScreen : Screen("profile_user_screen")
    object SellerAddProductScreen : Screen("seller_add_product_screen")
    object SellerHomeScreen : Screen("seller_home_screen")
    object SellerProfileScreen : Screen("seller_profile_screen")
    object SellerEditProductScreen : Screen("seller_edit_product_screen")
    object UserHomeScreen : Screen("user_home_screen")
    object ShopDetailScreen : Screen("user_shop_detail_screen")
    object ProductDetailScreen : Screen("product_detail_screen/{$PARAM_SHOP}/{$PARAM_PRODUCT}"){
        fun passProduct(shopId : String , productId: String) : String{
            return "product_detail_screen/$shopId/$productId"
        }
    }

}
