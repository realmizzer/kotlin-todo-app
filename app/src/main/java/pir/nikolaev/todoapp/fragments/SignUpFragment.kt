package pir.nikolaev.todoapp.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import pir.nikolaev.todoapp.R
import pir.nikolaev.todoapp.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()

    }

    private fun init(view: View) {

        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()

    }

    private fun registerEvents() {

        binding.tvHaveAccount.setOnClickListener {
            navController.navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        binding.buttonSignUp.setOnClickListener {
            toggleProgressBar()

            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val verifyPassword = binding.etVerifyPassword.text.toString().trim()

            if (
                email.isEmpty()
                || password.isEmpty()
                || verifyPassword.isEmpty()) {

                delayProgressBar(1000) {
                    Toast.makeText(context, R.string.some_fields_empty, Toast.LENGTH_SHORT).show()
                }
                return@setOnClickListener
            }

            if (password != verifyPassword) {
                delayProgressBar(1000) {
                    Toast.makeText(context, R.string.password_match_fail, Toast.LENGTH_SHORT).show()
                }
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                toggleProgressBar()

                if (!it.isSuccessful) {
                    Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }

                navController.navigate(R.id.action_signUpFragment_to_homeFragment)
                Toast.makeText(context, R.string.sign_in_success, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun toggleProgressBar() {

        val isProgressBarVisible = binding.progressBar.visibility == View.VISIBLE
        binding.progressBar.visibility = if (isProgressBarVisible) View.GONE else View.VISIBLE
        binding.buttonSignUp.visibility = if (isProgressBarVisible) View.VISIBLE else View.GONE

    }

    private fun delayProgressBar(delay: Long, callback: () -> Unit) {
        Handler(Looper.myLooper()!!).postDelayed({
            toggleProgressBar()
            callback()
        }, delay)
    }

}