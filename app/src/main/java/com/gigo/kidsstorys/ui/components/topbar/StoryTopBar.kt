package com.gigo.kidsstorys.ui.components.topbar

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.gigo.kidsstorys.R
import com.gigo.kidsstorys.ui.theme.AccentPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryTopBar(
    onBack: () -> Unit,
    onEdit: () -> Unit,
    cardAlpha: Float
) {
    TopAppBar(
        title = { Text("Geschichte", color = Color.White) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Zurück",
                    tint = Color.White
                )
            }
        },
        actions = {
            IconButton(onClick = onEdit) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "Bearbeiten",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AccentPurple.copy(alpha = cardAlpha),
            navigationIconContentColor = Color.White
        ),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 8.dp * cardAlpha,
                shape = RoundedCornerShape(24.dp),
                ambientColor = AccentPurple.copy(alpha = cardAlpha),
                spotColor = AccentPurple.copy(alpha = cardAlpha)
            )
            .clip(RoundedCornerShape(24.dp))
    )
} 