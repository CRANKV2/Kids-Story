import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.gigo.kidsstorys.data.SettingsManager
import com.gigo.kidsstorys.data.models.ChatMessage
import com.gigo.kidsstorys.ui.theme.AccentPurple

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager.getInstance(context) }
    val cardAlpha = settingsManager.cardAlpha.collectAsState(initial = 0.75f)
    val alpha = remember { Animatable(0f) }
    
    LaunchedEffect(message) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .graphicsLayer(alpha = alpha.value)
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (message.isUser) 
                AccentPurple.copy(alpha = cardAlpha.value) 
            else 
                Color(0xFF42424E).copy(alpha = cardAlpha.value),
            modifier = Modifier
                .align(if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart)
                .widthIn(max = 280.dp)
                .padding(end = if (message.isUser) 0.dp else 64.dp)
                .padding(start = if (message.isUser) 64.dp else 0.dp)
                .shadow(
                    elevation = 8.dp * cardAlpha.value,
                    spotColor = AccentPurple.copy(alpha = cardAlpha.value),
                    ambientColor = AccentPurple.copy(alpha = cardAlpha.value),
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(12.dp),
                color = Color.White
            )
        }
    }
} 