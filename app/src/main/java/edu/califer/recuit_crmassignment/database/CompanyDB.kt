package edu.califer.recuit_crmassignment.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.califer.recuit_crmassignment.database.dao.AuthDao
import edu.califer.recuit_crmassignment.database.entities.AuthEntity

@Database(
    entities = [AuthEntity::class],
    version = 1
)
abstract class CompanyDB : RoomDatabase() {
    abstract fun authDao(): AuthDao
}