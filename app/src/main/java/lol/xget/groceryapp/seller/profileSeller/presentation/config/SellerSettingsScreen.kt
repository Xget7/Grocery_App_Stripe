package lol.xget.groceryapp.seller.profileSeller.presentation.config

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import lol.xget.groceryapp.ui.GroceryAppTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SellerSettingsScreen(
    navController: NavController,
    viewModel: SellerSettingsViewModel = hiltViewModel(),
) {
    val sharedPreferences: SharedPreferences =  LocalContext.current.getSharedPreferences("FMC_ENABLED",
        Context.MODE_PRIVATE)
    val sp = sharedPreferences.getBoolean("FMC_ENABLED",false)
    var checked by remember { mutableStateOf(sp) }

    Scaffold(
        topBar = {
            BoxWithConstraints(modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(MaterialTheme.colors.onSecondary)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(onClick = {
                        //back to previous screen
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colors.background
                        )
                    }
                    Spacer(modifier = Modifier.width(90.dp))
                    Text(text = "Settings",
                        color = MaterialTheme.colors.background,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center)
                }
            }
        }
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)
                    .padding(8.dp),
                backgroundColor = MaterialTheme.colors.background,
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(140.dp)
                    ) {
                        Text(text = "Notifications",
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold)
                        Switch(checked = checked, onCheckedChange = {
                            checked = it
                            if (checked) {
                                viewModel.subscribeToNotifications(sharedPreferences.edit())
                            } else {
                                viewModel.unsubscribeToNotifications(sharedPreferences.edit())
                            }
                        }, modifier = Modifier.height(28.dp))
                    }
                    Text(text = if (checked) "Notifications are enabled" else "Notifications are disabled",
                        color = Color.LightGray)
                }
            }

        }


    }
}
