package lol.xget.groceryapp.seller.mainSeller.presentation.EditProducts

import androidx.compose.runtime.Composable
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import lol.xget.groceryapp.R
import lol.xget.groceryapp.auth.login.presentation.components.EventDialog
import lol.xget.groceryapp.auth.login.presentation.components.RoundedButton
import lol.xget.groceryapp.auth.login.presentation.components.TransparentTextField
import lol.xget.groceryapp.common.Constants
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.ui.components.DialogBoxLoading
import java.util.*


@ExperimentalCoroutinesApi
@Composable
fun EditProductScreen(
    navController: NavController,
    viewModel: EditProductViewModel = hiltViewModel()
) {
    //TODO( fix upload without image image = null)

    val openDialog by viewModel.open.observeAsState(false)


    var productImage by remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            productImage = uri
        }
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }
    val focusManager = LocalFocusManager.current
    var expanded by remember { mutableStateOf(false) }
    var buttomVisible by remember { mutableStateOf(true) }
    val productPhotoFromVm = viewModel.productPhoto.value


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        productImage?.let {
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
                navController.navigate(Destinations.SellerHomeDestinations.route)
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
            text = "Update Product",
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
                        if (bitmap.value != null){
                            bitmap.value?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                contentDescription = "Product Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                                )
                            }
                        }else{
                            GlideImage(
                                imageModel = viewModel.productPhoto.value,
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
                                        modifier = Modifier.width(300.dp).align(Alignment.CenterHorizontally),
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


                        val startDate = Date(System.currentTimeMillis())
                        val timestamp = com.google.firebase.Timestamp(startDate)

                        viewModel.currentProduct.value =
                            lol.xget.groceryapp.seller.mainSeller.domain.ProductModel(
                                productPhoto = if (productImage.toString()
                                        .isNullOrEmpty()
                                ) productPhotoFromVm else productImage.toString(),
                                productId = viewModel.productId.value,
                                productTitle = viewModel.productTitle.value,
                                productCategory = viewModel.productCategory.value,
                                productPrice = viewModel.productPrice.value,
                                productDescription = viewModel.productDescription.value,
                                productQuantity = viewModel.productQuantity.value,
                                discountAvailable = viewModel.productDiscountAvalide.value,
                                discountPrice = viewModel.productDiscountPrice.value,
                                discountNote = viewModel.productDiscountNote.value,
                                uid = viewModel.currentProduct.value.uid,

                                )

                        if (buttomVisible){
                            RoundedButton(
                                text = viewModel.msg.value,
                                displayProgressBar = viewModel.state.value.displayPb
                            ) {
                                Log.e("ProductPhotoFromModel", productImage.toString())
                                viewModel.uploadProductToFb(viewModel.currentProduct.value, productImage)
                            }
                        }else{
                            Text(text = viewModel.msg.value)
                        }


                    }
                }
            }
        }

        if (openDialog) {
            DialogBoxLoading()
        }


        if (viewModel.state.value.errorMsg != null) {
            EventDialog(
                errorMessage = viewModel.state.value.errorMsg,
                onDismiss = { viewModel.hideErrorDialog() })
        }

        if (viewModel.state.value.successUpdated) {
            buttomVisible = false
            if (viewModel.state.value.successUpdated) LaunchedEffect(viewModel.state.value.successUpdated){ navController?.navigate(Destinations.SellerHomeDestinations.route) }


        }

    }
}
