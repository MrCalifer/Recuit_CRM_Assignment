package edu.califer.recuit_crmassignment.Authentication

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.califer.recuit_crmassignment.MainActivity
import edu.califer.recuit_crmassignment.R
import edu.califer.recuit_crmassignment.ViewModels.AuthViewModel
import edu.califer.recuit_crmassignment.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    lateinit var binding: FragmentSignInBinding
    lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel = activity.run {
            ViewModelProvider(this@SignInFragment)[AuthViewModel::class.java]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)
        binding.lifecycleOwner = this

        (activity as MainActivity).viewModel.statusBarIconColor(0, requireActivity())

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.nextButton.setOnClickListener {
            if (binding.nextButton.text == "Next"){
                //Hide soft input keyboard
                val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

                if (validateEmail()){
                    if (binding.password.editText?.text != null && binding.password.editText!!.text.isNotBlank()){
                        if (authViewModel.verifyCredentials(email = binding.username.editText!!.text.toString().trim() ,
                            password = binding.password.editText!!.text.toString().trim())){
                            //TODO send user to main screen
                        }else{
                            //TODO if email is not registered then send user to sign-up
                        }
                    }else{
                        binding.password.setErrorTextColor(ColorStateList.valueOf(Color.RED))
                        binding.password.error = "Password cannot be empty!!"
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.password.error = null
                        },2000)
                    }
                }
            }else{
                validateEmail()
            }
        }
    }

    /**
     * Function to validate the email.
     */
    private fun validateEmail() : Boolean{
        val isValid: Boolean
        if (binding.username.editText!!.text.toString().isNotEmpty()
                && Patterns.EMAIL_ADDRESS.matcher(binding.username.editText?.text.toString())
            .matches()
        ) {
            binding.username.error = null
            if (authViewModel.isEmailRegistered(
                    email = binding.username.editText!!.text.toString().trim()
                )
            ) {
                isValid = true
                binding.password.visibility = View.VISIBLE
                binding.nextButton.text = "Next"
            } else {
                isValid = false

                //TODO Show Dialog and send user to Sign Up Page
                authViewModel.email.value = binding.username.editText!!.text.toString().trim()
            }
        } else {
            binding.password.editText!!.text.clear()
            binding.password.visibility = View.GONE
            binding.username.setErrorTextColor(ColorStateList.valueOf(Color.RED))
            binding.username.error = "Invalid Email Address!!"
            Handler(Looper.getMainLooper()).postDelayed({
                binding.username.error = null
            },2000)
            binding.nextButton.text = "Continue"
            isValid = false
        }
        return isValid
    }

}