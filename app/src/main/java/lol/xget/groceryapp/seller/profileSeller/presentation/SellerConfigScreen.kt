package lol.xget.groceryapp.seller.profileSeller.presentation

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.skydoves.landscapist.glide.GlideImage
import com.talhafaki.composablesweettoast.util.SweetToastUtil.SweetSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import lol.xget.groceryapp.R
import lol.xget.groceryapp.auth.login.presentation.components.EventDialog
import lol.xget.groceryapp.domain.util.Destinations

@ExperimentalCoroutinesApi
@Composable
fun SellerConfigScreen(
    navController: NavController,
    viewModel: SellerConfigViewModel = hiltViewModel(),
) {
    var profileImage by remember {
        mutableStateOf<Uri?>(null)
    }

    val context = LocalContext.current

    val profilePhotoBitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {

            profileImage?.let {
                if (Build.VERSION.SDK_INT < 28) {
                    profilePhotoBitmap.value = MediaStore.Images
                        .Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    profilePhotoBitmap.value = ImageDecoder.decodeBitmap(source)
                }
            }


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(top = 6.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top

                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp, bottom = 8.dp),
                        horizontalAlignment = Alignment.Start
                    ) {

                        Row(
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            Card(
                                modifier = Modifier
                                    .size(70.dp)
                                    .testTag(tag = "circle"),
                                shape = CircleShape,
                                elevation = 12.dp
                            ) {
                                if (profilePhotoBitmap.value != null) {
                                    profilePhotoBitmap.value?.let {
                                        Image(
                                            bitmap = it.asImageBitmap(),
                                            contentDescription = "Profile Image",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize(),
                                        )
                                    }
                                } else {
                                    GlideImage(
                                        imageModel = viewModel.shopImage.value,
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

                            Spacer(modifier = Modifier.width(40.dp))

                            Column(
                                verticalArrangement = Arrangement.Top
                            ) {
                                Row {
                                    Text(
                                        text = viewModel.fullNameValue.value,
                                        style = MaterialTheme.typography.h4.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = MaterialTheme.colors.primaryVariant
                                    )
                                    Spacer(modifier = Modifier.width(18.dp))

                                    IconButton(onClick = { navController.navigate(Destinations.SellerEditProfileDestinations.route) }) {
                                        Icon(
                                            modifier = Modifier.padding(bottom = 39.dp),
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit Profile",
                                            tint = MaterialTheme.colors.onSecondary
                                        )
                                    }
                                }
                                Row {
                                    Text(
                                        text = viewModel.addressValue.value,
                                        style = MaterialTheme.typography.h4.copy(
                                            fontWeight = FontWeight.Medium
                                        ),
                                        color = MaterialTheme.colors.primaryVariant,
                                        textAlign = TextAlign.Start
                                    )
                                }


                            }


                        }
                    }
                    Divider()

                    //-----------------------------------------OPTIONS------------------------------\\

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
                            Destinations.SellerMainDestinations.route
                        )
                    }
                }

                if (viewModel.state.value.loading!!) {
                    CircularProgressIndicator(color = MaterialTheme.colors.onSecondary)
                }


                if (viewModel.state.value.errorMsg != null) {
                    EventDialog(
                        errorMessage = viewModel.state.value.errorMsg,
                        onDismiss = { viewModel.hideErrorDialog() })
                }
            }

            Column(
                Modifier
                    .padding(20.dp)
                    .padding(top = 70.dp)
            ) {
                Row(
                    modifier = Modifier.padding(5.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Navigate to Banners",
                        tint = MaterialTheme.colors.onSecondary
                    )
                    Spacer(modifier = Modifier.width(40.dp))
                    TextButton(onClick = { navController.navigate(Destinations.SellerBannerDestination.route) }) {
                        Text(
                            text = "Banners",
                            style = MaterialTheme.typography.h3.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colors.primaryVariant
                        )
                    }
                }
                Divider()

                Row(
                    modifier = Modifier.padding(5.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Navigate to Reviews",
                        tint = Color.Yellow
                    )
                    Spacer(modifier = Modifier.width(40.dp))
                    TextButton(onClick = {
                        navController.navigate(Destinations.ShopReviewsScreen.passShop(viewModel.currentUserUid))
                    }) {
                        Text(
                            text = "Reviews",
                            style = MaterialTheme.typography.h3.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colors.primaryVariant
                        )
                    }
                }

                Divider()


                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .clickable {
                            navController.navigate(Destinations.SellerConfigurationDestinations.route)
                        },
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colors.onSecondary
                    )
                    Spacer(modifier = Modifier.width(40.dp))
                    TextButton(
                        onClick = {
                            navController.navigate(Destinations.SellerConfigurationDestinations.route)
                        },
                    ) {
                        Text(
                            text = "Settings",
                            style = MaterialTheme.typography.h3.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colors.primaryVariant

                        )
                    }
                }

                Divider()
                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .clickable {
                            FirebaseAuth
                                .getInstance()
                                .signOut()
                                .let {
                                    navController.navigate(Destinations.LoginDestinations.route)

                                }
                        },
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Logout",
                        tint = MaterialTheme.colors.onSecondary
                    )
                    Spacer(modifier = Modifier.width(40.dp))
                    TextButton(
                        onClick = {
                            viewModel.currentUserInstance.signOut()
                            navController.navigate(Destinations.LoginDestinations.route)
                            navController.popBackStack()
                        },
                    ) {
                        Text(
                            text = "Log out",
                            style = MaterialTheme.typography.h3.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colors.primaryVariant

                        )
                    }
                }


            }

        }


    }

}




