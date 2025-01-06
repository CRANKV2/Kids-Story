package com.gigo.kidsstorys.ui.components

import androidx.compose.material3.Divider
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

@Composable
fun HorizontalDivider(color: Color) {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = color
    )
} 