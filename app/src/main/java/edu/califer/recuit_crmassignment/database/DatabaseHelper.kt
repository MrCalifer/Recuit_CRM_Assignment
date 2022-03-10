package edu.califer.recuit_crmassignment.database

import edu.califer.recuit_crmassignment.database.entities.AuthEntity
import edu.califer.recuit_crmassignment.database.entities.CompanyEntity

interface DatabaseHelper {

    suspend fun insertAuth(authEntity: AuthEntity)
    suspend fun verifyEmail(email: String): AuthEntity?
    suspend fun getAllUser():List<AuthEntity>

    suspend fun insertCompany(companyEntity: CompanyEntity)
    suspend fun getAllCompany():List<CompanyEntity>
    suspend fun deleteCompany(companyEntity: String)
}