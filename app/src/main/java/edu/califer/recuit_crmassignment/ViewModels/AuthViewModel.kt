package edu.califer.recuit_crmassignment.ViewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.califer.recuit_crmassignment.Repositories.AuthRepository
import edu.califer.recuit_crmassignment.database.entities.AuthEntity
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : ViewModel() {

    lateinit var email: MutableLiveData<String>
    lateinit var password: MutableLiveData<String>

    private var authRepository: AuthRepository = AuthRepository(application)


    /**
     * Function returns TRUE if the email address is already registered else returns FALSE.
     * @param email Users Email for verification
     */
    fun isEmailRegistered(email:String) :Boolean{
        return true
    }

    /**
     * Function to verify the login credential of the user
     */
    fun verifyCredentials(email: String , password : String) : Boolean{
        return true
    }

    /**
     * Function to register new user.
     */
    fun registerUser(){

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
            }

            result.onFailure {
                Log.d("DB", "Auth Inserted failed ${it.message}")
            }
        }
    }

}