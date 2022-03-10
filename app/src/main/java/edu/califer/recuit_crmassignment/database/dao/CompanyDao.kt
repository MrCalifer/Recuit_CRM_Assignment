package edu.califer.recuit_crmassignment.database.dao

import androidx.room.*
import edu.califer.recuit_crmassignment.Utils.Company
import edu.califer.recuit_crmassignment.database.entities.CompanyEntity

@Dao
interface CompanyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDao(companyEntity: CompanyEntity)

    @Query("SELECT * FROM company_table")
    suspend fun getAllCompanyEntries(): List<CompanyEntity>

    @Query("DELETE FROM company_table WHERE website = :website")
    suspend fun deleteCompany(website: String)

    @Delete
    suspend fun deleteCompany(companyEntity: CompanyEntity)

    @Update(entity = CompanyEntity::class)
    suspend fun updateCompany(companyEntity: CompanyEntity)


}