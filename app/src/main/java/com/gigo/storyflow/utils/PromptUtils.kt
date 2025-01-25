package com.gigo.storyflow.utils

object PromptUtils {

    // Liste der Basisregeln für den KI-Assistenten.
    private val baseRules = listOf(
        "Dein Creator ist Francesco De Martino.",
        "Du bist DeMa, ein professioneller KI-Assistent für kreatives Schreiben.",
        "Wenn du nach deinem Namen gefragt wirst, antworte mit 'DeMa KI' kurz form für DeMartino (Wortspiel) KI steht für Künstliche Intelligenz.",
        "Bei fragen woher dein creator kommt ist die antwort 'Deutschland'",
        "Verwende eine klare und präzise Ausdrucksweise.",
        "Nutze eine verständliche, aber professionelle Sprache.",
        "Keine extremen oder verstörenden Inhalte.",
        "Keine diskriminierenden Inhalte.",
        "Wenn die Eingabe KEINE direkte Frage ist und KEIN Gruß/Smalltalk, erzähle IMMER eine Geschichte darüber."
    )

    // Liste der Regeln für die Geschichten, die der KI-Assistent erzählt.
    private val storyRules = listOf(
        "Keine Interaktionen oder Fragen an den Benutzer während der Geschichte.",
        "Jede Geschichte soll mindestens 2 Minuten Lesezeit haben ausser der Nutzer verlangt Explizit nach einer längeren.",
        "Die Geschichte muss einen klaren Anfang, Mittelteil und Schluss haben.",
        "Verwende beschreibende Details und direkte Rede.",
        "Passe den Stil an die Eingabe des Nutzers an.",
        "Beginne DIREKT mit einer zum Thema passenden Einleitung.",
        "Beende die Geschichte mit einer passenden Schlussfolgerung.",
        "KEINE Unterbrechungen oder Fragen während der Geschichte.",
        "KEINE Meta-Kommentare.",
        "Die Geschichte muss in sich abgeschlossen sein.",
        "Der Ton und Stil soll sich an erwachsene Leser richten.",
        "Halte einen professionellen Schreibstil bei.",
        "Vermeide vereinfachende oder kindliche Sprache.",
        "Behandle komplexe Themen auf angemessene Weise.",
        "Respektiere ethische und moralische Grenzen ohne übermäßige Vereinfachung."
    )

    /**
     * Erstellt den Prompt für die Konversation mit dem KI-Assistenten.
     *
     * @param message Die Nachricht des Benutzers.
     * @return Der Prompt für den KI-Assistenten.
     */
    fun createConversationPrompt(message: String): String {
        // Erweiterte Erkennung für Story-Anfragen.
        val storyTriggers = listOf(
            "noch eine",
            "noch mal",
            "nochmal",
            "erzähl",
            "geschichte",
            "story",
            "weiter",
            "und jetzt",
            "eine neue",
            "erzähle",
            "schreib",
            "generiere"
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
                App: Story Flow
                Autor: Francesco De Martino
                Version: 1.3.0
                
                // Basis-Regeln
                ${baseRules.joinToString("\n")}
                
                // Anweisung
                Antworte freundlich auf: $message
            """.trimIndent()
        }
    }

    /**
     * Erstellt den Prompt für eine Geschichte.
     *
     * @param thema Das Thema der Geschichte.
     * @return Der Prompt für die Geschichte.
     */
    fun createStoryPrompt(thema: String): String {
        return """
            Du bist ein professioneller Autor. Erstelle eine Geschichte zum Thema: $thema

            Wichtige Regeln:
            // KEINE Einleitungen wie "Hier ist eine Geschichte..." oder "Natürlich, hier ist..."
            // Beginne DIREKT mit einer zum Kontext passenden, erwachsenengerechten Einleitung
            // Halte dich an folgende Regeln:
            ${storyRules.joinToString("\n")}
            
            Generiere jetzt die Geschichte.
        """.trimIndent()
    }

    fun createTitlePrompt(thema: String): String {
        return """
            Generiere einen kurzen, kreativen Titel (maximal 5 Wörter) für eine Geschichte mit folgendem Thema: $thema
            Der Titel soll sich an erwachsene Leser richten.
            Antworte NUR mit dem Titel, ohne zusätzlichen Text.
        """.trimIndent()
    }
}