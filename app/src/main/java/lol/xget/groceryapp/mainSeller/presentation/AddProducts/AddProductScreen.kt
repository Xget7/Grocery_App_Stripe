package lol.xget.groceryapp.mainSeller.presentation.AddProducts

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import lol.xget.groceryapp.common.Constants
import lol.xget.groceryapp.mainSeller.domain.ProductModel
import lol.xget.groceryapp.domain.util.Screen
import lol.xget.groceryapp.login.presentation.components.EventDialog
import lol.xget.groceryapp.login.presentation.components.RoundedButton
import lol.xget.groceryapp.login.presentation.components.TransparentTextField
import lol.xget.groceryapp.ui.components.DialogBoxLoading
import java.text.DateFormat
import java.text.SimpleDateFormat


@SuppressLint("SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalCoroutinesApi
@Composable
fun AddProductScreen(
    navController: NavController,
    viewModel: AddProductViewModel = hiltViewModel()
) {
    val openDialog by viewModel.open.observeAsState(false)

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
    var expanded by remember { mutableStateOf(false) }
    var buttomVisible by remember { mutableStateOf(true) }

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
                .align(alignment = Alignment.TopCenter)
                .padding(top = 10.dp),
            text = "New Product",
            style = MaterialTheme.typography.h5.copy(
                color = MaterialTheme.colors.primaryVariant
            )
        )


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
                        .padding(13.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier
                            .size(100.dp)
                            .testTag(tag = "circle")
                            .clickable(
                                onClick = { launcher.launch("image/*") }
                            ),
                        shape = CircleShape,
                        elevation = 12.dp
                    ) {

                        bitmap.value?.let { btm ->
                            Image(
                                bitmap = btm.asImageBitmap(),
                                contentDescription = "Product Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        //Add icons to textField

                        TransparentTextField(
                            singleLine = true,
                            textFieldValue = viewModel.productTitle,
                            textLabel = "Title",
                            keyboardType = KeyboardType.Text,
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            imeAction = ImeAction.Next
                        )

                        TransparentTextField(
                            //TODO("At least 30 char")
                            textFieldValue = viewModel.productDescription,
                            textLabel = "Description",
                            maxChar = 200,
                            keyboardType = KeyboardType.Text,
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            imeAction = ImeAction.Next
                        )

                        TransparentTextField(
                            textFieldValue = viewModel.productPrice,
                            textLabel = "Price",
                            keyboardType = KeyboardType.Number,
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                            ),
                            imeAction = ImeAction.Next
                        )

                        TransparentTextField(
                            singleLine = true,
                            textFieldValue = viewModel.productQuantity ,
                            textLabel = "Quantity, kg , g , lb",
                            keyboardType = KeyboardType.Text,
                            keyboardActions = KeyboardActions(

                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                            ),
                            imeAction = ImeAction.Next
                        )

                        TextButton(
                            modifier = Modifier
                                .height(50.dp)
                                .fillMaxWidth(),
                            onClick = {
                                expanded = !expanded
                            },

                        ) {
                            Text(if(viewModel.productCategory.value == ""){
                                "Select Category"
                            }else{
                                viewModel.productCategory.value
                            }, color = MaterialTheme.colors.primaryVariant)
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = null,
                                tint = MaterialTheme.colors.primaryVariant
                            )
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false })
                            {
                                Constants.listOfCategories.forEach { label ->
                                    DropdownMenuItem(
                                        modifier = Modifier
                                            .width(300.dp)
                                            .align(Alignment.CenterHorizontally),
                                        onClick = {
                                        expanded = false
                                        viewModel.productCategory.value = label
                                    }) {
                                        Text(text = label)
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.width(400.dp),
                            horizontalArrangement = Arrangement.spacedBy(240.dp)
                        ) {
                            Text(text = "Discount")
                            Switch(
                                checked = viewModel.productDiscountAvalide.value,
                                onCheckedChange = {
                                    viewModel.productDiscountAvalide.value = it
                                }
                            )
                        }

                        if (viewModel.productDiscountAvalide.value) {
                            viewModel.productDiscountAvalide.value = true
                            TransparentTextField(
                                textFieldValue = viewModel.productDiscountPrice,
                                textLabel = "Discount Price",
                                keyboardType = KeyboardType.Number,
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        focusManager.moveFocus(FocusDirection.Down)
                                    }
                                ),
                                imeAction = ImeAction.Next
                            )

                            TransparentTextField(
                                textFieldValue = viewModel.productDiscountNote,
                                textLabel = "Discount Note e.g 10% off",
                                keyboardType = KeyboardType.Text,
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        focusManager.moveFocus(FocusDirection.Down)
                                    }
                                ),
                                imeAction = ImeAction.Next
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                        val date = java.util.Date()
                        val product = ProductModel(
                            productPhoto = profileImage.toString(),
                            productId = System.currentTimeMillis().toString(),
                            productTitle = viewModel.productTitle.value,
                            productCategory = viewModel.productCategory.value,
                            productPrice = viewModel.productPrice.value,
                            productDescription = viewModel.productDescription.value,
                            productQuantity = viewModel.productQuantity.value,
                            discountAvailable = viewModel.productDiscountAvalide.value,
                            discountPrice = viewModel.productDiscountPrice.value,
                            discountNote = viewModel.productDiscountNote.value,
                            uid = FirebaseAuth.getInstance().uid!!,
                            timestamp = dateFormat.format(date)
                        )

                        if (buttomVisible){
                            RoundedButton(
                                text = viewModel.msg.value,
                                displayProgressBar = viewModel.state.value.displayPb
                            ) {
                                viewModel.uploadProductToFb(product, profileImage)
                            }
                        }else{
                            Text(text = viewModel.msg.value)
                        }


                    }
                }
            }
            if (openDialog) {
                DialogBoxLoading()
            }
            if (viewModel.state.value.displayPb) {
                DialogBoxLoading()
            }

        }



        if (viewModel.state.value.errorMsg != null) {
            EventDialog(
                errorMessage = viewModel.state.value.errorMsg,
                onDismiss = { viewModel.hideErrorDialog() })
        }

        if (viewModel.state.value.successAdded) {
            buttomVisible = false
            viewModel.msg.value = "Sucess"
            if (viewModel.state.value.successAdded) LaunchedEffect(viewModel.state.value.successAdded){ navController?.navigate(Screen.SellerHomeScreen.route) }


        }

    }
}
