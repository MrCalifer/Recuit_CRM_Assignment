package edu.califer.recuit_crmassignment.Repositories

import android.app.Application
import android.util.Log
import edu.califer.recuit_crmassignment.database.DatabaseBuilder
import edu.califer.recuit_crmassignment.database.entities.AuthEntity
import edu.califer.recuit_crmassignment.database.entities.DatabaseHelperImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(application: Application) {

    private val TAG = "AuthRepository"
    private val dbHelper: DatabaseHelperImpl =
        DatabaseHelperImpl(DatabaseBuilder.getInstance(application))


    /**Function to insert user data in DB when the user Signs In: */
    suspend fun insertAuth(authEntity: AuthEntity): AuthEntity {
        withContext(Dispatchers.IO) {
            val res = kotlin.runCatching {
                dbHelper.insertAuth(authEntity)
            }

            res.onSuccess {
                Log.d("AuthRepository", "Insertion Successful.")
            }

            res.onFailure {
                Log.e("AuthRepository", "Insertion Failed due to ${it}.")
            }
        }
        return authEntity
    }

    /**
     * Function to verify if the email is already registered in the database.
     */
    suspend fun verifyEmail(email: String): AuthEntity? {
        return dbHelper.verifyEmail(email)
    }

    suspend fun getAllUser(): List<AuthEntity> {
        return dbHelper.getAllUser()
    }
}