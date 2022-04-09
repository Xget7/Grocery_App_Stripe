package lol.xget.groceryapp.login.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun TransparentTextField(
    singleLine : Boolean = false,
    modifier: Modifier = Modifier,
    textFieldValue: MutableState<String>,
    textLabel: String,
    maxChar: Int? = null,
    readOnly: Boolean = false,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    keyboardType: KeyboardType,
    keyboardActions: KeyboardActions,
    imeAction: ImeAction,
    trailingIcon: @Composable() (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    TextField(

        singleLine = singleLine,
        modifier = modifier.fillMaxWidth(),
        value = textFieldValue.value.take(maxChar ?: 40),
        onValueChange = { textFieldValue.value = it },
        label = {
            Text(text = textLabel, style = TextStyle(
                color = MaterialTheme.colors.primaryVariant
            ))
        },
        readOnly = readOnly,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            capitalization = capitalization,
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            textColor = MaterialTheme.colors.primaryVariant,


        )
    )
}