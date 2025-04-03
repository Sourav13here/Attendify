package com.example.attendify.common.composable

import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.attendify.ui.theme.GrayLight

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    enabled: Boolean = true,
    isPasswordField: Boolean = false,
    onNext: (() -> Unit)? = null,
    onDone: (() -> Unit)? = null
) {
    var showPassword by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
//            .background(GrayLight, shape = RoundedCornerShape(8.dp)),  // Background color

        label = { Text(label,color = Color.Black) },
        placeholder = { Text(placeholder?:"",color = Color.Black) },
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
            onDone = { onDone?.invoke() }
        ),
        trailingIcon = {
            if (isPasswordField) {
                val image =
                    if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = image,
                        contentDescription = if (showPassword) "Hide password" else "show password",
                        tint = Color.Black
                    )
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Black,  // Black border
            unfocusedBorderColor = Color.Black,
            cursorColor = Color.Black,
            focusedContainerColor = GrayLight,  // Gray inside the border
            unfocusedContainerColor = GrayLight,
            disabledContainerColor = GrayLight
        ),
        shape = RoundedCornerShape(8.dp)
    )
}