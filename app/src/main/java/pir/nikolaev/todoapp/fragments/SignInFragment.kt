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
import pir.nikolaev.todoapp.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()

        if (auth.currentUser != null) {
            navController.navigate(R.id.action_signInFragment_to_homeFragment)
        }

    }

    private fun init(view: View) {

        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()

    }

    private fun registerEvents() {

        binding.tvHaveNoAccount.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding.buttonSignIn.setOnClickListener {
            toggleProgressBar()

            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                delayProgressBar(1000) {
                    Toast.makeText(context, R.string.incorrect_email_password, Toast.LENGTH_SHORT).show()
                }
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                toggleProgressBar()

                if (!it.isSuccessful) {
                    Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }

                navController.navigate(R.id.action_signInFragment_to_homeFragment)
                Toast.makeText(context, R.string.sign_in_success, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun toggleProgressBar() {

        val isProgressBarVisible = binding.progressBar.visibility == View.VISIBLE
        binding.progressBar.visibility = if (isProgressBarVisible) View.GONE else View.VISIBLE
        binding.buttonSignIn.visibility = if (isProgressBarVisible) View.VISIBLE else View.GONE

    }

    private fun delayProgressBar(delay: Long, callback: () -> Unit) {
        Handler(Looper.myLooper()!!).postDelayed({
            toggleProgressBar()
            callback()
        }, delay)
    }

}