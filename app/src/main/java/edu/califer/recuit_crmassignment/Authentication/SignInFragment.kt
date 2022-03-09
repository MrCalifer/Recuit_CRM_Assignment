package edu.califer.recuit_crmassignment.Authentication

import android.annotation.SuppressLint
import android.app.Dialog
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
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import edu.califer.recuit_crmassignment.Utils.HelperClass
import edu.califer.recuit_crmassignment.R
import edu.califer.recuit_crmassignment.ViewModels.AuthViewModel
import edu.califer.recuit_crmassignment.activity.MainActivity
import edu.califer.recuit_crmassignment.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    lateinit var binding: FragmentSignInBinding
    lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel = activity?.run {
            ViewModelProvider(this@SignInFragment)[AuthViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        (activity as MainActivity).viewModel.statusBarIconColor(0, requireActivity())

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        authViewModel.getAllUser()

        authViewModel.isEmailRegistered.observe(viewLifecycleOwner) {
            if (it != null && it == AuthViewModel.EMAIL_IS_REGISTERED) {
                binding.password.visibility = View.VISIBLE
                binding.nextButton.text = "Next"
            } else if (it != null && it == AuthViewModel.EMAIL_IS_NOT_REGISTERED) {
                hideKeyboard()
                setUpDialog(AuthViewModel.EMAIL_IS_NOT_REGISTERED)
            }
        }

        authViewModel.isRegistrationCompleted.observe(viewLifecycleOwner) {
            if (it) {
                setUpDialog(AuthViewModel.REGISTRATION_COMPLETED)
                binding.nextButton.isEnabled = true
                binding.username.isEnabled = true
                binding.password.editText!!.setText("")
                binding.password.isEnabled = true
                binding.confirmPassword.isEnabled = true
                binding.confirmPassword.editText!!.setText("")

                authViewModel.isRegistrationCompleted.value = false
            }
        }

        authViewModel.isCredentialVerified.observe(viewLifecycleOwner) {
            if (it == AuthViewModel.CREDENTIAL_VERIFICATION_SUCCESS) {
                requireActivity().runOnUiThread {
                    if (findNavController().currentDestination?.id == R.id.signInFragment) {
                        HelperClass.setUserLoggedIn(requireContext(), true)
                        findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
                    }
                }
            } else if (it == AuthViewModel.CREDENTIAL_VERIFICATION_FAILED) {
                binding.password.setErrorTextColor(ColorStateList.valueOf(Color.RED))
                binding.password.error = "Password Incorrect!"
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.password.error = null
                }, 2000)
            }
        }

        binding.Login.setOnClickListener {
            binding.gettingStarted.text = "Lets get started with your account."
            binding.password.visibility = View.GONE
            binding.confirmPassword.visibility = View.GONE
            binding.username.isEnabled = true

            binding.nextButton.visibility = View.VISIBLE
            binding.nextButton.text = "Continue"

            binding.Login.visibility = View.GONE
            binding.loginText.visibility = View.GONE
        }

        binding.nextButton.setOnClickListener {
            if (binding.nextButton.text == "Next") {

                hideKeyboard()

                if (validateEmail()) {
                    if (binding.password.editText?.text != null && binding.password.editText!!.text.isNotBlank()) {
                        authViewModel.verifyCredentials(
                            email = binding.username.editText!!.text.toString().trim(),
                            password = binding.password.editText!!.text.toString().trim()
                        )

                    } else {
                        binding.password.setErrorTextColor(ColorStateList.valueOf(Color.RED))
                        binding.password.error = "Password cannot be empty!!"
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.password.error = null
                        }, 2000)
                    }
                }
            } else if (binding.nextButton.text == "Register") {

                if (binding.username.editText!!.text.toString().isNotEmpty()
                    && Patterns.EMAIL_ADDRESS.matcher(binding.username.editText?.text.toString())
                        .matches()
                ) {
                    if (binding.password.editText!!.text.toString().isNotEmpty()
                        && binding.password.editText!!.text.toString().trim().length > 5
                    ) {
                        if (binding.confirmPassword.editText!!.text.toString().isNotEmpty()) {
                            verifyPassword(
                                binding.password.editText!!.text.toString().trim(),
                                binding.confirmPassword.editText!!.text.toString().trim()
                            )
                        } else {
                            binding.confirmPassword.setErrorTextColor(ColorStateList.valueOf(Color.RED))
                            binding.confirmPassword.error = "Password cannot be empty!!"
                            Handler(Looper.getMainLooper()).postDelayed({
                                binding.confirmPassword.error = null
                            }, 2000)
                        }
                    } else {
                        binding.password.setErrorTextColor(ColorStateList.valueOf(Color.RED))
                        binding.password.error =
                            "Invalid Password!! . Password must be of 6 Character."
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.password.error = null
                        }, 2000)
                        binding.confirmPassword.editText?.text = null
                    }
                }


            } else {
                validateEmail()
            }
        }
    }

    /**
     * Function to verify password while registering the user.
     */
    private fun verifyPassword(password: String, confirm_password: String) {
        if (password == confirm_password) {
            binding.password.isEnabled = false
            binding.password.editText?.setTextColor(Color.WHITE)

            binding.confirmPassword.isEnabled = false
            binding.confirmPassword.editText?.setTextColor(Color.WHITE)

            binding.nextButton.isEnabled = false
            binding.nextButton.setTextColor(Color.WHITE)

            binding.Login.isEnabled = false
            binding.Login.setTextColor(Color.WHITE)

            binding.authProgressbar.visibility = View.VISIBLE

            authViewModel.registerUser(
                binding.username.editText!!.text.toString().trim(),
                binding.confirmPassword.editText!!.text.toString().trim()
            )
        } else {
            binding.confirmPassword.setErrorTextColor(ColorStateList.valueOf(Color.RED))
            binding.confirmPassword.error = "Password mismatch!!"
            Handler(Looper.getMainLooper()).postDelayed({
                binding.confirmPassword.error = null
            }, 2000)
        }
        hideKeyboard()
    }

    /**
     * Function to validate the email.
     */
    @SuppressLint("SetTextI18n")
    private fun validateEmail(): Boolean {
        var isValid: Boolean

        isValid = (authViewModel.isEmailRegistered.value != null
                && authViewModel.isEmailRegistered.value == AuthViewModel.EMAIL_IS_REGISTERED)

        //Checking if the email is in correct syntax
        if (binding.username.editText!!.text.toString().isNotEmpty()
            && Patterns.EMAIL_ADDRESS.matcher(binding.username.editText?.text.toString())
                .matches()
        ) {
            binding.username.error = null
            //Checking if the email is already registered
            authViewModel.isEmailRegistered(
                email = binding.username.editText!!.text.toString().trim()
            )
        } else {
            binding.password.editText!!.text.clear()
            binding.password.visibility = View.GONE
            binding.username.setErrorTextColor(ColorStateList.valueOf(Color.RED))
            binding.username.error = "Invalid Email Address!!"
            Handler(Looper.getMainLooper()).postDelayed({
                binding.username.error = null
            }, 2000)
            binding.nextButton.text = "Continue"
            isValid = false
        }

        return isValid
    }

    /**
     * Function to setup dialog.
     */
    @SuppressLint("SetTextI18n")
    private fun setUpDialog(isUserRegistered: Int) {
        val dialog = Dialog(binding.root.context)
        dialog.setContentView(R.layout.registration_dialog)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val icon = dialog.findViewById<ImageView>(R.id.icon_message)
        val message = dialog.findViewById<TextView>(R.id.message)
        val continueButton = dialog.findViewById<Button>(R.id.ok)

        when (isUserRegistered) {
            AuthViewModel.EMAIL_IS_NOT_REGISTERED -> {
                icon.setImageResource(R.drawable.ic_error)
                message.text = "User Not Registered.\n Kindly Register."
                continueButton.text = "Register"
                continueButton.visibility = View.VISIBLE
                binding.password.visibility = View.GONE
                dialog.show()
                dialog.setCanceledOnTouchOutside(false)
            }
            AuthViewModel.EMAIL_IS_REGISTERED -> {
                icon.setImageResource(R.drawable.ic_tick)
                message.text = "Verification Completed. Taking you to main screen"
                continueButton.text = "Next"
                continueButton.visibility = View.VISIBLE
                dialog.show()
                dialog.setCanceledOnTouchOutside(false)
            }
            AuthViewModel.INVALID_CREDENTIAL -> {
                icon.setImageResource(R.drawable.ic_error)
                message.text = "Invalid Credential !! \n Try Again."
                continueButton.visibility = View.GONE
                dialog.show()
                dialog.setCanceledOnTouchOutside(false)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.nextButton.text = "Next"
                    dialog.dismiss()
                }, 1000)
            }
            AuthViewModel.REGISTRATION_COMPLETED -> {
                icon.setImageResource(R.drawable.ic_tick)
                message.text = "Registration Completed. Taking you to main screen"
                continueButton.text = "Next"
                continueButton.visibility = View.VISIBLE
                dialog.show()
                dialog.setCanceledOnTouchOutside(false)

                binding.authProgressbar.visibility = View.GONE
                binding.gettingStarted.text = "Lets get started with your account."
                binding.password.visibility = View.GONE
                binding.confirmPassword.visibility = View.GONE
                binding.username.isEnabled = true

                binding.nextButton.text = "Continue"
                binding.Login.visibility = View.GONE
                binding.loginText.visibility = View.GONE
            }
        }

        continueButton.setOnClickListener {
            dialog.dismiss()
            if (continueButton.text == "Register") {
                binding.gettingStarted.text = "Let's get your account register with us."
                binding.password.visibility = View.VISIBLE
                binding.password.editText?.setText("")
                binding.confirmPassword.editText?.setText("")
                binding.confirmPassword.visibility = View.VISIBLE
                binding.nextButton.text = "Register"

                binding.Login.visibility = View.VISIBLE
                binding.loginText.visibility = View.VISIBLE
                binding.Login.isEnabled = true

                binding.username.isEnabled = false
                binding.username.editText?.setTextColor(Color.WHITE)
                binding.password.isFocusable = true

            } else {
                binding.gettingStarted.text = "Lets get started with your account."
                binding.password.visibility = View.GONE
                binding.confirmPassword.visibility = View.GONE
                binding.username.isEnabled = true
            }
        }
    }

    private fun hideKeyboard() {
        //Hide soft input keyboard
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

}