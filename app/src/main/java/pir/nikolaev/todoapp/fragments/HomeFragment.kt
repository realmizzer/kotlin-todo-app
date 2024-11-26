package pir.nikolaev.todoapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import pir.nikolaev.todoapp.R
import pir.nikolaev.todoapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), AddTodoDialogFragment.IAddTodoDialogListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding
    private lateinit var addTodoDialog: AddTodoDialogFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()
    }

    private fun init(view: View) {

        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference
            .child("Tasks")
            .child(auth.currentUser?.uid.toString())
        navController = Navigation.findNavController(view)

    }

    private fun registerEvents() {
        binding.btnAdd.setOnClickListener {
            addTodoDialog = AddTodoDialogFragment()
            addTodoDialog.setListener(this)
            addTodoDialog.show(
                childFragmentManager,
                "AddTodoDialogFragment"
            )
        }
    }

    override fun onSaveTask(text: String, et: TextInputEditText) {
        databaseRef.push().setValue(text).addOnCompleteListener {
            if (!it.isSuccessful) {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_LONG).show()
                return@addOnCompleteListener
            }

            Toast.makeText(context, R.string.add_todo_success, Toast.LENGTH_SHORT).show()
        }
    }

}