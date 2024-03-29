package lol.xget.groceryapp.auth.login.presentation.components

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EventDialog(
    modifier: Modifier = Modifier,
    @SuppressLint("SupportAnnotationUsage") @StringRes errorMessage: String?,
    onDismiss: (() -> Unit)? = null
) {
    AlertDialog(
        modifier = modifier
            .padding(16.dp),
        onDismissRequest = { onDismiss?.invoke() },
        title = {
            Text(
                "Error",
                style = TextStyle(
                    color = Color.Red,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        },
        text = {
            errorMessage?.let {
                Text(
                    text = it,
                    style = TextStyle(
                        color =MaterialTheme.colors.primaryVariant,
                        fontSize = 16.sp
                    )
                )
            }

        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { onDismiss?.invoke() }) {
                    Text(text = "Accept", style = MaterialTheme.typography.button)
                }
            }
        },
        backgroundColor = MaterialTheme.colors.background
    )
}