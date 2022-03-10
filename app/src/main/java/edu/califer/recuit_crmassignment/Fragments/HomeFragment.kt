package edu.califer.recuit_crmassignment.Fragments

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.happyplaces.utils.SwipeToDeleteCallback
import edu.califer.recuit_crmassignment.Adapter.CompanyAdapter
import edu.califer.recuit_crmassignment.R
import edu.califer.recuit_crmassignment.Utils.Company
import edu.califer.recuit_crmassignment.Utils.HelperClass
import edu.califer.recuit_crmassignment.ViewModels.BaseViewModel
import edu.califer.recuit_crmassignment.ViewModels.CompanyViewModel
import edu.califer.recuit_crmassignment.database.entities.CompanyEntity
import edu.califer.recuit_crmassignment.databinding.FragmentHomeBinding
import pl.kitek.rvswipetodelete.SwipeToEditCallback


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var baseViewModel: BaseViewModel
    lateinit var viewModel: CompanyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseViewModel = activity.run {
            ViewModelProvider(this@HomeFragment)[BaseViewModel::class.java]
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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this

        baseViewModel.statusBarIconColor(1, requireActivity())

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        viewModel.getCompaniesList()

        binding.logout.setOnClickListener {
            HelperClass.logout(requireContext())
            viewModel.deleteDB()
            findNavController().navigate(R.id.action_homeFragment_to_signInFragment)
        }

        binding.addButton.setOnClickListener {
            viewModel.isEdit.value = false
            viewModel.isDelete.value = false
            viewModel.company.value = null
            findNavController().navigate(R.id.action_homeFragment_to_addCompaniesFragment)
        }

        viewModel.refreshCompanyList.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.getCompaniesList()
            }
        }

        //Swipe To Edit
        val editSwipeHandle = object : SwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.companyRecyclerView.adapter as CompanyAdapter
                adapter.notifyEditItem(viewHolder.adapterPosition)
            }
        }

        val editItemTouchHelper = ItemTouchHelper(editSwipeHandle)
        editItemTouchHelper.attachToRecyclerView(binding.companyRecyclerView)

        //Swipe to Delete
        val deleteSwipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.companyRecyclerView.adapter as CompanyAdapter
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(binding.companyRecyclerView)

        binding.companySearch.setOnSearchClickListener {
            binding.companySearch.queryHint = Html.fromHtml("<font color = #00000>" + "Search Company Name" + "</font>")
        }

        binding.companySearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val adapter = binding.companyRecyclerView.adapter as CompanyAdapter
                filter(newText, adapter)
                return false
            }
        })


        viewModel.listOfCompanies.observe(viewLifecycleOwner) {
            if (it == null) {
                viewModel.getCompaniesList()
            } else if (it.isNotEmpty()) {
                binding.companySearch.visibility = View.VISIBLE
                if (it.size == 1) {
                    if (!HelperClass.isManualShowed(requireContext())) {
                        val dialog = Dialog(binding.root.context)
                        dialog.setContentView(R.layout.manual_dialog)
                        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                        dialog.show()
                        dialog.setCanceledOnTouchOutside(false)
                        Handler(Looper.getMainLooper()).postDelayed({
                            dialog.dismiss()
                        }, 3000)
                        HelperClass.setManualShowed(requireContext(), true)
                    }
                }
                binding.companyRecyclerView.visibility = View.VISIBLE
                binding.noElement.visibility = View.GONE
                binding.companyRecyclerView.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = CompanyAdapter(it, object : CompanyAdapter.CompanyListener {
                        override fun onItemEdit(company: Company): Company {
                            viewModel.company.value = Company(
                                id = company.id,
                                name = company.name,
                                website = company.website,
                                number = company.number,
                                address = company.address,
                                city = company.city,
                                state = company.state,
                                country = company.country,
                                type = company.type
                            )
                            viewModel.companyID.value = company.id
                            viewModel.isEdit.value = true
                            findNavController().navigate(R.id.action_homeFragment_to_addCompaniesFragment)
                            return super.onItemEdit(company)
                        }

                        override fun onItemDelete(company: Company): Company {
                            viewModel.company.value = Company(
                                id = company.id,
                                name = company.name,
                                website = company.website,
                                number = company.number,
                                address = company.address,
                                city = company.city,
                                state = company.state,
                                country = company.country,
                                type = company.type
                            )
                            viewModel.deleteCompanyInDB(
                                CompanyEntity(
                                    id = company.id,
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
                            return super.onItemDelete(company)
                        }
                    })
                }
            } else if (it.isEmpty()) {
                binding.companySearch.visibility = View.GONE
                binding.companyRecyclerView.visibility = View.GONE
                binding.noElement.visibility = View.VISIBLE
            }
        }
    }

    private fun filter(text: String?, adapter: CompanyAdapter) {
        val filteredList: ArrayList<Company> = ArrayList()

        val companyList: ArrayList<Company> = ArrayList()
        for (i in 0 until viewModel.listOfCompanies.value!!.size) {
            companyList.add(
                Company(
                    id = viewModel.listOfCompanies.value!![i].id,
                    name = viewModel.listOfCompanies.value!![i].companyName,
                    website = viewModel.listOfCompanies.value!![i].companyWebsite,
                    number = viewModel.listOfCompanies.value!![i].companyPhoneNumber,
                    address = viewModel.listOfCompanies.value!![i].companyAddress,
                    city = viewModel.listOfCompanies.value!![i].companyCity,
                    state = viewModel.listOfCompanies.value!![i].companyState,
                    country = viewModel.listOfCompanies.value!![i].companyCountry,
                    type = viewModel.listOfCompanies.value!![i].companyType
                )
            )
        }


        for (item in companyList) {
            if (text != null) {
                if (item.name.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item)
                }
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(requireContext(), "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            adapter.filterList(filteredList)
        }
    }
}