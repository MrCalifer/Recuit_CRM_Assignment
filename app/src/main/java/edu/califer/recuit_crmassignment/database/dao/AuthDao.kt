package edu.califer.recuit_crmassignment.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import edu.califer.recuit_crmassignment.database.entities.AuthEntity

@Dao
interface AuthDao {
    @Insert
    suspend fun insertDao(authEntity: AuthEntity)

    @Delete
    suspend fun deleteAuth(authEntity: AuthEntity?)

    @Query("DELETE FROM auth_table")
    suspend fun nukeAuthTable()

    @Query("SELECT * FROM auth_table WHERE email LIKE :email")
    suspend fun getAuthByEmail(email: String): AuthEntity?

    @Query("SELECT * FROM auth_table")
    suspend fun getAllAuth(): List<AuthEntity>

    @Query("UPDATE auth_table SET password = :password WHERE email = :email")
    suspend fun updatePasswordById(email: String, password: String)
}