package pir.nikolaev.todoapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import pir.nikolaev.todoapp.R
import pir.nikolaev.todoapp.databinding.FragmentHomeBinding
import pir.nikolaev.todoapp.utils.TodoAdapter
import pir.nikolaev.todoapp.utils.TodoData

class HomeFragment : Fragment(), AddTodoDialogFragment.IAddTodoDialogListener,
    TodoAdapter.ITodoAdapterListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var mTodoList: MutableList<TodoData>

    private var addTodoDialog: AddTodoDialogFragment? = null

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
        fillTodoRecycler()
        registerEvents()
    }

    private fun init(view: View) {

        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference
            .child("Tasks")
            .child(auth.currentUser?.uid.toString())
        navController = Navigation.findNavController(view)

        binding.recyclerTodo.setHasFixedSize(true)
        binding.recyclerTodo.layoutManager = LinearLayoutManager(context)
        mTodoList = mutableListOf()
        todoAdapter = TodoAdapter(mTodoList)
        todoAdapter.setListener(this)
        binding.recyclerTodo.adapter = todoAdapter

    }

    // Fill the screen list with firebase data
    private fun fillTodoRecycler() {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mTodoList.clear()
                snapshot.children.forEach { taskSnapshot ->
                    val todo = taskSnapshot.key?.let {
                        TodoData(it, taskSnapshot.value.toString())
                    }

                    if (todo != null) {
                        mTodoList.add(todo)
                    }
                }
                todoAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun registerEvents() {
        binding.btnAdd.setOnClickListener {
            clearAddTodoDialog()
            showAddTodoDialog(null)
        }
    }

    private fun showAddTodoDialog(data: TodoData?) {
        addTodoDialog = if (data == null) {
            AddTodoDialogFragment()
        } else {
            AddTodoDialogFragment.newInstance(data)
        }

        addTodoDialog!!.setListener(this)
        addTodoDialog!!.show(
            childFragmentManager,
            AddTodoDialogFragment.TAG
        )
    }

    private fun clearAddTodoDialog() {
        if (addTodoDialog != null)
            childFragmentManager.beginTransaction().remove(addTodoDialog!!).commit()
    }

    // Handle AddTodoDialogFragment
    override fun onSaveTask(text: String, et: TextInputEditText) {
        databaseRef.push().setValue(text).addOnCompleteListener {
            if (!it.isSuccessful) {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_LONG).show()
                return@addOnCompleteListener
            }

            Toast.makeText(context, R.string.add_todo_success, Toast.LENGTH_SHORT).show()
        }
    }

    // Handle AddTodoDialogFragment
    override fun onEditTask(todo: TodoData, et: TextInputEditText) {
        val map = HashMap<String, Any>()
        map[todo.id] = todo.title
        databaseRef.updateChildren(map).addOnCompleteListener {
            if (!it.isSuccessful) {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_LONG).show()
                return@addOnCompleteListener
            }

            Toast.makeText(context, R.string.todo_edit_success, Toast.LENGTH_SHORT).show()
        }
    }

    // Handle TodoAdapter
    override fun onButtonEditCLick(todo: TodoData) {
        clearAddTodoDialog()
        showAddTodoDialog(todo)
    }

    // Handle TodoAdapter
    override fun onButtonDeleteClick(todo: TodoData) {
        databaseRef.child(todo.id).removeValue().addOnCompleteListener {
            if (!it.isSuccessful) {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_LONG).show()
                return@addOnCompleteListener
            }

            Toast.makeText(context, R.string.todo_delete_success, Toast.LENGTH_SHORT).show()
        }
    }

}