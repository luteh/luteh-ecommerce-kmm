package org.luteh.ecommerce.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun RoundedTextField(
    modifier: Modifier = Modifier,
    valueText: String = "",
    onValueChange: (String) -> Unit = {},
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    labelText: String? = null,
    placeholderText: String = "",
    supportingText: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors()
) {
    var mObscured by remember { mutableStateOf(visualTransformation is PasswordVisualTransformation) }

    Column {
        OutlinedTextField(
            modifier = modifier,
            visualTransformation = if (mObscured) PasswordVisualTransformation() else VisualTransformation.None,
            value = valueText,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(12.dp),
            enabled = enabled,
            label = if (labelText.isNullOrBlank()) null else {
                {
                    Text(text = labelText)
                }
            },
            readOnly = readOnly,
            singleLine = singleLine,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon
                ?: if (visualTransformation is PasswordVisualTransformation) {
                    {

                        val icon =
                            if (mObscured) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility
                        Icon(
                            modifier = Modifier.clickable { mObscured = !mObscured },
                            imageVector = icon,
                            contentDescription = null
                        )
                    }
                } else null,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            placeholder = {
                Text(
                    text = placeholderText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                    )
                )
            },
            supportingText = if (supportingText.isNullOrBlank()) null else {
                {
                    Text(
                        text = supportingText,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            },
            isError = isError,
            colors = colors
        )
    }
}

@Composable
@Preview
private fun RoundedTextFieldPreview() {
    RoundedTextField(
        isError = false,
        supportingText = "supportingText",
        placeholderText = "Placeholder text",
        labelText = "Label text",
        enabled = true,
    )
}