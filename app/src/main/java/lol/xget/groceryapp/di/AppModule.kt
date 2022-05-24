package lol.xget.groceryapp.di

import android.content.Context
import androidx.room.Room
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import lol.xget.groceryapp.auth.login.use_case.LoginUseCase
import lol.xget.groceryapp.auth.recoverPassword.use_case.RecoverPasswordUseCase
import lol.xget.groceryapp.auth.register.use_case.RegisterUseCase
import lol.xget.groceryapp.seller.mainSeller.repository.SellerRepository
import lol.xget.groceryapp.data.localdb.CartItemsDao
import lol.xget.groceryapp.data.localdb.CartItemsDatabase
import lol.xget.groceryapp.data.repository.AuthRepoImpl
import lol.xget.groceryapp.data.repository.UserRepoImpl
import lol.xget.groceryapp.data.repository.SellerRepoImpl
import lol.xget.groceryapp.domain.repository.AuthRepository

import lol.xget.groceryapp.domain.use_case.products.AddProductUseCase
import lol.xget.groceryapp.domain.use_case.products.GetSpecificProductFromSeller
import lol.xget.groceryapp.domain.use_case.products.UpdateProductUseCase
import lol.xget.groceryapp.domain.use_case.auth.AuthUseCases
import lol.xget.groceryapp.seller.mainSeller.use_case.*
import lol.xget.groceryapp.user.mainUser.repository.UserRepository
import lol.xget.groceryapp.user.mainUser.use_case.GetShopsList
import lol.xget.groceryapp.user.mainUser.use_case.GetSpecificShopUseCase
import lol.xget.groceryapp.user.profileUser.use_case.GetOrdersUseCase
import lol.xget.groceryapp.user.profileUser.use_case.GetProfileUseCase
import lol.xget.groceryapp.user.profileUser.use_case.UpdateProfileUseCase
import lol.xget.groceryapp.user.profileUser.use_case.UserUseCases
import lol.xget.groceryapp.user.shoppingCar.use_case.GetShopData
import lol.xget.groceryapp.user.shoppingCar.use_case.GetUserData
import lol.xget.groceryapp.user.shoppingCar.use_case.PlaceOrderUseCase

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //TODO("GOOGLE SIGN IN ")
    @Provides
    @Singleton
    fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        val signInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context, signInOptions)
    }

    @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationCompenent (i.e. everywhere in the application)
    @Provides
    fun provideYourDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        CartItemsDatabase::class.java,
        "CartItems"
    ).build() // The reason we can construct a database for the repo



    @Provides
    @Singleton
    fun provideAuthRepo(): AuthRepository {
        return AuthRepoImpl()
    }

    @Provides
    @Singleton
    fun provideUserRepo(): UserRepository {
        return UserRepoImpl()
    }

    @Provides
    @Singleton
    fun provideSellerRepo(): SellerRepository {
        return SellerRepoImpl()
    }

    @Provides
    @Singleton
    fun cartItemsDao(db: CartItemsDatabase): CartItemsDao = db.cartItemsDao()

    @Provides
    @Singleton
    fun provideSellerUseCases(sellerRepo: SellerRepository): HomeSellerUseCases {
        return HomeSellerUseCases(
            getShopProducts = GetShopProducts(sellerRepo),
            deleteProducts = DeleteProduct(sellerRepo),
            updateProduct = UpdateProductUseCase(sellerRepo),
            addProduct = AddProductUseCase(sellerRepo),
            getSpecificProduct = GetSpecificProductFromSeller(sellerRepo),
            getSellerProfile = GetSellerProfileUseCase(sellerRepo),
            updateShopData = UpdateShopData(sellerRepo),
            getSpecificShop = GetSpecificShopUseCase(sellerRepo),
            updateShopBanners = UpdateShopBanners(sellerRepo)
        )
    }


    @Provides
    @Singleton
    fun provideProfileUseCases(profileUserRepository: UserRepository): UserUseCases {
        return UserUseCases(
            updateProfile = UpdateProfileUseCase(profileUserRepository),
            getProfileInfo = GetProfileUseCase(profileUserRepository),
            getShopsList = GetShopsList(profileUserRepository),
            getShop = GetShopData(profileUserRepository),
            getUSerData = GetUserData(profileUserRepository),
            placeOrder = PlaceOrderUseCase(profileUserRepository),
            getOrders = GetOrdersUseCase(profileUserRepository)
        )
    }

    @Provides
    @Singleton
    fun provideAuthUseCases(authRepository: AuthRepository): AuthUseCases {
        return AuthUseCases(
            loginCase = LoginUseCase(authRepository),
            registerCase = RegisterUseCase(authRepository),
            recoverPasswordUseCase = RecoverPasswordUseCase(authRepository)
        )
    }
}