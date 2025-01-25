package com.gigo.storyflow.ui.screens.legal

import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gigo.storyflow.ui.screens.about.components.AboutTopBar
import com.gigo.storyflow.ui.screens.about.components.AnimatedBackground
import com.gigo.storyflow.ui.theme.AccentPurple
import com.gigo.storyflow.ui.theme.TextLight

@Composable
fun TermsScreen(
    navController: NavController,
    cardAlpha: Float = 0.75f
) {
    val context = LocalContext.current

    val annotatedTermsText = buildAnnotatedString {
        withStyle(SpanStyle(color = AccentPurple, fontWeight = FontWeight.Bold)) {
            append("Story Flow - Nutzungsbedingungen\n\n")
        }
        append("1. Nutzungsalter\n")
        append("Die Nutzung dieser App ist für Personen ab 12 Jahren vorgesehen.\n\n")
        
        append("2. Nutzungsumfang\n")
        append("• Die App dient dem kreativen Schreiben mit KI-Unterstützung\n")
        append("• Alle erstellten Inhalte unterliegen der Verantwortung des Nutzers\n")
        append("• Die KI-Funktionen sind als Unterstützung gedacht\n\n")
        
        append("3. Haftungsausschluss\n")
        append("• Keine Garantie für die Verfügbarkeit der KI-Dienste\n")
        append("• Keine Haftung für generierte Inhalte\n")
        append("• Keine Gewährleistung für die Speicherung von Inhalten\n\n")
        
        append("4. Urheberrecht\n")
        append("• Alle durch Nutzer erstellten Inhalte bleiben deren geistiges Eigentum\n")
        append("• Die App-Funktionen dürfen nicht missbräuchlich verwendet werden\n\n")
        
        append("5. Datenschutz\n")
        append("• Siehe separate Datenschutzerklärung\n")
        append("• Minimale Datenerfassung für App-Funktionalität\n")
        append("• Keine Weitergabe an Dritte\n\n")
        
        append("6. Änderungen\n")
        append("Wir behalten uns vor, diese Bedingungen bei Bedarf anzupassen.\n")
    }

    val annotatedPrivacyText = buildAnnotatedString {
        withStyle(SpanStyle(color = AccentPurple, fontWeight = FontWeight.Bold)) {
            append("Datenschutzerklärung\n\n")
        }
        append("Wir respektieren Ihre Privatsphäre und verpflichten uns zu ihrem Schutz durch die Einhaltung dieser Datenschutzerklärung (\"Richtlinie\"). Diese Richtlinie beschreibt die Arten von Informationen, die wir von Ihnen in der \"Story Flow\" Mobile-Anwendung erfassen oder die Sie bereitstellen können (\"Persönliche Informationen\"), sowie unsere Praktiken für deren Erfassung, Verwendung, Pflege, Schutz und Offenlegung.\n\n")
        
        withStyle(SpanStyle(color = AccentPurple, fontWeight = FontWeight.Bold)) {
            append("Rechtlich bindende Vereinbarung\n\n")
        }
        append("Diese Richtlinie ist eine rechtlich bindende Vereinbarung zwischen Ihnen (\"Nutzer\") und STRP. Mit der Nutzung der App erklären Sie sich mit dieser Richtlinie einverstanden.\n\n")
        
        withStyle(SpanStyle(color = AccentPurple, fontWeight = FontWeight.Bold)) {
            append("Datenerfassung\n\n")
        }
        append("Unsere oberste Priorität ist die Datensicherheit. Wir verfolgen eine \"No-Logs-Policy\" und verarbeiten nur die minimal notwendigen Nutzerdaten für den Betrieb der App. Automatisch erfasste Informationen dienen ausschließlich der Missbrauchserkennung und statistischen Zwecken.\n\n")
        
        withStyle(SpanStyle(color = AccentPurple, fontWeight = FontWeight.Bold)) {
            append("Datenverarbeitung\n\n")
        }
        append("Wir agieren als Datenverantwortlicher und Datenverarbeiter. Die Verarbeitung Ihrer Daten erfolgt nur im notwendigen Umfang für:\n\n")
        append("""
            • Produkt- und Service-Updates
            • Support und Kundenbetreuung
            • Nutzerfeedback
            • Verbesserung der Nutzererfahrung
            • Durchsetzung unserer Richtlinien
            • Schutz vor Missbrauch
            • Erfüllung rechtlicher Anforderungen
            • Betrieb der App und Services
        """.trimIndent() + "\n\n")
        
        withStyle(SpanStyle(color = AccentPurple, fontWeight = FontWeight.Bold)) {
            append("Datenweitergabe\n\n")
        }
        append("Wir geben keine persönlichen Informationen an nicht verbundene Dritte weiter. Dienstleister erhalten nur die für ihre Funktion notwendigen Informationen.\n\n")
        
        withStyle(SpanStyle(color = AccentPurple, fontWeight = FontWeight.Bold)) {
            append("Datenspeicherung\n\n")
        }
        append("Wir speichern Ihre Daten nur so lange wie nötig. Nach Ablauf der Aufbewahrungsfrist werden die Daten gelöscht.\n\n")
        
        withStyle(SpanStyle(color = AccentPurple, fontWeight = FontWeight.Bold)) {
            append("Analytik\n\n")
        }
        append("Unsere App nutzt möglicherweise Analyse-Tools zur Erfassung standardmäßiger Nutzungsinformationen. Diese Tools sammeln keine persönlich identifizierbaren Informationen.\n\n")
        
        withStyle(SpanStyle(color = AccentPurple, fontWeight = FontWeight.Bold)) {
            append("Kinderschutz\n\n")
        }
        append("Wir erfassen wissentlich keine Daten von Kindern unter 13 Jahren. Eltern und Erziehungsberechtigte sollten die Internet-Nutzung ihrer Kinder überwachen.\n\n")
        
        withStyle(SpanStyle(color = AccentPurple, fontWeight = FontWeight.Bold)) {
            append("Push-Benachrichtigungen\n\n")
        }
        append("Sie können Push-Benachrichtigungen jederzeit aktivieren oder deaktivieren. Die verwendeten Geräte-Token erlauben keine persönliche Identifizierung.\n\n")
        
        withStyle(SpanStyle(color = AccentPurple, fontWeight = FontWeight.Bold)) {
            append("Datensicherheit\n\n")
        }
        append("Wir schützen Ihre Daten durch technische und organisatorische Maßnahmen. Eine absolute Sicherheit bei Internet-Übertragungen kann jedoch nicht garantiert werden.\n\n")
        
        withStyle(SpanStyle(color = AccentPurple, fontWeight = FontWeight.Bold)) {
            append("Änderungen\n\n")
        }
        append("Wir behalten uns vor, diese Richtlinie bei Bedarf zu aktualisieren. Änderungen werden in der App bekannt gegeben.\n\n")
        
        withStyle(SpanStyle(color = AccentPurple, fontWeight = FontWeight.Bold)) {
            append("Kontakt\n\n")
        }
        append("Bei Fragen zum Datenschutz:\n")
        pushStringAnnotation(
            tag = "EMAIL",
            annotation = "mailto:stratosphere.performance@gmail.com"
        )
        withStyle(SpanStyle(
            color = AccentPurple,
            textDecoration = TextDecoration.Underline
        )) {
            append("stratosphere.performance@gmail.com")
        }
        pop()
        append("\n\n")
        append("Stand: 25. Januar 2025")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedBackground()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                AboutTopBar(
                    navController = navController,
                    cardAlpha = cardAlpha,
                    title = "Rechtliche Hinweise"
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .padding(top = 0.dp, bottom = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = cardAlpha),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = annotatedTermsText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextLight,
                            textAlign = TextAlign.Start
                        )
                        
                        Spacer(modifier = Modifier.height(32.dp))

                        @Suppress("DEPRECATION")
                        ClickableText(
                            text = annotatedPrivacyText,
                            style = TextStyle(
                                color = TextLight,
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                textAlign = TextAlign.Start
                            ),
                            onClick = { offset ->
                                annotatedPrivacyText.getStringAnnotations(
                                    tag = "EMAIL",
                                    start = offset,
                                    end = offset
                                ).firstOrNull()?.let {
                                    val deviceInfo = """
                                        |Sehr geehrtes Story Flow Team,
                                        |
                                        |- Beschreiben Sie hier Ihr Anliegen -
                                        |
                                        |------------------
                                        |Geräte-Informationen:
                                        |Gerät: ${Build.MANUFACTURER} ${Build.MODEL}
                                        |Android Version: ${Build.VERSION.RELEASE}
                                        |App Version: ${context.packageManager.getPackageInfo(context.packageName, 0).versionName}
                                    """.trimMargin()

                                    val emailIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "message/rfc822"
                                        putExtra(Intent.EXTRA_EMAIL, arrayOf("stratosphere.performance@gmail.com"))
                                        putExtra(Intent.EXTRA_SUBJECT, "Frage zum Datenschutz - Story Flow App")
                                        putExtra(Intent.EXTRA_TEXT, deviceInfo)
                                    }
                                    
                                    try {
                                        context.startActivity(Intent.createChooser(emailIntent, "E-Mail senden"))
                                    } catch (e: Exception) {
                                        // Handle den Fall, dass keine E-Mail-App installiert ist
                                        Toast.makeText(
                                            context,
                                            "Keine E-Mail-App gefunden",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
