package edu.califer.recuit_crmassignment.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {

    lateinit var email: MutableLiveData<String>
    lateinit var password: MutableLiveData<String>


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
}