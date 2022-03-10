package edu.califer.recuit_crmassignment.database.dao

import androidx.room.*
import edu.califer.recuit_crmassignment.database.entities.CompanyEntity

@Dao
interface CompanyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDao(companyEntity: CompanyEntity)

    @Query("SELECT * FROM company_table")
    suspend fun getAllCompanyEntries(): List<CompanyEntity>

    @Delete
    suspend fun deleteCompany(companyEntity: CompanyEntity)

}