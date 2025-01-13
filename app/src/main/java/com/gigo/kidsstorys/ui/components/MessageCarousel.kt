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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.gigo.kidsstorys.ui.theme.AccentPurple
import com.gigo.kidsstorys.ui.theme.TextLight
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.gigo.kidsstorys.data.SettingsManager

@Composable
fun MessageCarousel(
    examples: List<String>,
    onMessageSelected: (String) -> Unit,
    hasBackground: Boolean
) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager.getInstance(context) }
    val cardAlpha = settingsManager.cardAlpha.collectAsState(initial = 0.75f)

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
                        elevation = 8.dp * cardAlpha.value,
                        spotColor = AccentPurple.copy(alpha = cardAlpha.value),
                        ambientColor = AccentPurple.copy(alpha = cardAlpha.value),
                        shape = RoundedCornerShape(16.dp)
                    ),
                color = Color(0xFF2D2D3A).copy(alpha = cardAlpha.value),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = example,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextLight.copy(alpha = maxOf(cardAlpha.value + 0.2f, 1f)),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
} 