package lol.xget.groceryapp.auth.register.presentation.register_user

import android.Manifest
import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View.inflate
import android.widget.FrameLayout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.resources.Compatibility.Api21Impl.inflate
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.findFragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.talhafaki.composablesweettoast.util.SweetToastUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lol.xget.groceryapp.MainActivity
import lol.xget.groceryapp.R
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.auth.login.presentation.components.EventDialog
import lol.xget.groceryapp.auth.login.presentation.components.TransparentTextField
import lol.xget.groceryapp.auth.mapLocalization.presentation.MapsActivityResultContract
import lol.xget.groceryapp.databinding.ActivityMainBinding.inflate
import lol.xget.groceryapp.databinding.ActivityMapsBinding.inflate
import lol.xget.groceryapp.databinding.AutoCompleteSupportFragmentBinding.inflate
import lol.xget.groceryapp.ui.components.DialogBoxLoading
import lol.xget.groceryapp.user.mainUser.domain.User


@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@Composable
fun RegistrationScreen(
    navController: NavController,
    activity: Activity,
    viewModel: RegisterUserViewModel = hiltViewModel()

) {





    var inProfileScreen by remember { mutableStateOf(false) }
    var passwordVisibility by remember { mutableStateOf(false) }
    var confirmPasswordVisibility by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    if (viewModel._state.value.displayPb == true) {
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colors.onSecondary)
        }

    }else{
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.background)
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
                            navController.navigate(Destinations.LoginDestinations.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Icon",
                            tint = MaterialTheme.colors.onSecondary
                        )
                    }

                    Text(
                        text = "Register as user",
                        style = MaterialTheme.typography.h5.copy(
                            color = MaterialTheme.colors.primaryVariant
                        )
                    )

                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
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
                    Spacer(modifier = Modifier.height(20.dp))

                    TransparentTextField(
                        textFieldValue = viewModel.emailValue,
                        textLabel = "Email",
                        keyboardType = KeyboardType.Email,
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        imeAction = ImeAction.Next,
                        maxLines = 1

                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    TransparentTextField(
                        textFieldValue = viewModel.phoneValue,
                        textLabel = "Phone Number",
                        maxChar = 10,
                        keyboardType = KeyboardType.Phone,
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        imeAction = ImeAction.Next,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(20.dp))

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
                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        maxLines = 1

                    )
                    Spacer(modifier = Modifier.height(20.dp))

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
                        visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        maxLines = 1

                    )

                    Spacer(modifier = Modifier.height(26.dp))
                    OutlinedButton(
                        modifier = Modifier
                            .width(300.dp)
                            .height(60.dp),
                        shape = RoundedCornerShape(40),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.onSecondary
                        ),
                        onClick = {
                            val user = User(
                                userName = viewModel.fullNameValue.value,
                                phone = viewModel.phoneValue.value,
                                address = viewModel.addressValue.value,
                                accountType = "user",
                                email = viewModel.emailValue.value,
                                uid = viewModel.userUid.value,
                                latitude = null,
                                longitude = null

                            )
                            viewModel.registerUser(user, navController)
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
                                    color = MaterialTheme.colors.onSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("Log in")
                            }
                        },
                        onClick = {
                            navController.navigate(Destinations.LoginDestinations.route)
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
                                    color = MaterialTheme.colors.onSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("Register as seller")
                            }
                        },
                        onClick = {
                            navController.navigate(
                                Destinations.RegisterSellerDestinations.passLatitudeLatitude(
                                    0.0,
                                    0.0
                                )
                            )
                        }
                    )


                }


            }


            if (viewModel._state.value.errorMsg != null) {
                EventDialog(
                    errorMessage = viewModel._state.value.errorMsg,
                    onDismiss = { viewModel.hideErrorDialog() })
            }
        }
    }

}


