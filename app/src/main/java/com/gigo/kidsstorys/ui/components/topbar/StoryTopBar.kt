package com.gigo.kidsstorys.ui.components.topbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.gigo.kidsstorys.R
import com.gigo.kidsstorys.ui.theme.AccentPurple

@Composable
fun StoryTopBar(
    onBack: () -> Unit,
    onEdit: () -> Unit,
    cardAlpha: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 8.dp * cardAlpha,
                shape = RoundedCornerShape(24.dp),
                ambientColor = AccentPurple.copy(alpha = cardAlpha),
                spotColor = AccentPurple.copy(alpha = cardAlpha)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2D2D3A).copy(alpha = cardAlpha)
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Zurück",
                    tint = Color.White
                )
            }
            
            Text(
                "Geschichte", 
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
            
            IconButton(onClick = onEdit) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "Bearbeiten",
                    tint = Color.White
                )
            }
        }
    }
}