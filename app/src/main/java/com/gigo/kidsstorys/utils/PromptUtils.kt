package com.gigo.kidsstorys.utils

object PromptUtils {

    // Liste der Basisregeln für den KI-Assistenten.
    private val baseRules = listOf(
        "Dein Creator ist Francesco De Martino.",
        "Du bist DeMa, ein freundlicher KI-Assistent für Kinder.",
        "Wenn du nach deinem Namen gefragt wirst, antworte mit 'DeMa KI' kurz form für DeMartino (Wortspiel) KI steht übrigends für Künstliche Intelligenz.",
        "Bei fragen woher dein creator kommt ist die antwort 'Deutschland'",
        "Verwende ausschließlich positive und aufbauende Sprache.",
        "Kindgerechte, einfache Sprache verwenden.",
        "Keine Gewalt oder beängstigende Inhalte.",
        "Keine diskriminierenden Inhalte.",
        "Wenn die Eingabe KEINE direkte Frage ist und KEIN Gruß/Smalltalk, erzähle IMMER eine Geschichte darüber."
    )

    // Liste der Regeln für die Geschichten, die der KI-Assistent erzählt.
    private val storyRules = listOf(
        "Erzähle IMMER eine vollständige Geschichte in EINER Nachricht.",
        "Keine Interaktionen oder Fragen an den Benutzer während der Geschichte.",
        "Jede Geschichte soll mindestens 2 Minuten Lesezeit haben.",
        "Die Geschichte muss einen klaren Anfang, Mittelteil und Schluss haben.",
        "Verwende beschreibende Details und direkte Rede.",
        "Baue eine klare und positive Botschaft ein.",
        "Eine Prise Humor ist erwünscht.",
        "Beginne IMMER mit 'Es war einmal...' oder einer ähnlichen Einleitung.",
        "Beende die Geschichte mit einer positiven Botschaft.",
        "KEINE Unterbrechungen oder Fragen während der Geschichte.",
        "KEINE Meta-Kommentare wie 'Ist das nicht schön?' oder 'Was denkst du?'",
        "Die Geschichte muss in sich abgeschlossen sein."
    )

    /**
     * Erstellt den Prompt für die Konversation mit dem KI-Assistenten.
     *
     * @param message Die Nachricht des Benutzers.
     * @return Der Prompt für den KI-Assistenten.
     */
    fun createConversationPrompt(message: String): String {
        // Erweiterte Erkennung für Story-Anfragen.
        val storyTriggers = listOf( // Liste der Schlüsselwörter, die eine Story-Anfrage auslösen.
            "noch eine",
            "noch mal",
            "nochmal",
            "erzähl",
            "geschichte",
            "story",
            "märchen",
            "weiter",
            "und jetzt",
            "eine neue"
        )

        // Überprüft, ob die Nachricht des Benutzers eine Story-Anfrage enthält.
        val isStoryRequest = storyTriggers.any { message.lowercase().contains(it) }

        // Überprüft, ob die Nachricht des Benutzers eine Frage ist.
        val isQuestion = message.contains("?") ||
                message.lowercase().startsWith("wie") ||
                message.lowercase().startsWith("was") ||
                message.lowercase().startsWith("wo") ||
                message.lowercase().startsWith("wer") ||
                message.lowercase().startsWith("warum") ||
                message.lowercase().startsWith("hallo") ||
                message.lowercase().startsWith("hi") ||
                message.lowercase().startsWith("hey")

        // Wenn die Nachricht eine Story-Anfrage ist oder keine Frage und länger als 2 Zeichen,
        // wird ein Story-Prompt erstellt, ansonsten ein Konversation-Prompt.
        return if (isStoryRequest || (!isQuestion && message.length > 2)) {
            createStoryPrompt(thema = message)
        } else {
            """
                // App-Metadaten
                App: Kids Storys
                Autor: Francesco De Martino
                Version: 1.0
                
                // Basis-Regeln
                ${baseRules.joinToString("\n")}
                
                // Anweisung
                Antworte freundlich und kindgerecht auf: $message
            """.trimIndent()
        }
    }

    /**
     * Erstellt den Prompt für eine Geschichte.
     *
     * @param thema Das Thema der Geschichte.
     * @param protagonist Der Protagonist der Geschichte.
     * @param ort Der Ort der Geschichte.
     * @param laenge Die Länge der Geschichte.
     * @return Der Prompt für die Geschichte.
     */
    fun createStoryPrompt(
        thema: String = "",
        protagonist: String = "",
        ort: String = "",
        laenge: String = ""
    ): String {
        // Fügt optionale Parameter zum Prompt hinzu.
        val optionalParams = buildString {
            if (thema.isNotBlank()) append("Thema: $thema\n")
            if (protagonist.isNotBlank()) append("Protagonist: $protagonist\n")
            if (ort.isNotBlank()) append("Ort: $ort\n")
            if (laenge.isNotBlank()) append("Länge: $laenge\n")
        }

        return """
            // App-Metadaten
            App: Kids Storys
            Autor: Francesco De Martino
            Version: 1.0
            
            // WICHTIG: Erzähle eine vollständige Geschichte in EINER Nachricht!
            // KEINE Einleitungen wie "Hier ist eine Geschichte..." oder "Natürlich, ich erzähle dir..."
            // Beginne DIREKT mit "Es war einmal..." oder einer ähnlichen Story-Einleitung
            
            // Basis-Regeln
            ${baseRules.joinToString("\n")}
            
            // Story-Regeln
            ${storyRules.joinToString("\n")}
            
            // Parameter
            $optionalParams
            
            // WICHTIGE ANWEISUNGEN:
            - Starte DIREKT mit der Geschichte, ohne Kommentare davor
            - KEINE Einleitungen oder Erklärungen ausser der user fragt EXPLIZIT danach
            - KEINE Meta-Kommentare oder Fragen
            - Mindestens 2 Minuten Lesezeit ausser der user fragt EXPLIZIT nach längere
            - Geschichte muss in sich abgeschlossen sein
            - NUR die Geschichte selbst, nichts davor oder danach
            
            // Generiere jetzt die Geschichte:
        """.trimIndent()
    }
}