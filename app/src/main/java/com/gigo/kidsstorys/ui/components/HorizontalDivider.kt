package com.gigo.testapp.ui.components

import androidx.compose.material3.Divider
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

@Composable
fun HorizontalDivider(color: Color) {
    Divider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = color
    )
} 