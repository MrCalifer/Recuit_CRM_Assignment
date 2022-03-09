package edu.califer.recuit_crmassignment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import edu.califer.recuit_crmassignment.ViewModels.BaseViewModel
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

        HelperClass.loginSharedPreference(requireContext())

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        /**Setting the Status Bar Icon Color.*/
        viewModel.statusBarIconColor(1, requireActivity())

        setAnimation()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if (HelperClass.getUserLoggedIn(requireContext())) {
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
            }
        }, 3000)
    }

    /**
     * Function to set animation for the splash screen
     */
    private fun setAnimation() {
        val splashVideoAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        splashVideoAnimation.reset()
        binding.splashAnimation.clearAnimation()
        binding.splashAnimation.startAnimation(splashVideoAnimation)
    }
}