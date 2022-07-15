package com.example.expensetracker

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import androidx.core.widget.doOnTextChanged

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.expensetracker.database.expense.Expense
import com.example.expensetracker.databinding.FragmentAddEditBinding
import com.example.expensetracker.viewmodels.ExpenseViewModel
import com.example.expensetracker.viewmodels.ExpenseViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_add_edit.*
import javax.inject.Inject


class AddEdit : DaggerFragment(), DatePickerDialog.OnDateSetListener {


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (navigationArgs.expense != null)
            inflater.inflate(R.menu.edit_menu, menu)
        else
            super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.delete -> {
                navigationArgs.expense?.let {
                    viewModel.deleteExpense(it)
                    findNavController().navigateUp()
                }
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

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
        binding?.run {
            editTitle.doOnTextChanged { _, _, _, _ ->
                editTitleLabel.isErrorEnabled = false
            }
            editExpense.doOnTextChanged { _, _, _, _ ->
                editExpenseLabel.isErrorEnabled = false
            }
            editCategory.doOnTextChanged { _, _, _, _ ->
                editCategoryLabel.isErrorEnabled = false
            }
            editWhen.doOnTextChanged { _, _, _, _ ->
                editWhenLabel.isErrorEnabled = false
            }
        }
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
        binding?.editWhen?.setOnClickListener {
            val newFragment = DatePickerFragment()
            newFragment.setDateChangeListener(this)
            newFragment.show(childFragmentManager, "datePicker")
        }
        binding?.editDone?.setOnClickListener {
            if (navigationArgs.expense == null) {
                addNewExpense()
            } else {
                updateExpense()

            }

        }

    }

    private fun isEntryValid(): Boolean {
        var temp = false
        val errorMap: MutableMap<Int, Int> = mutableMapOf()
        binding?.run {
            temp = viewModel.isEntryValid(
                edit_title.text.toString(),
                edit_expense.text.toString(),
                edit_when.text.toString(),
                edit_category.text.toString(),
                errorMap
            )
        }
        if (!temp) {
            showErrorMessages(errorMap)
        }
        return temp
    }

    private fun showErrorMessages(errorMap: Map<Int, Int>) {
        binding?.run {

            if (errorMap.containsKey(1)) {
                errorMap[1]?.let {
                    editTitleLabel.isErrorEnabled = true
                    editTitleLabel.error = getString(it)
                }
            }
            if (errorMap.containsKey(2)) {
                errorMap[2]?.let {
                    editExpenseLabel.isErrorEnabled = true
                    editExpenseLabel.error = getString(it) }
            }
            if (errorMap.containsKey(3)) {
                errorMap[3]?.let {
                    editCategoryLabel.isErrorEnabled  = true
                    editCategoryLabel.error = getString(it) }
            }
            if (errorMap.containsKey(4)) {
                errorMap[4]?.let {
                    editWhenLabel.isErrorEnabled = true
                    editWhenLabel.error = getString(it) }
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        binding?.editWhen?.setText("$day/$month/$year")

    }

}


