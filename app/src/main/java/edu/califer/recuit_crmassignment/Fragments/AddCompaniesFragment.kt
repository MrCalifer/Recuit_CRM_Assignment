package edu.califer.recuit_crmassignment.Fragments

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.califer.recuit_crmassignment.R
import edu.califer.recuit_crmassignment.Utils.Company
import edu.califer.recuit_crmassignment.ViewModels.BaseViewModel
import edu.califer.recuit_crmassignment.ViewModels.CompanyViewModel
import edu.califer.recuit_crmassignment.database.entities.CompanyEntity
import edu.califer.recuit_crmassignment.databinding.FragmentAddCompaniesBinding

class AddCompaniesFragment : Fragment() {

    lateinit var binding: FragmentAddCompaniesBinding
    lateinit var baseViewModel: BaseViewModel
    lateinit var viewModel: CompanyViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseViewModel = activity.run {
            ViewModelProvider(this@AddCompaniesFragment)[BaseViewModel::class.java]
        }
        viewModel = activity.run {
            ViewModelProvider(requireActivity())[CompanyViewModel::class.java]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_companies, container, false)
        binding.lifecycleOwner = this

        baseViewModel.statusBarIconColor(1, requireActivity())

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        dropDownAdapter()

        viewModel.companyAdded.observe(viewLifecycleOwner) {
            if (it) {
                requireActivity().onBackPressed()
                viewModel.companyAdded.value = false
            } else {
                binding.addCompanyProgressBar.visibility = View.GONE
                binding.outerLayout.isEnabled = true
            }
        }

        viewModel.isEdit.observe(viewLifecycleOwner) {
            if (it != null && viewModel.company.value != null && it) {
                binding.companyName.editText?.setText(viewModel.company.value!!.name)
                binding.companyWebsite.editText?.setText(viewModel.company.value!!.website)
                binding.companyPhoneNumber.editText?.setText(viewModel.company.value!!.number)
                binding.companyAddress.editText?.setText(viewModel.company.value!!.address)
                binding.companyCity.editText?.setText(viewModel.company.value!!.city)
                binding.companyState.editText?.setText(viewModel.company.value!!.state)
                binding.companyCountry.editText?.setText(viewModel.company.value!!.country)
                when (viewModel.company.value!!.type) {
                    "Account" -> {
                        binding.industryLists.setSelection(1)
                    }
                    "IT" -> {
                        binding.industryLists.setSelection(2)
                    }
                    "Sales" -> {
                        binding.industryLists.setSelection(3)
                    }
                    "Health Care" -> {
                        binding.industryLists.setSelection(4)
                    }
                    else -> {
                        binding.industryLists.setSelection(0)
                    }
                }
                binding.addCompany.text = "Update"
                viewModel.isEdit.value = false
            }
        }

