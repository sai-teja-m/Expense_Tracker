package com.example.expensetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.database.expense.Expense
import com.example.expensetracker.databinding.FragmentListDisplayBinding
import com.example.expensetracker.domain.usecase.DeleteExpenseUseCase
import com.example.expensetracker.domain.usecase.GetAllUseCase
import com.example.expensetracker.domain.usecase.InsertExpenseUseCase
import com.example.expensetracker.domain.usecase.UpdateExpenseUseCase
import com.example.expensetracker.viewmodels.ExpenseViewModel
import com.example.expensetracker.viewmodels.ExpenseViewModelFactory
import kotlinx.android.synthetic.main.expense_view.*
import kotlinx.android.synthetic.main.fragment_list_display.*

class ListDisplay : Fragment() {

//    private val expenses = arrayListOf<Expense>(
//        Expense(1,"at Barber Shop", 200, "12June", "Needs") ,
//        Expense(2,"Grocery", 500, "2June", "Needs"),
//        Expense(3,"Bus", 150, "9June", "Travel"),
//        Expense( 4,"Tshirt", 600, "10June", "Shopping"),
//        Expense(5,"Shirt & Pant", 2500, "28June", "Shopping")
//    )
    private val expenseApplication = activity?.application as ExpenseApplication
    private val insertExpenseUseCase = InsertExpenseUseCase(expenseApplication.expenseRepo)
    private val updateExpenseUseCase = UpdateExpenseUseCase(expenseApplication.expenseRepo)
    private val deleteExpenseUseCase = DeleteExpenseUseCase(expenseApplication.expenseRepo)
    private val getAllUseCase = GetAllUseCase(expenseApplication.expenseRepo)
    private val expenseAdapter:ExpenseAdapter by lazy { ExpenseAdapter(::onClickEdit) }


    private val viewModel: ExpenseViewModel by activityViewModels {
        ExpenseViewModelFactory(
            insertExpenseUseCase,updateExpenseUseCase,deleteExpenseUseCase,getAllUseCase
        )
    }


    private var _binding: FragmentListDisplayBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.allExpense.observe(viewLifecycleOwner, Observer { expenseAdapter.submitList(it)})
        viewModel.getAll()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListDisplayBinding.inflate(inflater, container, false)

        return binding?.root
    }


//    private var recyclerView: RecyclerView = binding.recyclerViewew

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        if(binding != null) {
//
//            binding.recyclerView = binding?.recyclerView
//            binding.recyclerView.layoutManager = LinearLayoutManager(context)
//            recyclerView.adapter = ExpenseAdapter(expenses)
//        }
        binding?.run{
            recyclerView.layoutManager = LinearLayoutManager(context)

            recyclerView.adapter = expenseAdapter
            add_button.setOnClickListener{
                findNavController().navigate(ListDisplayDirections.actionListDisplayToAddEdit(null ))
            }
        }


    }
    private fun onClickEdit(expense:Expense){
        findNavController().navigate(ListDisplayDirections.actionListDisplayToAddEdit( expense ))
    }


    private fun totalExpense(){

    }

}