package lol.xget.groceryapp.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color(0xFF1A090D),
    background = Color(0xFFFCFFFB),
    onBackground = Color(0xFFFF5C4D),
    surface = Color(0xFFA8BA9A),
    onSurface = Color(0xFF6B6570)
)

private val ColorPalette = lightColors(
    primary = Color(0xFF000000),
    primaryVariant = Color(0xFFFFFFFF),
    background = Color(0xFF15142c),
    onBackground = Color(0xFF6CD5C8),
    surface = Color(0xFFDEFFEE),
    onSurface = Color(0xFF20577C),
    onSecondary = Color(0xFF4CAF50)
)

@Composable
fun ModalBottomSheetTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        ColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Composable
fun GroceryAppTheme(darkTheme: Boolean = true, content : @Composable() () -> Unit) {
    MaterialTheme(
        colors = ColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}