package edu.califer.recuit_crmassignment.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import edu.califer.recuit_crmassignment.R
import edu.califer.recuit_crmassignment.ViewModels.BaseViewModel
import edu.califer.recuit_crmassignment.databinding.FragmentAddCompaniesBinding

class AddCompaniesFragment : Fragment() {

    lateinit var binding: FragmentAddCompaniesBinding
    lateinit var baseViewModel: BaseViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseViewModel = activity.run {
            ViewModelProvider(this@AddCompaniesFragment)[BaseViewModel::class.java]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_add_companies, container , false)
        binding.lifecycleOwner = this

        baseViewModel.statusBarIconColor(1 , requireActivity())

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        dropDownAdapter()
    }

    private fun dropDownAdapter(){
        val items = listOf("Account", "IT", "Sales", "Health Care")
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        binding.industryLists.setAdapter(arrayAdapter)
    }
}