package pir.nikolaev.todoapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import pir.nikolaev.todoapp.R
import pir.nikolaev.todoapp.databinding.FragmentAddTodoDialogBinding

class AddTodoDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentAddTodoDialogBinding
    private lateinit var listener: IAddTodoDialogListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTodoDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvents()
    }

    private fun registerEvents() {
        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnAddTodo.setOnClickListener {
            val todoText = binding.etTodoTitle.text.toString().trim();

            if (todoText.isEmpty()) {
                Toast.makeText(context, R.string.empty_todo_text, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            listener.onSaveTask(todoText, binding.etTodoTitle)
            dismiss()
        }
    }

    fun setListener(listener: IAddTodoDialogListener) {
        this.listener = listener
    }

    interface IAddTodoDialogListener {
        fun onSaveTask(text: String, et: TextInputEditText)
    }

}