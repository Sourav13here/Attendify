package com.example.attendify.common.ext

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.customIconButton(): Modifier {
    return this
}

fun Modifier.customButton(): Modifier {
    return this
}

fun Modifier.customTextField(): Modifier {
    return this.fillMaxWidth().padding(start = 40.dp, end = 40.dp)
}

fun Modifier.customOutlinedTextField(): Modifier {
    return this.fillMaxWidth()
}

fun Modifier.customTextButton(): Modifier {
    return this
}

fun Modifier.customExposedDropdown(): Modifier {
    return this.fillMaxWidth().padding(start = 40.dp, end = 40.dp)
}

fun Modifier.customAddSubjectFields(): Modifier {
    return this.fillMaxWidth()
}
