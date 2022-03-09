package edu.califer.recuit_crmassignment.ViewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import edu.califer.recuit_crmassignment.Repositories.AuthRepository
import edu.califer.recuit_crmassignment.database.entities.AuthEntity
import kotlinx.coroutines.launch

open class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "AuthViewModel"

    companion object {
        const val EMAIL_IS_REGISTERED = 1
        const val EMAIL_IS_NOT_REGISTERED = 2
        const val INVALID_CREDENTIAL = 3
        const val REGISTRATION_COMPLETED = 4
    }

    var isEmailRegistered: MutableLiveData<Int> = MutableLiveData(0)

    var isRegistrationCompleted: MutableLiveData<Boolean> = MutableLiveData(false)

    private var authRepository: AuthRepository = AuthRepository(application)


    /**
     * Function returns TRUE if the email address is already registered else returns FALSE.
     * @param email Users Email for verification
     */
    fun isEmailRegistered(email: String): Int? {
        viewModelScope.launch {
            val result = kotlin.runCatching {
                authRepository.verifyEmail(email)
            }
            result.onSuccess {
                if (it != null) {
                    if (it.email == email) {
                        isEmailRegistered.value = EMAIL_IS_REGISTERED
                        Log.d(TAG, "Email is already registered as ${it.email}")
                    }
                } else {
                    isEmailRegistered.value = EMAIL_IS_NOT_REGISTERED
                    Log.d(TAG, "Email is not registered.")
                }
            }
            result.onFailure {
                Log.e(TAG, "Failed due to ${it.message}")
            }
        }
        return isEmailRegistered.value
    }

    /**
     * Function to verify the login credential of the user
     */
    fun verifyCredentials(email: String, password: String): Boolean {
        return true
    }

    /**
     * Function to register new user.
     */
    fun registerUser(email: String, password: String) {
        val authEntity = AuthEntity(email, password)
        insertDataInDB(authEntity = authEntity)
    }


    /**
     * Function to insert login credential into database
     */
    private fun insertDataInDB(authEntity: AuthEntity) {
        viewModelScope.launch {
            val result = kotlin.runCatching {
                authRepository.insertAuth(authEntity)
            }

            result.onSuccess {
                Log.d("DB", "Auth Inserted in DB $it")
                isRegistrationCompleted.value = true
            }

            result.onFailure {
                Log.d("DB", "Auth Inserted failed ${it.message}")
            }
        }
    }

}