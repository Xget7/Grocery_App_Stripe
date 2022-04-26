package lol.xget.groceryapp.seller.profileSeller.presentation.BannerOption
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.skydoves.landscapist.glide.GlideImage
import com.talhafaki.composablesweettoast.util.SweetToastUtil.SweetSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import lol.xget.groceryapp.R
import lol.xget.groceryapp.auth.login.presentation.components.EventDialog
import lol.xget.groceryapp.auth.login.presentation.components.RoundedButton
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.ui.components.DialogBoxLoading
import lol.xget.groceryapp.user.mainUser.domain.User

@ExperimentalCoroutinesApi
@Composable
fun BannerScreen(
    navController: NavController,
    viewModel: BannerViewModel = hiltViewModel()
) {
    var shopAdBannerImage by remember {
        mutableStateOf<Uri?>(null)
    }

    var shopBannerImage by remember {
        mutableStateOf<Uri?>(null)
    }

    val context = LocalContext.current

    val shopBannerImageLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            shopBannerImage = uri
        }

    val shopAdBannerImageLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            shopAdBannerImage = uri
            Log.e("BannerScreen", "shopAdBannerImage + $uri ")

        }

    val ShopBannerImageBitMap = remember {
        mutableStateOf<Bitmap?>(null)
    }


    val ShopAdBannerImageBitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {

        // AD BANNER
        shopAdBannerImage?.let {
            if (Build.VERSION.SDK_INT < 28) {
                ShopAdBannerImageBitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                ShopAdBannerImageBitmap.value = ImageDecoder.decodeBitmap(source)
            }
        }

        // BANNER
        shopBannerImage?.let {
            if (Build.VERSION.SDK_INT < 28) {
                ShopBannerImageBitMap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                ShopBannerImageBitMap.value = ImageDecoder.decodeBitmap(source)
            }
        }

        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 12.dp),
            text = "Banners",
            style = MaterialTheme.typography.h6.copy(
                color = MaterialTheme.colors.primaryVariant
            )
        )

        Text(
            modifier = Modifier.padding(top = 130.dp).align(Alignment.TopCenter),
            text = "Add new banner",
            style = MaterialTheme.typography.h4.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colors.primaryVariant
        )



        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp, bottom = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    //-----------------------------------------Banner---------------------------------\\
                    Card(
                        modifier = Modifier
                            .width(340.dp)
                            .height(180.dp),
                        shape = RoundedCornerShape(4),
                        elevation = 12.dp
                    ) {
                        if (ShopBannerImageBitMap.value != null) {
                            ShopBannerImageBitMap.value?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = "Banner Image",
                                    contentScale = ContentScale.FillWidth,
                                    modifier = Modifier.fillMaxSize(),
                                )
                            }
                        } else {
                            GlideImage(
                                imageModel = viewModel.shopBannerImage.value,
                                // Crop, Fit, Inside, FillHeight, FillWidth, None
                                contentScale = ContentScale.FillWidth,
                                // shows an image with a circular revealed animation.
                                // shows a placeholder ImageBitmap when loading.
                                placeHolder = ImageBitmap.imageResource(R.drawable.holder),
                                // shows an error ImageBitmap when the request failed.
                                error = ImageBitmap.imageResource(R.drawable.error)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))


                    OutlinedButton(
                        modifier = Modifier
                            .width(400.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(40),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(
                                0xFF01ae5e
                            )
                        ),
                        onClick = {
                            shopBannerImageLauncher.launch("image/*")
                        }
                    ) {
                        Text(text = "Change banner", color = Color.White, fontSize = 18.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    //-----------------------------------------AD Banner---------------------------------\\

                    Card(
                        modifier = Modifier
                            .width(340.dp)
                            .height(180.dp),
                        shape = RoundedCornerShape(4),
                        elevation = 12.dp
                    ) {
                        if (ShopAdBannerImageBitmap.value != null) {
                            ShopAdBannerImageBitmap.value?.let {
                                Log.e("ShopAdBannerUri", "Bitmap")

                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = "Ad banner Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize(),
                                )
                            }
                        } else {
                            Log.e("ShopAdBannerUri", "Glide")

                            GlideImage(
                                imageModel = viewModel.shopAdBannerImage.value,
                                // Crop, Fit, Inside, FillHeight, FillWidth, None
                                contentScale = ContentScale.Crop,
                                // shows an image with a circular revealed animation.
                                // shows a placeholder ImageBitmap when loading.
                                placeHolder = ImageBitmap.imageResource(R.drawable.holder),
                                // shows an error ImageBitmap when the request failed.
                                error = ImageBitmap.imageResource(R.drawable.error)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedButton(
                            modifier = Modifier
                                .width(400.dp)
                                .height(50.dp),
                            shape = RoundedCornerShape(40),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(
                                    0xFF01ae5e
                                )
                            ),
                            onClick = {
                                shopAdBannerImageLauncher.launch("image/*")
                            }
                        ) {
                            Text(text = "Change publicitary banner", color = Color.White, fontSize = 18.sp)
                        }

                        Spacer(modifier = Modifier.height(6.dp))




                        RoundedButton(
                            modifier = Modifier.width(200.dp),
                            text = "Save",
                            displayProgressBar = viewModel.state.value.loading!!
                        ) {
                            val user = User(
                                uid = viewModel.currentUserUid,
                                shopBanner = if (shopBannerImage == null) {
                                    viewModel.shopBannerImage.value
                                }else {
                                    shopBannerImage.toString()
                                },
                                shopAdBanner = if (shopAdBannerImage == null) {
                                    viewModel.shopAdBannerImage.value
                                }else {
                                    shopAdBannerImage.toString()
                                },
                                accountType = "seller",
                                profilePhoto =viewModel.shopImage.value,
                                shopName = viewModel.shopNameValue.value,
                                country = viewModel.country.value,
                                city = viewModel.cityValue.value,
                                address = viewModel.addressValue.value,
                                phone = viewModel.phoneValue.value,
                                userName = viewModel.fullNameValue.value,
                                latitude = viewModel.latitude.value,
                                longitude = viewModel.longitude.value,
                                state = viewModel.stateValue.value,
                                online = true,
                                deliveryFee = viewModel.deliveryFee.value,
                                shopOpen = false,
                                email = viewModel.currentUserGmail

                            )

                            Log.e("BannerScreen", "shopAdBannerImageUser + $shopAdBannerImage ")

                            viewModel.updateProfile(user, shopAdBannerImage, shopBannerImage)
                        }

                    }
                }
            }
        }

        if (viewModel.state.value.successUpdate!!) {
            SweetSuccess(
                message = "Success Update!",
                duration = Toast.LENGTH_SHORT,
                padding = PaddingValues(top = 16.dp),
                contentAlignment = Alignment.TopCenter
            )
            LaunchedEffect(viewModel.state.value.successUpdate) {
                delay(500)
                navController.navigate(
                    Destinations.SellerHomeDestinations.route
                )
            }
        }

        if (viewModel.state.value.loading!!) {
            DialogBoxLoading()
        }


        if (viewModel.state.value.errorMsg != null) {
            EventDialog(
                errorMessage = viewModel.state.value.errorMsg,
                onDismiss = { viewModel.hideErrorDialog() })
        }
    }
}




