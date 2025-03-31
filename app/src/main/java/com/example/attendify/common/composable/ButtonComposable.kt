package com.example.attendify.common.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.attendify.common.ext.customButton

@Composable
fun CustomButton(
    text: String,
    modifier: Modifier = Modifier,
    action: () -> Unit,
    isLoadingIcon: Boolean = false
) {
    Button(
        onClick = action,
        modifier = modifier.customButton(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEC8484), contentColor = Color.Black),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = text)
            Spacer(Modifier.width(4.dp))
            if (isLoadingIcon) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp
                )
            }
        }
    }
}

@Composable
fun CustomTextButton(text: String, modifier: Modifier = Modifier, action: () -> Unit) {
    TextButton(
        onClick = action
    ) {
        Text(text = text)
    }
}


@Composable
fun CustomIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector? = null,
    painter: Painter? = null,
    contentDescription: String? = null,
    size: Dp = 28.dp,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        when {
            imageVector != null -> {
                Icon(
                    imageVector = imageVector,
                    contentDescription = contentDescription.toString(),
                    modifier = modifier.size(size)
                )
            }

            painter != null -> {
                Image(
                    painter = painter,
                    contentDescription = contentDescription.toString(),
                    modifier = modifier.size(size)
                )
            }
        }
    }
}