        binding.addCompany.setOnClickListener {
            if (binding.addCompany.text == "Update") {
                if (verifyInputs()) {
                    val company = getCompanyInput()
                    binding.addCompanyProgressBar.visibility = View.VISIBLE
                    binding.outerLayout.isEnabled = false
                    viewModel.updateCompanyInDB(
                        CompanyEntity(
                            id = viewModel.companyID.value!!,
                            companyName = company.name,
                            companyWebsite = company.website,
                            companyPhoneNumber = company.number,
                            companyAddress = company.address,
                            companyCity = company.city,
                            companyState = company.state,
                            companyCountry = company.country,
                            companyType = company.type
                        )
                    )
                }
            } else if (verifyInputs()) {
                val company = getCompanyInput()
                binding.addCompanyProgressBar.visibility = View.VISIBLE
                binding.outerLayout.isEnabled = false
                viewModel.insertDataInDB(
                    CompanyEntity(
                        id = 0,
                        companyName = company.name,
                        companyWebsite = company.website,
                        companyPhoneNumber = company.number,
                        companyAddress = company.address,
                        companyCity = company.city,
                        companyState = company.state,
                        companyCountry = company.country,
                        companyType = company.type
                    )
                )
            }
        }
    }

    /**
     * Function to get Company Inputs
     */
    private fun getCompanyInput(): Company {
        return Company(
            id = viewModel.companyID.value ?: 0,
            name = binding.companyName.editText!!.text.toString().trim(),
            website = binding.companyWebsite.editText!!.text.toString().trim(),
            number = binding.companyPhoneNumber.editText!!.text.toString().trim(),
            address = binding.companyAddress.editText?.text.toString().trim(),
            city = binding.companyCity.editText!!.text.toString().trim(),
            state = binding.companyState.editText!!.text.toString().trim(),
            country = binding.companyCountry.editText!!.text.toString().trim(),
            type = binding.industryLists.selectedItem.toString().trim()
        )
    }

    /**
     * Function to verify each input fields.
     */
    private fun verifyInputs(): Boolean {
        val isValid: Boolean
        if (binding.companyName.editText?.text.toString().isNotBlank()) {
            if (binding.companyWebsite.editText?.text.toString().isNotBlank() &&
                Patterns.WEB_URL.matcher(binding.companyWebsite.editText?.text.toString())
                    .matches()
            ) {
                if (binding.companyPhoneNumber.editText?.text.toString().isNotBlank() &&
                    binding.companyPhoneNumber.editText?.text.toString().trim().length > 9
                ) {
                    if (binding.companyCity.editText?.text.toString().isNotBlank()) {
                        if (binding.companyState.editText?.text.toString().isNotBlank()) {
                            if (binding.companyCountry.editText?.text.toString().isNotBlank()) {
                                if (binding.industryLists.selectedItem.toString() != "Select your Company Type") {
                                    isValid = true
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Industry Type must be selected",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    return false
                                }
                            } else {
                                binding.companyCountry.setErrorTextColor(
                                    ColorStateList.valueOf(
                                        Color.RED
                                    )
                                )
                                binding.companyCountry.error = "Cannot be empty!!"
                                Handler(Looper.getMainLooper()).postDelayed({
                                    binding.companyCountry.error = null
                                }, 2000)
                                return false
                            }
                        } else {
                            binding.companyState.setErrorTextColor(ColorStateList.valueOf(Color.RED))
                            binding.companyState.error = "Cannot be empty!!"
                            Handler(Looper.getMainLooper()).postDelayed({
                                binding.companyState.error = null
                            }, 2000)
                            return false
                        }
                    } else {
                        binding.companyCity.setErrorTextColor(ColorStateList.valueOf(Color.RED))
                        binding.companyCity.error = "Cannot be empty!!"
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.companyCity.error = null
                        }, 2000)
                        return false
                    }
                } else {
                    binding.companyPhoneNumber.setErrorTextColor(ColorStateList.valueOf(Color.RED))
                    binding.companyPhoneNumber.error =
                        "Invalid Number!. Number should be of 10 digits"
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.companyPhoneNumber.error = null
                    }, 2000)
                    return false
                }
            } else {
                binding.companyWebsite.setErrorTextColor(ColorStateList.valueOf(Color.RED))
                binding.companyWebsite.error = "Invalid web address!!"
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.companyWebsite.error = null
                }, 2000)
                return false
            }
        } else {
            binding.companyName.setErrorTextColor(ColorStateList.valueOf(Color.RED))
            binding.companyName.error = "Name cannot be empty!!"
            Handler(Looper.getMainLooper()).postDelayed({
                binding.companyName.error = null
            }, 2000)
            return false
        }
        return isValid
    }

    /**
     * Function to set the Drop down for the industry types.
     */
    private fun dropDownAdapter() {
        val items = listOf("Select your Company Type", "Account", "IT", "Sales", "Health Care")
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        binding.industryLists.adapter = arrayAdapter
    }
}