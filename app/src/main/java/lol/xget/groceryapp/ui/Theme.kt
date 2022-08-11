package lol.xget.groceryapp.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color(0xFF000000),
    primaryVariant = Color(0xFFFFFFFF),
    background = Color(0xFF15142c),
    onBackground = Color(0xFF6CD5C8),
    surface = Color(0xFFDEFFEE),
    onSurface = Color(0xFF20577C),
    onSecondary = Color(0xFF4CAF50)
)

private val ColorPalette = lightColors(
    primary = Color(0xFF000000),
    primaryVariant = Color(0xFF000000),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF5BA56D),
    surface = Color(0xFF3E86C5),
    onSurface = Color(0xFFF57373),
    onSecondary = Color(0xFFD54848)
)



@Composable
fun GroceryAppTheme(darkTheme: Boolean = true, content : @Composable() () -> Unit) {
    MaterialTheme(
        colors = ColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}