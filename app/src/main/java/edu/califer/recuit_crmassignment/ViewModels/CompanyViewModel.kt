package edu.califer.recuit_crmassignment.ViewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import edu.califer.recuit_crmassignment.Repositories.AuthRepository
import edu.califer.recuit_crmassignment.Repositories.CompanyRepository
import edu.califer.recuit_crmassignment.Utils.Company
import edu.califer.recuit_crmassignment.database.entities.AuthEntity
import edu.califer.recuit_crmassignment.database.entities.CompanyEntity
import kotlinx.coroutines.launch

class CompanyViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "AuthViewModel"
    private var companyRepository: CompanyRepository = CompanyRepository(application)

    var listOfCompanies : MutableLiveData<List<CompanyEntity>> = MutableLiveData()
    var companyAdded : MutableLiveData<Boolean> = MutableLiveData(false)
    var company : MutableLiveData<Company> = MutableLiveData()
    var isEdit : MutableLiveData<Boolean> = MutableLiveData(false)
    var isDelete : MutableLiveData<Boolean> = MutableLiveData(false)

    var isCompanyDeleted : MutableLiveData<Boolean> = MutableLiveData(false)

    /**
     * Function to get all the list of companies
     */
    fun getCompaniesList(){
        viewModelScope.launch {
            val  result = kotlin.runCatching {
                companyRepository.getCompaniesList()
            }
            result.onSuccess {
                listOfCompanies.value = it
                Log.d(TAG , "List of companies size is ${it.size}")
            }

            result.onFailure {
                Log.e(TAG , "Failed due to ${it.message}")
            }
        }
    }

    /**
     * Function to insert login credential into database
     */
     fun insertDataInDB(companyEntity: CompanyEntity) {
        viewModelScope.launch {
            val result = kotlin.runCatching {
                companyRepository.insertCompany(companyEntity)
            }

            result.onSuccess {
                companyAdded.value = true
                Log.d(TAG, "Company Inserted in DB")
            }

            result.onFailure {
                Log.d(TAG, "Company Insertion failed ${it.message}")
            }
        }
    }

    /**
     * Function to delete company in database
     */
    fun deleteCompanyInDB(website: String){
        viewModelScope.launch {
            val result = kotlin.runCatching {
                companyRepository.deleteCompany(website)
            }

            result.onSuccess {
                isCompanyDeleted.value = true
                Log.d(TAG , "Company deleted from DB")
            }

            result.onFailure {
                Log.e(TAG , "Failed due to ${it.message}")
            }
        }
    }

}