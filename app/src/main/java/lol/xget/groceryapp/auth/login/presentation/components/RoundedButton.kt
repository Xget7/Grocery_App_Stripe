package lol.xget.groceryapp.auth.login.presentation.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RoundedButton(
    modifier: Modifier = Modifier,
    text: String,
    displayProgressBar: Boolean = false,
    progressBarColor: Color = Color.Black,
    color : Color = Color.Black,
    textColor : Color = Color.White,
    textFz : TextUnit = 15.sp,
    onClick: () -> Unit
) {
    if(!displayProgressBar) {
        Button(
            modifier = modifier
                .width(250.dp)
                .height(50.dp),
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = color,
            ),
            shape = RoundedCornerShape(50),
            elevation = ButtonDefaults.elevation(6.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.h6.copy(
                    color = textColor
                ),
                fontSize = textFz
            )
        }
    } else {
        CircularProgressIndicator(
            modifier = Modifier.size(50.dp),
            color = progressBarColor,
            strokeWidth = 6.dp
        )
    }
}