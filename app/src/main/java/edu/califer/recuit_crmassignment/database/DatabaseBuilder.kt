package edu.califer.recuit_crmassignment.database

import android.content.Context
import androidx.room.Room

object DatabaseBuilder {
    private var instance: CompanyDB? = null

    fun getInstance(context: Context): CompanyDB {
        if (instance == null) {
            synchronized(CompanyDB::class) {
                instance = buildDB(context)
            }
        }
        return instance!!
    }

    private fun buildDB(context: Context): CompanyDB {
        return Room.databaseBuilder(
            context.applicationContext,
            CompanyDB::class.java,
            "my-world-db"
        ).build()
    }
}