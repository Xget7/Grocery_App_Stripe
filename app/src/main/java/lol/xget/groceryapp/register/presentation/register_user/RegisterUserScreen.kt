package lol.xget.groceryapp.register.presentation.register_user

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.talhafaki.composablesweettoast.util.SweetToastUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lol.xget.groceryapp.mainUser.domain.User
import lol.xget.groceryapp.domain.util.Screen
import lol.xget.groceryapp.login.presentation.components.EventDialog
import lol.xget.groceryapp.login.presentation.components.TransparentTextField
import lol.xget.groceryapp.mapLocalization.presentation.MapsActivityResultContract
import lol.xget.groceryapp.ui.components.DialogBoxLoading


@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@Composable
fun RegistrationScreen(
    navController: NavController,
    activity: Activity,
    viewModel: RegisterUserViewModel = hiltViewModel()

) {

    var userLatLng by remember {
        mutableStateOf(LatLng(0.0, 0.0))
    }

    val launchMap = rememberLauncherForActivityResult(MapsActivityResultContract()) {
        viewModel.setLocationAddresses(it.latitude, it.longitude)
        userLatLng = it
    }

    val openDialog by viewModel.open.observeAsState(false)

    val gpsStatus by viewModel.gpsStatus.observeAsState(false)

    var latlng = viewModel.userLatLngflow.collectAsState(userLatLng)

    val scope = rememberCoroutineScope()


    var inProfileScreen by remember { mutableStateOf(false) }
    var passwordVisibility by remember { mutableStateOf(false) }
    var confirmPasswordVisibility by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    var warningToastActivateGps by remember {
        mutableStateOf(false)
    }
    var warningToastRejectedPermissions by remember { mutableStateOf(false) }

    val requestPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.locationEnabled()
                if (gpsStatus) {
                    scope.launch {
                        viewModel.open.value = true
                        delay(2000)
                        launchMap.launch(userLatLng)
                    }
                } else {
                    warningToastActivateGps = true
                }
            } else {
                warningToastRejectedPermissions = true
            }
        }


    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {


                IconButton(
                    onClick = {
                        //oNBAck
                        navController.navigate(Screen.LoginScreen.route)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back Icon",
                        tint = MaterialTheme.colors.primaryVariant
                    )
                }

                Text(
                    text = "Register as user",
                    style = MaterialTheme.typography.h5.copy(
                        color = MaterialTheme.colors.primaryVariant
                    )
                )

                IconButton(
                    modifier = Modifier.padding(start = 150.dp),
                    onClick = {
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(
                                activity,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) -> {
                                viewModel.locationEnabled()
                                if (gpsStatus){
                                    launchMap.launch(latlng.value)
                                } else {
                                    warningToastActivateGps = true
                                }
                            }
                            else -> {
                                // Asking for permission
                                requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        }
                    }
                ) {
                    Icon(

                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "Location icon",
                        tint = MaterialTheme.colors.primaryVariant
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                TransparentTextField(
                    textFieldValue = viewModel.fullNameValue,
                    textLabel = "Name",
                    keyboardType = KeyboardType.Text,
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    imeAction = ImeAction.Next
                )

                TransparentTextField(
                    textFieldValue = viewModel.emailValue,
                    textLabel = "Email",
                    keyboardType = KeyboardType.Email,
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
                    textFieldValue = viewModel.passwordValue,
                    textLabel = "Password",
                    keyboardType = KeyboardType.Password,
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    imeAction = ImeAction.Next,
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                passwordVisibility = !passwordVisibility
                            }
                        ) {
                            Icon(
                                imageVector = if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle Password Icon"
                            )
                        }
                    },
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation()
                )

                TransparentTextField(
                    textFieldValue = viewModel.confirmPasswordValue,
                    textLabel = "Confirm Password",
                    keyboardType = KeyboardType.Password,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    imeAction = ImeAction.Done,
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                confirmPasswordVisibility = !confirmPasswordVisibility
                            }
                        ) {
                            Icon(
                                imageVector = if (confirmPasswordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle Password Icon"
                            )
                        }
                    },
                    visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(26.dp))
                OutlinedButton(
                    modifier = Modifier
                        .width(300.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(40),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(
                            0xFF01ae5e
                        )
                    ),
                    onClick = {
                        val user = User(
                            userName = viewModel.fullNameValue.value,
                            phone = viewModel.phoneValue.value,
                            state = viewModel.stateValue.value,
                            city = viewModel.cityValue.value,
                            address = viewModel.addressValue.value,
                            accountType = "user",
                            email = viewModel.emailValue.value,
                            country = viewModel.countryValue.value,
                            uid = viewModel.userUid.value,
                            latitude = userLatLng.latitude.toFloat(),
                            longitude = userLatLng.longitude.toFloat()

                        )
                        viewModel.registerUser(user)
                    }
                ) {
                    Text(text = "Sign Up", color = Color.White, fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(26.dp))

                ClickableText(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colors.primaryVariant,
                            )
                        ) {
                            append("Already have an account?")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colors.primaryVariant,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Log in")
                        }
                    },
                    onClick = {
                        //TODO()
                        //onBack()
                    }
                )



                Spacer(modifier = Modifier.height(26.dp))

                ClickableText(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colors.primaryVariant,
                            )
                        ) {
                            append("Are you a seller?")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colors.primaryVariant,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Register as seller")
                        }
                    },
                    onClick = {
                        navController.navigate(
                            Screen.RegisterSellerScreen.passLatitudeLatitude(
                                0.0,
                                0.0
                            )
                        )
                    }
                )


            }
            if (viewModel._state.value.displayPb == true) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    DialogBoxLoading()
                }

            }


        }
        if (warningToastRejectedPermissions) {
            SweetToastUtil.SweetWarning(message = "Without location permissions we can't get your location")
            warningToastRejectedPermissions = false
        }
        if (warningToastActivateGps) {
            SweetToastUtil.SweetWarning(message = "You need to active your location")
            warningToastActivateGps = false
        }

        if (viewModel._state.value.successRegister && inProfileScreen.equals(false)) {
            navController.navigate(Screen.UserHomeScreen.route)
            inProfileScreen = true
        }

        if (viewModel._state.value.errorMsg != null) {
            EventDialog(
                errorMessage = viewModel._state.value.errorMsg,
                onDismiss = { viewModel.hideErrorDialog() })
        }
    }
}


