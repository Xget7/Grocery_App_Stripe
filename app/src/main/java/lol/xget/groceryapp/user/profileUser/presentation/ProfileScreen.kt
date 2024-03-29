package lol.xget.groceryapp.user.profileUser.presentation

import android.annotation.SuppressLint
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EditLocation
import androidx.compose.material.icons.outlined.Settings
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
import com.talhafaki.composablesweettoast.util.SweetToastUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import lol.xget.groceryapp.R
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.auth.login.presentation.components.EventDialog
import lol.xget.groceryapp.auth.login.presentation.components.RoundedButton
import lol.xget.groceryapp.auth.login.presentation.components.TransparentTextField
import lol.xget.groceryapp.user.mainUser.domain.User

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalCoroutinesApi
@Composable
fun ProfileScreen(
    navController: NavController,
    activity: Activity,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val openedScaffold = remember {
        mutableStateOf(false)
    }

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

    val resultPlacesError = remember {
        mutableStateOf(false)
    }
    // Set the fields to specify which types of place data to
    // return after the user has made a selection.
    val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

    // Start the autocomplete intent.
    val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
        .build(activity)

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


    val focusManager = LocalFocusManager.current


    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))

    Scaffold(
        topBar = {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = "Profile",
                    style = MaterialTheme.typography.h5.copy(
                        color = MaterialTheme.colors.primaryVariant
                    ),
                    fontSize = 30.sp
                )
                Spacer(modifier = Modifier.width(120.dp))
                IconButton(onClick = {
//                    openedScaffold.value = true
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Settings ICon",
                        tint = MaterialTheme.colors.onSecondary
                    )
                }
            }
        },
        drawerContent = {
            if (scaffoldState.drawerState.isOpen){
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.padding(16.dp),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onSecondary
                    )
                    Divider()
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { navController.navigate(Destinations.UserOrdersScreen.route) }) {
                        Text(
                            text = "Orders",
                            style = MaterialTheme.typography.h5,
                            modifier = Modifier.padding(16.dp),
                            fontSize = 16.sp,
                            color =MaterialTheme.colors.primaryVariant

                        )
                    }
                    Divider()
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                        TODO()
                    }) {
                        Text(
                            text = "Politics ",
                            style = MaterialTheme.typography.h5,
                            modifier = Modifier.padding(16.dp),
                            fontSize = 16.sp,
                            color =MaterialTheme.colors.primaryVariant
                        )
                    }


                }
            }

        },
        scaffoldState = scaffoldState,
        drawerBackgroundColor = MaterialTheme.colors.background,
    ) {
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




            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top

                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, bottom = 50.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Card(
                            modifier = Modifier
                                .size(140.dp)
                                .testTag(tag = "circle")
                                .clickable(
                                    onClick = { launcher.launch("image/*") }
                                ),
                            shape = CircleShape,
                            elevation = 8.dp
                        ) {
                            if (bitmap.value != null) {
                                bitmap.value?.let { btm ->
                                    Image(
                                        bitmap = btm.asImageBitmap(),
                                        contentDescription = "User Profile Image",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize(),
                                    )
                                }
                            } else {
                                GlideImage(
                                    imageModel = viewModel.profilePhoto.value,
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
                                fontWeight = FontWeight.Bold
                            ),
                            fontSize = 24.sp,
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
                                imeAction = ImeAction.Next,
                                maxLines = 1,
                                laberColor = MaterialTheme.colors.onSecondary


                            )

                            TransparentTextField(
                                textFieldValue = viewModel.phoneValue,
                                textLabel = "Phone Number",
                                maxChar = 14,
                                keyboardType = KeyboardType.Phone,
                                keyboardActions = KeyboardActions(
                                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                                ),
                                imeAction = ImeAction.Next,
                                maxLines = 1,
                                laberColor = MaterialTheme.colors.onSecondary

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
                                maxLines = 1,
                                laberColor = MaterialTheme.colors.onSecondary,
                                readOnly = true
                            )

                            Spacer(modifier = Modifier.height(25.dp))

                            if (viewModel.longitude.value == 0f && viewModel.latitude.value == 0f) {
                                OutlinedButton(
                                    modifier = Modifier,
                                    onClick = { launchPlaces.launch(intent) },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(0xFFF05555)
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AddLocation,
                                        contentDescription = "Location Icon",
                                        tint = MaterialTheme.colors.background
                                    )
                                    Text(
                                        text = "Set your location",
                                        color = MaterialTheme.colors.background
                                    )
                                }
                            } else {
                                OutlinedButton(
                                    modifier = Modifier,
                                    onClick = { launchPlaces.launch(intent) },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(0xFFF05555)
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.EditLocation,
                                        contentDescription = "Location Icon",
                                        tint = MaterialTheme.colors.background
                                    )
                                    Text(
                                        text = "Update location",
                                        color = MaterialTheme.colors.background
                                    )
                                }
                            }


                            val user = User(
                                profilePhoto = if (profileImage == null) {
                                    viewModel.profilePhoto.value
                                } else {
                                    profileImage.toString()
                                },
                                accountType = "user",
                                userName = viewModel.fullNameValue.value,
                                phone = viewModel.phoneValue.value,
                                state = viewModel.stateValue.value,
                                city = viewModel.cityValue.value,
                                address = viewModel.addressValue.value,
                                country = viewModel.country.value,
                                uid = viewModel.firebaseAuthCurrentUser,
                                latitude = viewModel.latitude.value,
                                longitude = viewModel.longitude.value
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            RoundedButton(
                                text = "Save",
                                color = MaterialTheme.colors.onSecondary,
                                displayProgressBar = viewModel.state.value.loading!!,
                                progressBarColor = MaterialTheme.colors.background,
                                onClick = {
                                    viewModel.updateUserProfile(user, profileImage)
                                }
                            )

                        }
                    }
                }
            }

            if (viewModel.state.value.successUpdate!!) {
                SweetToastUtil.SweetSuccess(
                    message = "Success Update!",
                    duration = Toast.LENGTH_SHORT,
                    padding = PaddingValues(top = 16.dp),
                    contentAlignment = Alignment.TopCenter
                )
            }
            if (resultPlacesError.value) {
                SweetToastUtil.SweetError(
                    message = "Error getting place!",
                    duration = Toast.LENGTH_SHORT,
                    padding = PaddingValues(top = 16.dp),
                    contentAlignment = Alignment.TopCenter
                )
                resultPlacesError.value = false
            }

            if (viewModel.state.value.errorMsg != null) {
                EventDialog(
                    errorMessage = viewModel.state.value.errorMsg,
                    onDismiss = { viewModel.hideErrorDialog() })
            }
            LaunchedEffect(openedScaffold.value == true) {
                if (openedScaffold.value){
                    scaffoldState.drawerState.open()
                }
            }
        }
    }
}



