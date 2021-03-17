package com.ahmedr.movies_app.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Result::class],
    version = 1
)
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun getMoviesDao(): MoviesDAO

    companion object {
        @Volatile
        private var inctance: MoviesDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = inctance ?: synchronized(LOCK) {
            inctance?:createDatabase(context).also{
                inctance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MoviesDatabase::class.java,
                "movies_db.db"
            ).build()

    }
}