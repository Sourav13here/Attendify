package com.example.attendify.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.attendify.ui.theme.BorderColor
import com.example.attendify.ui.theme.CharcoalBlue
import com.example.attendify.ui.theme.ErrorColor
import com.example.attendify.ui.theme.GrayLight
import com.example.attendify.ui.theme.PrimaryColor
import com.example.attendify.ui.theme.SecondaryColor

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    enabled: Boolean = true,
    isPasswordField: Boolean = false,
    error: String? = null,
    onNext: (() -> Unit)? = null,
    onDone: (() -> Unit)? = null,
    leadingIcon: ImageVector? = null // <-- NEW

) {
    var showPassword by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        modifier = modifier.horizontalScroll(rememberScrollState()),
        onValueChange = onValueChange,
        label = {
            Text(
                label,
                color = if (error != null) ErrorColor else if (value.isNotEmpty()) PrimaryColor else CharcoalBlue
            )
        },
        placeholder = {
            Text(
                placeholder ?: "",
                color = Color(0xFF293241)
            )
        }
        ,
        singleLine = true,
        enabled = enabled,
        visualTransformation = if (isPasswordField && !showPassword)
            PasswordVisualTransformation()
        else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPasswordField) KeyboardType.Password else KeyboardType.Text,
            imeAction = when {
                onNext != null -> ImeAction.Next
                onDone != null -> ImeAction.Done
                else -> ImeAction.Default
            }
        ),
        keyboardActions = KeyboardActions(
            onNext = { onNext?.invoke() },
            onDone = { onDone?.invoke()
                focusManager.clearFocus() // this explicitly closes the keyboard

            }
        ),
        supportingText = {
            if (error != null) {
                Text(error, color = ErrorColor)
            }
        },leadingIcon = {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = if (isFocused) PrimaryColor else CharcoalBlue // Change color on focus
                )
            }
        },
        trailingIcon = {
            if (isPasswordField) {
                val image = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = image,
                        contentDescription = if (showPassword) "Hide password" else "Show password",
                        tint = Color(0xFF293241)
                    )
                }
            }
        },

        isError = error != null,  // <-- Shows error state if error is present

        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (error != null) ErrorColor else SecondaryColor,
            unfocusedBorderColor = if (error != null) ErrorColor else BorderColor,
            cursorColor = Color.Black,
            focusedContainerColor = Color(0xFFFFFFFF),
            unfocusedContainerColor = Color(0xFFFFFFFF),
            disabledContainerColor = Color(0xFFCBD5E1),
            disabledBorderColor = Color(0xFFA0AEC0), // muted gray-blue
            disabledTextColor = Color(0xFF7B8794),   // dull text
            disabledLabelColor = Color(0xFF7B8794),
            disabledPlaceholderColor = Color(0xFF7B8794),
        ),
        shape = RoundedCornerShape(8.dp)
    )
}
