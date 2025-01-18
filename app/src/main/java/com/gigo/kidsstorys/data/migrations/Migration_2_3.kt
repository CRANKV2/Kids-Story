package com.gigo.kidsstorys.data.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Temporäre Tabelle erstellen
        db.execSQL("""
            CREATE TABLE stories_new (
                id INTEGER PRIMARY KEY NOT NULL,
                title TEXT NOT NULL,
                content TEXT NOT NULL,
                imagePath TEXT,
                timestamp INTEGER NOT NULL
            )
        """)

        // Daten kopieren (ohne imagePath, da diese Spalte neu ist)
        db.execSQL("""
            INSERT INTO stories_new (id, title, content, timestamp)
            SELECT id, title, content, createdAt
            FROM stories
        """)

        // Alte Tabelle löschen
        db.execSQL("DROP TABLE stories")

        // Neue Tabelle umbenennen
        db.execSQL("ALTER TABLE stories_new RENAME TO stories")
    }
} 