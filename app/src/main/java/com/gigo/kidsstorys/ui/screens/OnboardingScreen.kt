import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigo.kidsstorys.R
import com.gigo.kidsstorys.ui.theme.AccentPurple
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onComplete: () -> Unit
) {
    val pages = listOf(
        OnboardingPage(
            title = "Willkommen bei Kids Storys",
            description = "Erschaffe magische Geschichten für Kinder und lass deiner Kreativität freien Lauf.",
            image = R.drawable.ic_books,
            backgroundColor = Color(0xFF2D2D3A)
        ),
        OnboardingPage(
            title = "KI-Unterstützung",
            description = "Nutze unseren KI-Assistenten, um neue Geschichten zu generieren oder bestehende zu verbessern.",
            image = R.drawable.ic_ai,
            backgroundColor = Color(0xFF353545)
        ),
        OnboardingPage(
            title = "Personalisierung",
            description = "Passe Schriftgröße, Farben und Layout ganz nach deinen Wünschen an.",
            image = R.drawable.ic_customize,
            backgroundColor = Color(0xFF42424E)
        )
    )

    val pagerState = rememberPagerState { pages.size }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { position ->
            OnboardingPageContent(
                page = pages[position],
                onNext = {
                    if (position < pages.size - 1) {
                        scope.launch {
                            pagerState.animateScrollToPage(position + 1)
                        }
                    } else {
                        onComplete()
                    }
                }
            )
        }

        // Dots Indicator
        Row(
            Modifier
                .height(50.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pages.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) {
                    AccentPurple
                } else {
                    AccentPurple.copy(alpha = 0.5f)
                }
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }

        // Skip Button
        TextButton(
            onClick = onComplete,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text(
                "Überspringen",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun OnboardingPageContent(
    page: OnboardingPage,
    onNext: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(page.backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = page.image),
                contentDescription = null,
                modifier = Modifier.size(160.dp),
                tint = Color.Unspecified
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = page.title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = page.description,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }

        Button(
            onClick = onNext,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp)
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    spotColor = AccentPurple,
                    shape = RoundedCornerShape(16.dp)
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentPurple
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                "Weiter",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

private data class OnboardingPage(
    val title: String,
    val description: String,
    @DrawableRes val image: Int,
    val backgroundColor: Color
) 