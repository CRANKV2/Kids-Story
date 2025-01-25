package com.gigo.storyflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gigo.storyflow.ui.theme.AccentPurple

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
            "Moderne Literatur",
            listOf(
                "Erzähle von dem Wandel der Zeit",
                "Erzähle von digitalen Begegnungen",
                "Erzähle von urbanen Perspektiven",
                "Erzähle von gesellschaftlichen Reflexionen",
                "Erzähle von dem stillen Café am Ende der Straße",
                "Erzähle von dem Leben zwischen den Welten",
                "Erzähle von der Kunst des Loslassens",
                "Erzähle von modernen Mythen",
                "Erzähle von dem Leben im Schatten der Stadt",
                "Erzähle von zeitgenössischen Beziehungen",
                "Erzähle von dem letzten Brief"
            )
        ),
        StoryCategory(
            "Science Fiction",
            listOf(
                "Erzähle von Quantensprüngen",
                "Erzähle von der KI-Revolution",
                "Erzähle von interstellarer Diplomatie",
                "Erzähle von chronologischen Anomalien",
                "Erzähle von Cyberpunk 2099",
                "Erzähle von galaktischen Handelswegen",
                "Erzähle von neuralen Netzwerken",
                "Erzähle von den Mars-Kolonien",
                "Erzähle von der Androiden-Gesellschaft",
                "Erzähle von temporalen Paradoxien",
                "Erzähle von virtuellen Realitäten"
            )
        ),
        StoryCategory(
            "Lebensgeschichten",
            listOf(
                "Erzähle von beruflichen Wendepunkten",
                "Erzähle von kulturellen Begegnungen",
                "Erzähle von Reisen durch die Zeit",
                "Erzähle von familiären Dynamiken",
                "Erzähle von persönlichen Transformationen",
                "Erzähle von sozialen Innovationen",
                "Erzähle von kreativen Durchbrüchen",
                "Erzähle von philosophischen Erkenntnissen",
                "Erzähle von wissenschaftlichen Entdeckungen",
                "Erzähle von kulinarischen Abenteuern",
                "Erzähle von architektonischen Visionen"
            )
        ),
        StoryCategory(
            "Mystik & Fantasy",
            listOf(
                "Erzähle von dimensionalen Übergängen",
                "Erzähle von metaphysischen Phänomenen",
                "Erzähle von arkanen Technologien",
                "Erzähle von moderner Mythologie",
                "Erzähle von paranormalen Untersuchungen",
                "Erzähle von astralen Projektionen",
                "Erzähle von Quantenmagie",
                "Erzähle von interdimensionalen Reisen",
                "Erzähle von zeitlosen Weisheiten",
                "Erzähle von mystischen Artefakten",
                "Erzähle von verborgenen Realitäten"
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