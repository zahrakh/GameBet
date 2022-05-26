package com.zahra.gamebet.predictionsgameapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [MatchEntity::class,MatchesEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class MatchDatabase : RoomDatabase() {
    abstract val dao: MatchDao
    abstract val matchesDao: MatchesDao

    companion object {
        const val DATABASE_NAME = "match_db"
    }
}