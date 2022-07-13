package com.example.expensetracker

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.expensetracker.database.expense.Expense
import com.example.expensetracker.databinding.FragmentAddEditBinding
import com.example.expensetracker.domain.usecase.*
import com.example.expensetracker.viewmodels.ExpenseViewModel
import com.example.expensetracker.viewmodels.ExpenseViewModelFactory
import kotlinx.android.synthetic.main.expense_view.*
import kotlinx.android.synthetic.main.fragment_add_edit.*
import java.util.Observer
import javax.inject.Inject


class AddEdit : Fragment(), DatePickerDialog.OnDateSetListener{



    private val navigationArgs: AddEditArgs by navArgs()

    @Inject
    lateinit var expenseViewModelFactory: ExpenseViewModelFactory



    private val viewModel: ExpenseViewModel by activityViewModels {
        expenseViewModelFactory
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
        binding?.apply {
            editTitle.setText(expense.expenseTitle)
            editExpense.setText(expense.expense.toString())
            editCategory.setText(expense.category)
            editWhen.setText(expense.`when`)
            editDone.text = getString(R.string.update)
        }
    }



    private fun addNewExpense() {


        if (isEntryValid()) {
            binding?.run {

                viewModel.addNewExpense(
                    editTitle.text.toString(),
                    editExpense.text.toString(),
                    editWhen.text.toString(),
                    editCategory.text.toString()
                )
            }

            val action = AddEditDirections.actionAddEditToListDisplay()
            findNavController().navigate(action)

        }
    }

    private fun updateExpense() {


        if (isEntryValid()) {
            binding?.run {
            val exp = navigationArgs.expense?.copy(
                expenseTitle = editTitle.text.toString(),
                expense = editExpense.text.toString().toInt(),
                `when` = editWhen.text.toString(),
                category = editCategory.text.toString()

            )
                exp?.let { viewModel.updateExpense(exp) }

            }

            val action = AddEditDirections.actionAddEditToListDisplay()
            findNavController().navigate(action)

        }
    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


            super.onViewCreated(view, savedInstanceState)
            navigationArgs.expense?.let {

                bind(it)


            }
            binding?.editWhen?.setOnClickListener{
                val newFragment = DatePickerFragment()
                newFragment.setDateChangeListener(this)
                newFragment.show(childFragmentManager, "datePicker")
            }
            binding?.editDone?.setOnClickListener{
                if(navigationArgs.expense == null) {
                    addNewExpense()
                }else {
                    updateExpense()

                }

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

    override fun onDateSet(view: DatePicker?, year: Int,month: Int, day: Int) {
        binding?.editWhen?.setText( "$day/$month/$year")

    }


}


