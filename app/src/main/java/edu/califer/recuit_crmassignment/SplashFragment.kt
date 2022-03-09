package edu.califer.recuit_crmassignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.califer.recuit_crmassignment.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    lateinit var binding: FragmentSplashBinding
    lateinit var viewModel: BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity.run {
            ViewModelProvider(this@SplashFragment)[BaseViewModel::class.java]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        /**Initialing the FragmentHomeBinding.*/
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_splash, container, false
        )
        /**Setting a lifecycleOwner as this Fragment.*/
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        /**Setting the Status Bar Icon Color.*/
        viewModel.statusBarIconColor(0, requireActivity())
    }
}