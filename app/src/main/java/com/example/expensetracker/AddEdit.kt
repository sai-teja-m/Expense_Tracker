package com.example.expensetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.expensetracker.database.expense.Expense
import com.example.expensetracker.databinding.FragmentAddEditBinding
import com.example.expensetracker.domain.usecase.DeleteExpenseUseCase
import com.example.expensetracker.domain.usecase.GetAllUseCase
import com.example.expensetracker.domain.usecase.InsertExpenseUseCase
import com.example.expensetracker.domain.usecase.UpdateExpenseUseCase
import com.example.expensetracker.viewmodels.ExpenseViewModel
import com.example.expensetracker.viewmodels.ExpenseViewModelFactory
import kotlinx.android.synthetic.main.fragment_add_edit.*


class AddEdit : Fragment() {

    private val expenseApplication = activity?.application as ExpenseApplication
    private val insertExpenseUseCase = InsertExpenseUseCase(expenseApplication.expenseRepo)
    private val updateExpenseUseCase = UpdateExpenseUseCase(expenseApplication.expenseRepo)
    private val deleteExpenseUseCase = DeleteExpenseUseCase(expenseApplication.expenseRepo)
    private val getAllUseCase = GetAllUseCase(expenseApplication.expenseRepo)
    private val navigationArgs: AddEditArgs by navArgs()


    private val viewModel: ExpenseViewModel by activityViewModels {
        ExpenseViewModelFactory(
            insertExpenseUseCase,updateExpenseUseCase,deleteExpenseUseCase,getAllUseCase
        )
    }

    private var _binding: FragmentAddEditBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddEditBinding.inflate(inflater, container, false)
        return binding?.root
    }

    private fun bind(expense: Expense) {
        binding.apply {
            edit_title.setText(expense.expenseTitle)
            edit_expense.setText(expense.expense.toString())
            edit_category.setText(expense.category)
            edit_when.setText(expense.`when`)
        }
    }

    private fun addNewExpense() {


        if (isEntryValid()) {
            binding?.run {

                viewModel.addNewExpense(
                    edit_title.text.toString(),
                    edit_expense.text.toString(),
                    edit_category.text.toString(),
                    edit_when.text.toString()
                )
            }

            val action = AddEditDirections.actionAddEditToListDisplay()
//            Log.d("addNew","here")
            findNavController().navigate(action)

        }
    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            navigationArgs.expense?.let {
                bind(it)
                binding?.editDone?.setOnClickListener {

                    binding?.run {
                        viewModel.updateExpense(
                            editTitle.text.toString(),
                            editExpense.text.toString(),
                            editWhen.text.toString(),
                            editCategory.text.toString()
                        )

                    }
                }
            }
            edit_done.setOnClickListener{

                addNewExpense()
            }

        }


        private fun isEntryValid(): Boolean {
            var temp = false
            binding?.run {
                temp =  viewModel.isEntryValid(
                    edit_title.text.toString(),
                    edit_expense.text.toString(),
                    edit_when.text.toString(),
                    edit_category.text.toString(),
                )
            }
            return temp
        }



}


