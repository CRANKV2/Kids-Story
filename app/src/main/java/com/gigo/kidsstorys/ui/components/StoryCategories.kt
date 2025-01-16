package com.gigo.kidsstorys.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gigo.kidsstorys.ui.theme.AccentPurple
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.ui.text.style.TextAlign

data class StoryCategory(
    val name: String,
    val examples: List<String>
)

@Composable
fun StoryCategoryDropdown(
    onCategorySelected: (List<String>) -> Unit,
    hasBackground: Boolean
) {
    val categories = listOf(
        StoryCategory(
            "Magische Abenteuer",
            listOf(
                "Erzähle von einem kleinen Drachen, der Angst vor Feuer hat",
                "Eine Geschichte über eine Hexenschule für Anfänger",
                "Das magische Spielzeug, das nachts zum Leben erwacht",
                "Der vergessliche Zauberer und sein sprechender Hut",
                "Eine Fee, die keine Flügel mag",
                "Der mutige Kobold im Zaubergarten",
                "Der sprechende Baum im verzauberten Wald",
                "Die Reise zum Zentrum der Erde mit magischen Tieren",
                "Der unsichtbare Freund und seine Abenteuer",
                "Das verzauberte Haustier, das sprechen kann",
                "Die magische Schatzsuche im eigenen Garten"
            )
        ),
        StoryCategory(
            "Tiergeschichten",
            listOf(
                "Der Pinguin, der schwimmen lernen möchte",
                "Eine Geschichte über einen tanzenden Elefanten",
                "Das schüchterne Löwenbaby auf Freundschaftssuche",
                "Die Maus, die Katzen das Fürchten lehrt",
                "Ein Hund und sein Abenteuer im Hundepark",
                "Die weise alte Schildkröte und ihre Geschichten",
                "Der kleine Vogel, der das Fliegen lernt",
                "Die Katze, die in den Zoo ausbüxt",
                "Der Hamster, der ein Rockstar werden will",
                "Das Pony, das sich verlaufen hat",
                "Der Goldfisch, der sich etwas wünscht"
            )
        ),
        StoryCategory(
            "Weltraum & Fantasie",
            listOf(
                "Der kleine Astronaut auf seiner ersten Mondreise",
                "Eine Geschichte über einen freundlichen Alien",
                "Das Raumschiff mit den bunten Sternenlichtern",
                "Abenteuer auf dem Regenbogenplaneten",
                "Der verlorene Satellit findet neue Freunde",
                "Eine Reise durch die Milchstraße",
                "Die Sternenfee und das Geheimnis des Kometen",
                "Der Mondhase und seine Abenteuer im Weltall",
                "Die Suche nach dem verlorenen Planeten",
                "Der Roboter, der Freunde sucht",
                "Picknick im Weltall"
            )
        ),
        StoryCategory(
            "Alltagsabenteuer",
            listOf(
                "Der erste Schultag eines mutigen Kindes",
                "Eine Geschichte über das Zähneputzen mit Spaß",
                "Das Abenteuer beim Gemüse probieren",
                "Ein regnerischer Tag wird zum Spielparadies",
                "Die aufregende Reise mit dem Schulbus",
                "Der geheimnisvolle Dachboden",
                "Der Besuch beim Arzt macht Spaß",
                "Zelten im Garten mit Freunden",
                "Ein Ausflug in den Zoo",
                "Backen mit Oma",
                "Verloren im Supermarkt"
            )
        ),
        StoryCategory(
            "Märchen & Fantasie",
            listOf(
                "Die Prinzessin, die lieber Ritter sein wollte",
                "Ein modernes Märchen vom sprechenden Handy",
                "Der Drache, der Blumen züchtet",
                "Eine Geschichte über einen verträumten Ritter",
                "Die Meerjungfrau in der Badewanne",
                "Der Zauberspiegel im Kinderzimmer",
                "Der Prinz, der Angst im Dunkeln hat",
                "Die sprechenden Tiere im Wald",
                "Das fliegende Bett",
                "Der Junge, der mit Tieren sprechen kann",
                "Die magische Reise in ein Bilderbuch"
            )
        )
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<StoryCategory?>(null) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopCenter)
    ) {
        Surface(
            onClick = { expanded = true },
            shape = MaterialTheme.shapes.medium,
            color = if (hasBackground) {
                AccentPurple.copy(alpha = 0.75f)
            } else {
                AccentPurple
            },
            modifier = Modifier
                .width(280.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    selectedCategory?.name ?: "Wähle eine Beispiel Kategorie...",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dropdown Pfeil",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(280.dp)
                .background(
                    if (hasBackground) {
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                )
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(
                            category.name,
                            color = AccentPurple,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    onClick = {
                        selectedCategory = category
                        expanded = false
                        onCategorySelected(category.examples)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
} 