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
                "Der mutige Kobold im Zaubergarten"
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
                "Die weise alte Schildkröte und ihre Geschichten"
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
                "Eine Reise durch die Milchstraße"
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
                "Der geheimnisvolle Dachboden"
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
                "Der Zauberspiegel im Kinderzimmer"
            )
        )
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<StoryCategory?>(null) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
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
                .padding(8.dp)
                .widthIn(min = 280.dp)
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
                .widthIn(min = 280.dp)
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