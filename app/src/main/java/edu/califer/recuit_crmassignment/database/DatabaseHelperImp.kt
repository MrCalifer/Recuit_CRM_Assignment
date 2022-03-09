package edu.califer.recuit_crmassignment.database.entities

import edu.califer.recuit_crmassignment.database.CompanyDB
import edu.califer.recuit_crmassignment.database.DatabaseHelper

class DatabaseHelperImpl(private val companyDB: CompanyDB) : DatabaseHelper {
    override suspend fun insertAuth(authEntity: AuthEntity) {
        companyDB.authDao().insertDao(authEntity)
    }

    override suspend fun verifyEmail(email: String): AuthEntity? =
        companyDB.authDao().getAuthByEmail(email)

    override suspend fun getAllUser(): List<AuthEntity> =
        companyDB.authDao().getAllAuth()
}