package edu.califer.recuit_crmassignment.Repositories

import android.app.Application
import android.util.Log
import edu.califer.recuit_crmassignment.database.DatabaseBuilder
import edu.califer.recuit_crmassignment.database.entities.AuthEntity
import edu.califer.recuit_crmassignment.database.entities.CompanyEntity
import edu.califer.recuit_crmassignment.database.entities.DatabaseHelperImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class CompanyRepository(application: Application) {

    private val TAG = "CompanyRepository"
    private val dbHelper: DatabaseHelperImpl =
        DatabaseHelperImpl(DatabaseBuilder.getInstance(application))


    /**Function to insert user data in DB when the user Signs In: */
    suspend fun insertCompany(companyEntity: CompanyEntity): CompanyEntity {
        withContext(Dispatchers.IO) {
            val res = kotlin.runCatching {
                dbHelper.insertCompany(companyEntity)
            }

            res.onSuccess {
                Log.d("AuthRepository", "Insertion Successful.")
            }

            res.onFailure {
                Log.e("AuthRepository", "Insertion Failed due to ${it}.")
            }
        }
        return companyEntity
    }

    /**
     * Function to fetch all companies list and return it.
     */
    suspend fun getCompaniesList() : List<CompanyEntity>{
        return dbHelper.getAllCompany()
    }

}