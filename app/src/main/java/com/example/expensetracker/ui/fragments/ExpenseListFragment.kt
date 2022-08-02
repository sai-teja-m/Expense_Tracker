package com.example.expensetracker.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.util.Pair
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.R
import com.example.expensetracker.database.expense.DateConverter
import com.example.expensetracker.database.expense.Expense
import com.example.expensetracker.databinding.FragmentListDisplayBinding
import com.example.expensetracker.ui.adapters.ExpenseAdapter
import com.example.expensetracker.utils.SwipeToDeleteCallBack
import com.example.expensetracker.viewmodels.ExpenseViewModel
import com.example.expensetracker.viewmodels.ExpenseViewModelFactory
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import kotlin.reflect.KProperty


class ExpenseListFragment : DaggerFragment() {

    private val expenseAdapter: ExpenseAdapter by lazy {
        ExpenseAdapter(::onClickEdit, ::onDelete, dateConverter)
    }

    @Inject
    lateinit var expenseViewModelFactory: ExpenseViewModelFactory

    @Inject
    lateinit var dateConverter: DateConverter

    private lateinit var menu: Menu

    private val viewModel: ExpenseViewModel by activityViewModels {
        expenseViewModelFactory
    }


    private var _binding: FragmentListDisplayBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.getAllExpenses()
        viewModel.getCategory()
        viewModel.getTotalExpense()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        this.menu = menu
        setFilterIcon()
        setDateRangeTitle()
    }

    var t: Int = 0
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.total_expense -> {
                if (viewModel.allExpense.value.isNullOrEmpty()) {
                    showSnackBarTotalExpEmpty()
                } else {
                    showSnackBarTotalExp()
                }
                true
            }

            R.id.date_range -> {
                if (viewModel.startDate.value != null && viewModel.endDate.value != null) {
                    viewModel.selectDateRange(null, null)

                    viewModel.filter(viewModel.selectedCategory.value, null, null)
//                    item.title = "Choose Date Range"
                    t = 1

                } else {
                    t = 0
                    showDateRangePicker()
                    setDateRangeTitle()
//                    item.title = "Clear Range"
                }

                true
            }

            R.id.category_total -> {
                if (viewModel.selectedCategory.value.isNullOrEmpty()) {
                    showSnackBarCategoryExp(null)
                } else {
                    viewModel.getCategoryExpense(viewModel.selectedCategory.value.toString())
                }
                true
            }

            R.id.filter -> {
                onClickFilter()
                true
            }
            R.id.make_graph -> {
                viewModel.getCategoryAndAmount()


                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun onClickFilter() {
        findNavController().navigate(ExpenseListFragmentDirections.actionListDisplayToFilterFragment())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListDisplayBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.run {
            recyclerView.layoutManager = LinearLayoutManager(context)

            recyclerView.adapter = expenseAdapter
            val itemTouchHelper =
                ItemTouchHelper(SwipeToDeleteCallBack(expenseAdapter, requireContext()))
            itemTouchHelper.attachToRecyclerView(recyclerView)
            addButton.setOnClickListener {
                findNavController().navigate(
                    ExpenseListFragmentDirections.actionListDisplayToAddEdit(
                        getString(
                            R.string.add_expense
                        ), null
                    )
                )
            }

            viewModel.allExpense.observe(viewLifecycleOwner, Observer {
                expenseAdapter.submitList(it)
                if (it.isNullOrEmpty()) {
                    noItems.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    noItems.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            })
            viewModel.selectedCategory.observe(viewLifecycleOwner) {
                setFilterIcon()
                if (it.isEmpty()) {
                    //DO NOTHING
                } else {
                    viewModel.filter(it, viewModel.startDate.value, viewModel.endDate.value)
                }
            }

            viewModel.categoryExpense.observe(viewLifecycleOwner) {
                if (it != null) {
                    showSnackBarCategoryExp(it)
                    viewModel.clearCategoryExpense()
                }
            }

            viewModel.categoryAndAmount.observe(viewLifecycleOwner) {
                findNavController().navigate(ExpenseListFragmentDirections.actionListDisplayToGraphFragment())
            }

            viewModel.startDate.observe(viewLifecycleOwner) {
                setDateRangeTitle()
            }

//            val spinner: Spinner = expOrder
//            spinner.onItemSelectedListener = this@ExpenseListFragment
//            val listOrder: List<String> = listOf("none", "Low to High", " High to Low")
//            val adapter = ArrayAdapter(
//                requireContext(),
//                R.layout.dropdown_category_list, listOrder
//            )
//
//            spinner.adapter = adapter

            viewModel.expenseOrder.observe(viewLifecycleOwner) {}
        }
    }

    private fun showSnackBarTotalExpEmpty() {
        binding?.let {
            Snackbar.make(
                it.listDisplay,
                getString(R.string.empty_list),
                Snackbar.LENGTH_LONG
            )
                .show()
        }
    }


    private fun showSnackBarTotalExp() {
        binding?.let {
            Snackbar.make(
                it.listDisplay,
                getString(R.string.total_expense_is, viewModel.totalExpense.value.toString()),
                Snackbar.LENGTH_LONG
            )
                .show()
        }
    }

    private fun showSnackBarCategoryExp(categoryExpense: Int?) {
        binding?.let {

            if (categoryExpense == null) {
                Snackbar.make(
                    it.listDisplay,
                    getString(R.string.no_filter_applied),
                    Snackbar.LENGTH_LONG
                )
                    .show()
            } else {

                Snackbar.make(
                    it.listDisplay,
                    getString(
                        R.string.expense_for,
                        viewModel.selectedCategory.value.toString(),
                        categoryExpense.toString()
                    ),
                    Snackbar.LENGTH_LONG
                )
                    .show()
            }
        }
    }

    private fun onClickEdit(expense: Expense) {
        findNavController().navigate(
            ExpenseListFragmentDirections.actionListDisplayToAddEdit(
                getString(R.string.edit_expense),
                expense
            )
        )
    }

    private fun onDelete(expense: Expense) {
        viewModel.deleteExpense(expense)
    }

    private fun setFilterIcon() {
        if (::menu.isInitialized) {
            val item = menu.findItem(R.id.filter)
            val filterSelected = !viewModel.selectedCategory.value.isNullOrEmpty()
            if (item != null) {
                if (filterSelected) {
                    item.setIcon(R.drawable.ic_filter_list_enabled)
                } else {
                    item.setIcon(R.drawable.ic_filter_list)
                }
            }
        }
    }

    private fun setDateRangeTitle() {
        if (::menu.isInitialized) {
            val item = menu.findItem(R.id.date_range)
            if (item != null) {
                if (viewModel.startDate.value == null)
                    item.title = getString(R.string.choose_date_range)
                else item.title = getString(R.string.clear_date_range)
            }
        }
    }

    private fun showDateRangePicker() {
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText(R.string.filter_by_date)
            .setSelection(
                Pair(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            )
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointBackward.now())
                    .build()
            )
            .build()

        dateRangePicker.show(
            childFragmentManager,
            "date Range Picker"
        )

        dateRangePicker.addOnPositiveButtonClickListener { datePicked ->
            dateConverter.longToDate(datePicked.first)
                ?.let {
                    dateConverter.longToDate(datePicked.second)
                        ?.let { endDate ->
                            viewModel.selectDateRange(it, endDate)
                            viewModel.filter(
                                viewModel.selectedCategory.value,
                                start = it,
                                end = endDate
                            )
                        }
                }
        }

    }

//    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
//        var expOrder: Int
//        if (position == 0) expOrder = -1
//        else if (position == 1) expOrder = 0
//        else expOrder = 1
//
////        1viewModel.setExpenseOrder(expOrder)
//        viewModel.filter(
//            viewModel.selectedCategory.value,
//            viewModel.startDate.value,
//            viewModel.endDate.value,
//            expOrder
//        )
//        Toast.makeText(
//            requireContext(),
//            "${parent?.getItemAtPosition(position)}",
//            Toast.LENGTH_SHORT
//        ).show()
//    }

//    override fun onNothingSelected(p0: AdapterView<*>?) {
//        TODO("Not yet implemented")
//    }

}

