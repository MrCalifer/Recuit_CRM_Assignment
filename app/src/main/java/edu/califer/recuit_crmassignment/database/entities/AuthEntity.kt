package edu.califer.recuit_crmassignment.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auth_table")
data class AuthEntity(

    @PrimaryKey var email: String = "",
    @ColumnInfo(name = "password") var password: String= ""
)