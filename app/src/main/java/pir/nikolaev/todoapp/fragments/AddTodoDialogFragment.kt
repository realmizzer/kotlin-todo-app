package pir.nikolaev.todoapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import pir.nikolaev.todoapp.R
import pir.nikolaev.todoapp.databinding.FragmentAddTodoDialogBinding
import pir.nikolaev.todoapp.utils.TodoData

class AddTodoDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentAddTodoDialogBinding
    private lateinit var listener: IAddTodoDialogListener
    private var data: TodoData? = null

    companion object {
        const val TAG = "AddTodoDialogFragment"

        @JvmStatic
        fun newInstance(todo: TodoData) = AddTodoDialogFragment().apply {
            arguments = Bundle().apply {
                putString("id", todo.id)
                putString("title", todo.title)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTodoDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            data = TodoData(
                arguments?.getString("id").toString(),
                arguments?.getString("title").toString()
            )

            binding.etTodoTitle.setText(data?.title)
            binding.imgAddPlus.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_edit
                )
            )
            binding.tvAddText.text = ContextCompat.getString(requireContext(), R.string.edit)
        }

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

            if (data == null) {
                listener.onSaveTask(todoText, binding.etTodoTitle)
            } else {
                data?.title = todoText
                listener.onEditTask(data!!, binding.etTodoTitle)
            }

            dismiss()
        }
    }

    fun setListener(listener: IAddTodoDialogListener) {
        this.listener = listener
    }

    interface IAddTodoDialogListener {
        fun onSaveTask(todo: String, et: TextInputEditText)
        fun onEditTask(todo: TodoData, et: TextInputEditText)
    }

}