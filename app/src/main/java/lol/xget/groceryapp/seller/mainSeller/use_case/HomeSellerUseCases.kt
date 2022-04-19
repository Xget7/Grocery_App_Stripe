package lol.xget.groceryapp.seller.mainSeller.use_case

import lol.xget.groceryapp.domain.use_case.products.AddProductUseCase
import lol.xget.groceryapp.domain.use_case.products.GetSpecificProductFromSeller
import lol.xget.groceryapp.domain.use_case.products.UpdateProductUseCase
import lol.xget.groceryapp.user.mainUser.use_case.GetSpecificShopUseCase

data class HomeSellerUseCases (
    val getSellerProfile : GetSellerProfileUseCase,
    val getShopProducts: GetShopProducts,
    val deleteProducts : DeleteProduct,
    val updateProduct: UpdateProductUseCase,
    val updateShopData: UpdateShopData,
    val addProduct: AddProductUseCase,
    val getSpecificProduct: GetSpecificProductFromSeller,
    val getSpecificShop: GetSpecificShopUseCase,

    )