import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.gigo.kidsstorys.data.models.ChatMessage
import com.gigo.kidsstorys.ui.theme.AccentPurple

@Composable
fun ChatMessageItem(message: ChatMessage) {
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .graphicsLayer(alpha = alpha.value)
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (message.isUser) AccentPurple else Color(0xFF42424E),
            modifier = Modifier
                .align(if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart)
                .widthIn(max = 280.dp)
                .padding(end = if (message.isUser) 0.dp else 64.dp)
                .padding(start = if (message.isUser) 64.dp else 0.dp)
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(12.dp),
                color = Color.White
            )
        }
    }
} 