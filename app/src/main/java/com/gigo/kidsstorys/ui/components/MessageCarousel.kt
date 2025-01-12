package com.gigo.kidsstorys.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gigo.kidsstorys.ui.theme.AccentPurple
import com.gigo.kidsstorys.ui.theme.TextLight

@Composable
fun MessageCarousel(
    examples: List<String>,
    onMessageSelected: (String) -> Unit,
    hasBackground: Boolean
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(examples) { example ->
            Surface(
                modifier = Modifier
                    .width(260.dp)
                    .clickable { onMessageSelected(example) }
                    .shadow(
                        elevation = 8.dp,
                        spotColor = AccentPurple,
                        shape = RoundedCornerShape(16.dp)
                    ),
                color = if (hasBackground) {
                    Color(0xFF2D2D3A).copy(alpha = 0.75f)
                } else {
                    Color(0xFF2D2D3A)
                },
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = example,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextLight,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
} 