package pir.nikolaev.todoapp.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pir.nikolaev.todoapp.databinding.TodoCardBinding

class TodoAdapter(private val list: MutableList<TodoData>) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    inner class TodoViewHolder(val binding: TodoCardBinding) : RecyclerView.ViewHolder(binding.root)

    private var listener: ITodoAdapterListener? = null

    fun setListener(listener: ITodoAdapterListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = TodoCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                binding.title.text = this.title.toString()

                binding.btnEdit.setOnClickListener {
                    listener?.onButtonEditCLick(this)
                }

                binding.btnDelete.setOnClickListener {
                    listener?.onButtonDeleteClick(this)
                }
            }
        }
    }

    interface ITodoAdapterListener {
        fun onButtonEditCLick(todo: TodoData)
        fun onButtonDeleteClick(todo: TodoData)
    }
}