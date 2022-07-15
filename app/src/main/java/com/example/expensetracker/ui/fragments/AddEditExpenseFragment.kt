package com.example.expensetracker.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.core.widget.doOnTextChanged

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.expensetracker.R
import com.example.expensetracker.database.expense.Expense
import com.example.expensetracker.databinding.FragmentAddEditBinding
import com.example.expensetracker.viewmodels.ExpenseViewModel
import com.example.expensetracker.viewmodels.ExpenseViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_add_edit.*
import javax.inject.Inject


class AddEditExpenseFragment : DaggerFragment(), DatePickerDialog.OnDateSetListener {


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

    private val navigationArgs: AddEditExpenseFragmentArgs by navArgs()

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

            val action = AddEditExpenseFragmentDirections.actionAddEditToListDisplay()
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

            val action = AddEditExpenseFragmentDirections.actionAddEditToListDisplay()
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
        binding?.run {
            editTitle.doOnTextChanged { _, _, _, _ ->

                editTitleLabel.isErrorEnabled =   false
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
        binding?.run {
            val categoryList:MutableList<String> = (viewModel.categoryList.value?.toMutableList()?: mutableListOf()).apply {
                addAll(getDefaultCategoryList())

            }
            val adapter = ArrayAdapter(requireContext(),
                R.layout.dropdown_category_list, categoryList.distinct())
            editCategory.setAdapter(adapter)
        }
    }

    private fun getDefaultCategoryList():List<String>{
        return listOf(
            getString(R.string.food),
            getString(R.string.grocery),
            getString(R.string.education),
            getString(R.string.entertainment),
            getString(R.string.shopping),
            getString(R.string.travel)
        )
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
                    editTitleLabel.error = getString(it)
                }
            }
            if (errorMap.containsKey(2)) {
                errorMap[2]?.let {
                    editExpenseLabel.error = getString(it) }
            }
            if (errorMap.containsKey(3)) {
                errorMap[3]?.let {
                    editCategoryLabel.error = getString(it) }
            }
            if (errorMap.containsKey(4)) {
                errorMap[4]?.let {
                    editWhenLabel.error = getString(it) }
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        binding?.editWhen?.setText("$day/$month/$year")

    }

}


