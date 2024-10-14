package pir.nikolaev.todoapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pir.nikolaev.todoapp.databinding.FragmentAddTodoDialogBinding

class AddTodoDialogFragment : Fragment() {

    private lateinit var binding: FragmentAddTodoDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddTodoDialogBinding.inflate(inflater, container, false)
        return binding.root

    }



}