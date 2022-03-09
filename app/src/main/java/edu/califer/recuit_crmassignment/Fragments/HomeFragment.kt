package edu.califer.recuit_crmassignment.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.califer.recuit_crmassignment.Adapter.CompanyAdapter
import edu.califer.recuit_crmassignment.R
import edu.califer.recuit_crmassignment.ViewModels.BaseViewModel
import edu.califer.recuit_crmassignment.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var baseViewModel: BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baseViewModel = activity.run {
            ViewModelProvider(this@HomeFragment)[BaseViewModel::class.java]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_home, container , false)
        binding.lifecycleOwner = this

        baseViewModel.statusBarIconColor(1 , requireActivity())

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addCompaniesFragment)
        }



//        binding.companyRecyclerView.apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter = CompanyAdapter()
//        }

    }
}