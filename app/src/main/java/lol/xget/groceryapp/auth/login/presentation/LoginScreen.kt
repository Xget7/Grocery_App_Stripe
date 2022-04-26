package lol.xget.groceryapp.auth.login.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import lol.xget.groceryapp.R


import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import lol.xget.groceryapp.auth.login.presentation.components.EventDialog
import lol.xget.groceryapp.auth.login.presentation.components.GoogleButton
import lol.xget.groceryapp.auth.login.presentation.components.TransparentTextField
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.ui.components.DialogBoxLoading


@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {

    var passwordVisibility by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val state = viewModel.state.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.onBackground)
    ) {
        Image(
            painter = painterResource(id = R.drawable.grocery),
            contentDescription = "Login Image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(bottom = 350.dp)
                .align(Alignment.Center)
                .width(250.dp)
                .height(250.dp)
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            ConstraintLayout {

                val (surface, fab) = createRefs()

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp)
                        .constrainAs(surface) {
                            bottom.linkTo(parent.bottom)
                        },
                    color = MaterialTheme.colors.background,
                    shape = RoundedCornerShape(
                        topStartPercent = 8,
                        topEndPercent = 8
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, bottom = 20.dp),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Welcome Back!",
                            style = MaterialTheme.typography.h4.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colors.primaryVariant
                            )
                        )

                        Text(
                            text = "Login to your Account",
                            style = MaterialTheme.typography.h5.copy(
                                color = MaterialTheme.colors.primaryVariant
                            )
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TransparentTextField(
                                singleLine = true,
                                textFieldValue = viewModel.emailValue,
                                textLabel = "Email",
                                keyboardType = KeyboardType.Email,
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        focusManager.moveFocus(FocusDirection.Down)
                                    }
                                ),
                                imeAction = ImeAction.Next
                            )

                            TransparentTextField(
                                singleLine = true,
                                textFieldValue = viewModel.passwordValue,
                                textLabel = "Password",
                                keyboardType = KeyboardType.Password,
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        focusManager.clearFocus()

                                        //TODO("LOGIN")
                                    }
                                ),
                                imeAction = ImeAction.Done,
                                trailingIcon = {
                                    IconButton(
                                        onClick = {
                                            passwordVisibility = !passwordVisibility
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (passwordVisibility) {
                                                Icons.Default.Visibility
                                            } else {
                                                Icons.Default.VisibilityOff
                                            },
                                            contentDescription = "Toggle Password Icon"
                                        )
                                    }
                                },
                                visualTransformation = if (passwordVisibility) {
                                    VisualTransformation.None
                                } else {
                                    PasswordVisualTransformation()
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))


                            ClickableText(
                                text = buildAnnotatedString {
                                    append("Forgot Password?")
                                },
                                style = TextStyle(
                                    color = MaterialTheme.colors.primaryVariant
                                )
                            ) {
                                navController.navigate(Destinations.RecoverPasswordDestinations.route)
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))



                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
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
                                    viewModel.login(
                                        viewModel.emailValue.value,
                                        viewModel.passwordValue.value,
                                        navController
                                    )
                                }
                            ) {
                                Text(text = "Login", color = Color.White, fontSize = 18.sp)
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            GoogleButton(
                                modifier = Modifier
                                    .height(50.dp)
                                    .width(300.dp)
                                    .align(Alignment.CenterHorizontally),
                                shape = RoundedCornerShape(40),
                                onClicked = {
                                    TODO()
                                }
                            )



                            Spacer(modifier = Modifier.height(10.dp))
                            ClickableText(
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            color = MaterialTheme.colors.primaryVariant,
                                        )
                                    ) {
                                        append("Do not have an Account?")
                                    }
                                    withStyle(
                                        style = SpanStyle(
                                            color = MaterialTheme.colors.primaryVariant,
                                            fontWeight = FontWeight.Bold
                                        )
                                    ) {
                                        append("Sign up")
                                    }
                                }
                            ) {
                                navController.navigate(Destinations.RegisterUserDestinations.route)
                            }
                        }
                    }
                }

                FloatingActionButton(
                    modifier = Modifier
                        .size(72.dp)
                        .constrainAs(fab) {
                            top.linkTo(surface.top, margin = (-36).dp)
                            end.linkTo(surface.end, margin = 36.dp)
                        },
                    backgroundColor = MaterialTheme.colors.primary,
                    onClick = {
                        //TODO()
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(42.dp),
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Forward Icon",
                        tint = Color.White
                    )
                }

            }

            if (viewModel.state.value.displayPb) {
                DialogBoxLoading()
            }



            if (viewModel.state.value.errorMsg != null) {
                EventDialog(
                    errorMessage = viewModel.state.value.errorMsg,
                    onDismiss = { viewModel.hideErrorDialog() })
            }
        }
    }
}




