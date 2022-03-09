package edu.califer.recuit_crmassignment.database

import edu.califer.recuit_crmassignment.database.entities.AuthEntity

interface DatabaseHelper {

    suspend fun insertAuth(authEntity: AuthEntity)
    suspend fun verifyEmail(email: String): AuthEntity?

    suspend fun getAllUser():List<AuthEntity>

}