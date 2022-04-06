package lol.xget.groceryapp.presentation.main.Seller.Profile

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.skydoves.landscapist.glide.GlideImage
import com.talhafaki.composablesweettoast.util.SweetToastUtil.SweetSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import lol.xget.groceryapp.R
import lol.xget.groceryapp.domain.model.ShopModel
import lol.xget.groceryapp.domain.model.UserModel
import lol.xget.groceryapp.domain.util.Screen
import lol.xget.groceryapp.presentation.auth.login.components.EventDialog
import lol.xget.groceryapp.presentation.auth.login.components.RoundedButton
import lol.xget.groceryapp.presentation.auth.login.components.TransparentTextField
import lol.xget.groceryapp.ui.components.DialogBoxLoading

@ExperimentalCoroutinesApi
@Composable
fun SellerProfileScreen(
    navController: NavController,
    viewModel: SellerProfileViewModel = hiltViewModel()
) {
    var profileImage by remember {
        mutableStateOf<Uri?>(null)
    }

    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            profileImage = uri
        }
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        profileImage?.let {

            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }


        }
        IconButton(
            onClick = {
                navController.navigate(Screen.SellerHomeScreen.route)
            }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back Icon",
                tint = MaterialTheme.colors.primaryVariant
            )
        }



        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 12.dp),
            text = "Profile",
            style = MaterialTheme.typography.h6.copy(
                color = MaterialTheme.colors.primaryVariant
            )
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
                        .padding(14.dp, bottom = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Card(
                        modifier = Modifier
                            .size(100.dp)
                            .testTag(tag = "circle")
                            .clickable(
                                onClick = { launcher.launch("image/*") }
                            )
                            ,
                        shape = CircleShape,
                        elevation = 12.dp
                    ) {
                        if (bitmap.value != null){
                            bitmap.value?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = "Profile Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize(),
                                )
                            }
                        }else{
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
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = viewModel.fullNameValue.value,
                        style = MaterialTheme.typography.h4.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )


                    Spacer(modifier = Modifier.height(5.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {


                        TransparentTextField(
                            singleLine = true,
                            textFieldValue = viewModel.fullNameValue,
                            textLabel = "Name",
                            keyboardType = KeyboardType.Text,
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            imeAction = ImeAction.Next
                        )

                        TransparentTextField(
                            textFieldValue = viewModel.shopNameValue,
                            textLabel = "Shop Name",
                            keyboardType = KeyboardType.Text,
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            imeAction = ImeAction.Next
                        )

                        TransparentTextField(
                            textFieldValue = viewModel.phoneValue,
                            textLabel = "Phone Number",
                            maxChar = 10,
                            keyboardType = KeyboardType.Phone,
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            imeAction = ImeAction.Next
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)

                        ) {
                            TransparentTextField(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(50.dp),
                                textFieldValue = viewModel.countryValue,
                                textLabel = "Country",
                                keyboardType = KeyboardType.Text,
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        focusManager.moveFocus(FocusDirection.Down)
                                    }
                                ),
                                imeAction = ImeAction.Next
                            )

                            TransparentTextField(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(50.dp),
                                textFieldValue = viewModel.stateValue,
                                textLabel = "State",
                                keyboardType = KeyboardType.Text,
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        focusManager.moveFocus(FocusDirection.Down)
                                    }
                                ),
                                imeAction = ImeAction.Next
                            )

                            TransparentTextField(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(50.dp),
                                textFieldValue = viewModel.cityValue,
                                textLabel = "City",
                                keyboardType = KeyboardType.Text,
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        focusManager.moveFocus(FocusDirection.Down)
                                    }
                                ),
                                imeAction = ImeAction.Next
                            )
                        }

                        TransparentTextField(
                            textFieldValue = viewModel.addressValue,
                            textLabel = " Address",
                            keyboardType = KeyboardType.Text,
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                            ),
                            imeAction = ImeAction.Next
                        )

                        TransparentTextField(
                            textFieldValue = viewModel.deliveryFee,
                            textLabel = "Delivery Fee",
                            keyboardType = KeyboardType.Number,
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                            ),
                            imeAction = ImeAction.Next
                        )


                        val user = UserModel(
                            profilePhoto = profileImage.toString().ifEmpty { viewModel.shopImage.value },
                            fullName = viewModel.fullNameValue.value,
                            accountType = viewModel.accountType.value,
                            phone = viewModel.phoneValue.value,
                            state = viewModel.stateValue.value,
                            city = viewModel.cityValue.value,
                            address = viewModel.addressValue.value,
                            country = viewModel.countryValue.value,
                            uid = viewModel.currentUser,
                            deliveryFee = viewModel.deliveryFee.value,
                            shopName = viewModel.shopNameValue.value
                        )


                        Spacer(modifier = Modifier.height(6.dp))
                        RoundedButton(
                            modifier = Modifier.width(200.dp),
                            text = "Save",
                            displayProgressBar = viewModel.state.value.loading!!
                        ) {
                            viewModel.updateProfile(user, profileImage)

                        }

                    }
                }
            }
        }
            //TODO("ADD BANNER IMG ")

        if (viewModel.state.value.successUpdate!!){
            SweetSuccess(message = "Success Update!", duration = Toast.LENGTH_SHORT, padding = PaddingValues(top = 16.dp), contentAlignment = Alignment.TopCenter)
            LaunchedEffect(viewModel.state.value.successUpdate){
                delay(500)
                navController.navigate(
                     Screen.SellerHomeScreen.route
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



