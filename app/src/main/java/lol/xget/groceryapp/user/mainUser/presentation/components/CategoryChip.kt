package lol.xget.groceryapp.user.mainUser.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lol.xget.groceryapp.ui.raleway

@Composable
fun CategoryChip(
    category : String,
    isSelected: Boolean = false,
    onSelectedCategoryChange: (String) -> Unit,
    onExecuteSearch : () -> Unit,
) {


    Surface(
        modifier = Modifier.padding(end = 8.dp),
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colors.background
//        color = if (isSelected) Color.LightGray else MaterialTheme.colors.onSecondary
    ) {
        Row(
            modifier = Modifier.toggleable(
                value = isSelected,
                onValueChange = {
                    onSelectedCategoryChange(category)
                    onExecuteSearch()
                })
        ) {

            Text(
                text = category,
                style = TextStyle(fontFamily = raleway,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color(0xFF4CAF50) else Color.Black,
                    fontSize = 16.sp),
                modifier=  Modifier.drawBehind {
                    drawLine(
                        color = if (isSelected) Color(0xFF4CAF50) else Color.Transparent,
                        start = Offset(this.center.x / 2, 110f),
                        end = Offset(this.center.x + this.center.x - (this.center.x / 2),110f),
                        strokeWidth = 45f,
                    )
                }.padding(8.dp)
            )
        }

    }
    
}