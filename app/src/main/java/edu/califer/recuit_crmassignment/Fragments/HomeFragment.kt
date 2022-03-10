package edu.califer.recuit_crmassignment.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.califer.recuit_crmassignment.Adapter.CompanyAdapter
import edu.califer.recuit_crmassignment.R
import edu.califer.recuit_crmassignment.ViewModels.BaseViewModel
import edu.califer.recuit_crmassignment.ViewModels.CompanyViewModel
import edu.califer.recuit_crmassignment.databinding.FragmentHomeBinding

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
            ViewModelProvider(this@HomeFragment)[CompanyViewModel::class.java]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_home, container , false)
        binding.lifecycleOwner = this

        baseViewModel.statusBarIconColor(1 , requireActivity())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCompaniesList()
    }

    override fun onResume() {
        super.onResume()

        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addCompaniesFragment)
        }

        viewModel.listOfCompanies.observe(viewLifecycleOwner){
            if (it == null){
                viewModel.getCompaniesList()
            }else if (it.isNotEmpty()){
                binding.companyRecyclerView.visibility = View.VISIBLE
                binding.noElement.visibility = View.GONE
                binding.companyRecyclerView.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = CompanyAdapter(it)
                }
            }
        }
    }
}