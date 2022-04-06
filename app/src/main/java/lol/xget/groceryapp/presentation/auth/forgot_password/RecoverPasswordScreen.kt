package lol.xget.groceryapp.presentation.auth.forgot_password

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import lol.xget.groceryapp.R
import lol.xget.groceryapp.domain.util.Screen
import lol.xget.groceryapp.presentation.auth.login.components.EventDialog
import lol.xget.groceryapp.presentation.auth.login.components.RoundedButton
import lol.xget.groceryapp.presentation.auth.login.components.TransparentTextField

@ExperimentalCoroutinesApi
@Composable
fun RecoverPassword(
    navController: NavController,
    viewModel: RecoverPasswordViewModel = hiltViewModel(),
) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ){
        IconButton(
            onClick = {
                //oNBAck
                navController.navigate(Screen.LoginScreen.route)
            }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back Icon",
                tint = MaterialTheme.colors.primary
            )
        }

        Image(
            painter = painterResource(id = R.drawable.grocery),
            contentDescription = "Recover password Image",
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
                    color = Color.White,
                    shape = RoundedCornerShape(
                        topStartPercent = 8,
                        topEndPercent = 8
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, bottom = 50.dp),
                    ) {
                        Text(
                            text = "Reset password",
                            style = MaterialTheme.typography.h4.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )

                        Text(
                            text = "Please enter your email address",
                            style = MaterialTheme.typography.h6.copy(
                                color = MaterialTheme.colors.primary
                            )
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TransparentTextField(
                                singleLine = true ,
                                textFieldValue = viewModel.emailValue,
                                textLabel = "Email",
                                keyboardType = KeyboardType.Text,
                                keyboardActions = KeyboardActions(
                                    onNext = {}
                                ),
                                imeAction = ImeAction.Next
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            if (viewModel.state.value.successRecovered == false){
                                RoundedButton(
                                    text = "Recover",
                                    displayProgressBar = viewModel.state.value.displayPb,
                                    onClick = {
                                        viewModel.recoveryPassword(viewModel.emailValue.value)
                                    }
                                )
                            }


                            if (viewModel.state.value.successRecovered == true){

                                Text(
                                    text = "Success , Email send",
                                    style = MaterialTheme.typography.h6.copy(
                                        color = Color.Green
                                    )
                                )

                            }
                        }
                    }
                }
            }

            if (viewModel.state.value.errorMsg != null){EventDialog(errorMessage = viewModel.state.value.errorMsg, onDismiss = {viewModel.hideErrorDialog()}) }
        }
    }

}