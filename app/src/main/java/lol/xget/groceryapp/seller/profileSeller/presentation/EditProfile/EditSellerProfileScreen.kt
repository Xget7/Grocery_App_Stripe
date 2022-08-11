package lol.xget.groceryapp.seller.profileSeller.presentation.EditProfile
import android.app.Activity
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.skydoves.landscapist.glide.GlideImage
import com.talhafaki.composablesweettoast.util.SweetToastUtil.SweetSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import lol.xget.groceryapp.R
import lol.xget.groceryapp.auth.login.presentation.components.EventDialog
import lol.xget.groceryapp.auth.login.presentation.components.RoundedButton
import lol.xget.groceryapp.auth.login.presentation.components.TransparentTextField
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.ui.components.DialogBoxLoading
import lol.xget.groceryapp.user.mainUser.domain.User

@ExperimentalCoroutinesApi
@Composable
fun EditSellerProfileScreen(
    navController: NavController,
    activity: Activity,
    viewModel: EditSellerProfileViewModel = hiltViewModel()
) {
    var profileImage by remember {
        mutableStateOf<Uri?>(null)
    }
    val resultPlacesError = remember {
        mutableStateOf(false)
    }

    var shopBannerImage by remember {
        mutableStateOf<Uri?>(null)
    }

    val context = LocalContext.current

    val profileImageLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            profileImage = uri
        }


    val profilePhotoBitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }


    val focusManager = LocalFocusManager.current

    val launchPlaces =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                Activity.RESULT_OK -> {
                    it.data?.let { _ ->

                        val place = Autocomplete.getPlaceFromIntent(it.data!!)
                        viewModel.addressValue.value = place.address!!
                        place.addressComponents?.let { it1 ->
                            Log.e(
                                "AddressComponents",
                                it1.toString()
                            )
                        }
                        viewModel.longitude.value = place.latLng?.longitude!!.toFloat()
                        viewModel.latitude.value = place.latLng?.latitude!!.toFloat()
                        place.address?.let{ addr ->
                            viewModel.addressValue.value = addr
                        }
                    }
                }

                AutocompleteActivity.RESULT_ERROR -> {
                    //  Handle the error.
                    it.data?.let { intent ->
                        val status = Autocomplete.getStatusFromIntent(it.data!!)
                        Log.d("Error",status.statusMessage.toString() )
                        resultPlacesError.value = true
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }


        }
    val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

    // Start the autocomplete intent.
    val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
        .build(activity)



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
        IconButton(onClick = {
            navController.navigateUp()
        }) {
            Icon(
                modifier = Modifier.padding(top = 22.dp),
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colors.onSecondary
            )
        }

        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 22.dp),
            text = "Profile",
            style = MaterialTheme.typography.h2.copy(
                color = MaterialTheme.colors.primaryVariant
            ),
            fontSize = 25.sp
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

                    Card(
                        modifier = Modifier
                            .size(100.dp)
                            .testTag(tag = "circle")
                            .clickable(
                                onClick = { profileImageLauncher.launch("image/*") }
                            ),
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
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = viewModel.fullNameValue.value,
                        style = MaterialTheme.typography.h4.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colors.onSecondary
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

                        TransparentTextField(
                            textFieldValue = viewModel.addressValue,
                            textLabel = " Address",
                            keyboardType = KeyboardType.Text,
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                            ),
                            imeAction = ImeAction.Next,
                            modifier = Modifier.clickable {
                                launchPlaces.launch(intent)
                            }
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


                        val user = User(
                            profilePhoto = if (profileImage == null) {
                                viewModel.shopImage.value
                            }else {
                                profileImage.toString()
                            },
                            accountType = "seller",
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
                            uid = viewModel.currentUser,
                            deliveryFee = viewModel.deliveryFee.value,
                            shopOpen = false,
                            email = viewModel.currentUserGmail
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        RoundedButton(
                            modifier = Modifier.width(200.dp),
                            text = "Save",
                            color = MaterialTheme.colors.onSecondary,
                            displayProgressBar = viewModel.state.value.loading!!
                        ) {
                            viewModel.updateProfile(user, profileImage, shopBannerImage)
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
                    Destinations.SellerMainDestinations.route
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




