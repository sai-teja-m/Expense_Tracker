package com.example.expensetracker.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.core.util.Pair
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.R
import com.example.expensetracker.database.expense.DateConverter
import com.example.expensetracker.databinding.FragmentFilterBinding
import com.example.expensetracker.domain.SortFilterOptions
import com.example.expensetracker.ui.adapters.CategoryAdapter
import com.example.expensetracker.viewmodels.ExpenseViewModel
import com.example.expensetracker.viewmodels.ExpenseViewModelFactory
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.android.support.DaggerFragment
import java.util.*
import javax.inject.Inject

class FilterFragment : DaggerFragment() {
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding

    @Inject
    lateinit var dateConverter: DateConverter

    @Inject
    lateinit var expenseViewModelFactory: ExpenseViewModelFactory
    private val categoryAdapter: CategoryAdapter by lazy { CategoryAdapter() }
    private val viewModel: ExpenseViewModel by activityViewModels {
        expenseViewModelFactory
    }
    private lateinit var menu: Menu
    private var startDate: Date? = null
    private var endDate: Date? = null
    private var categoryList: List<String> = emptyList()
    private var sortOptions:SortFilterOptions.SortOptions = SortFilterOptions.SortOptions.DateOrderDesc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setDateRange()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.clear_filter -> {
                clearAll()
                true
            }
            else ->
                return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.run {
            categoryRecycler.layoutManager = LinearLayoutManager(context)
            categoryRecycler.adapter = categoryAdapter


            //drop down open close for sort option
            sortDropDown.setOnClickListener {
                if (sortDropDown.tag == "up") {
                    sortRadioGroup.visibility = View.GONE
                    sortDropDown.setImageResource(R.drawable.ic_down)
                    sortDropDown.tag = getString(R.string.down)
                } else {
                    sortRadioGroup.visibility = View.VISIBLE
                    sortDropDown.setImageResource(R.drawable.ic_up)
                    sortDropDown.tag = getString(R.string.up)
                }
            }

            //drop down for filter option
            filterDropDown.setOnClickListener {
                if (filterDropDown.tag == "up") {
                    filterDropDown.setImageResource(R.drawable.ic_down)
                    filterDropDown.tag = getString(R.string.down)
                    filterLayout.visibility = View.GONE

                } else {
                    filterDropDown.setImageResource(R.drawable.ic_up)
                    filterDropDown.tag = getString(R.string.up)
                    filterLayout.visibility = View.VISIBLE
                }
            }

            viewModel.categoryList.observe(viewLifecycleOwner) {
                categoryAdapter.setCurrentCategory(
                    viewModel.sortFilterDetails.value?.categories ?: emptyList()
                )
                categoryAdapter.submitList(it)
            }


            //range visibility
            viewModel.sortFilterDetails.observe(viewLifecycleOwner) {
                if (viewModel.sortFilterDetails.value?.dateRange?.startDate == null) {
                    //DO NOTHING
                } else if (viewModel.sortFilterDetails.value?.dateRange?.startDate != null) {
                    startDate = viewModel.sortFilterDetails?.value?.dateRange?.startDate
                    endDate = viewModel.sortFilterDetails.value?.dateRange?.endDate
                    filterStartDate.text = dateConverter.fromDate(startDate)
                    filterEndDate.text = dateConverter.fromDate(endDate)
                    filterStartDate.visibility = View.VISIBLE
                    filterEndDate.visibility = View.VISIBLE
                    dateRange.text = getString(R.string.clear_date_range)
                }
                viewModel.sortFilterDetails.value?.categories?.let { it1 ->
                    categoryAdapter.setCurrentCategory(
                        it1
                    )
                }
                setDateRangeInit()
            }

//            }

            dateRange.setOnClickListener {
                if (dateRange.text == getString(R.string.select_date_range))
                    showDateRangePicker()
                else
                    clearDateRange()
            }

            editDateRange.setOnClickListener {
                showDateRangePicker()
            }

            applyFilters.setOnClickListener {
                setSelectCategory(categoryAdapter.getSelectedCategories())
                onRadioButtonClicked()

                var sort = SortFilterOptions(
                    sortOptions,
                    SortFilterOptions.DateRangeFilter(startDate, endDate),
                    categoryList
                )

                viewModel.setSortFilterOption(sort)
                viewModel.filter(sort)
                findNavController().navigateUp()
            }

            setDateRange()
            setRadio()
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
                            startDate = it
                            this.endDate = endDate
                            setDate(startDate, this.endDate)
                        }
                }

        }
    }

    private fun setRadio() {
        sortOptions = viewModel.sortFilterDetails.value?.sort?: SortFilterOptions.SortOptions.DateOrderDesc

        if (viewModel.sortFilterDetails == null || sortOptions ==SortFilterOptions.SortOptions.DateOrderDesc ) {
            binding?.radioSortNewestFirst?.isChecked = true
        }
        if (sortOptions == SortFilterOptions.SortOptions.DateOrderAsc)
            binding?.radioSortOldestFirst?.isChecked = true
        if (sortOptions == SortFilterOptions.SortOptions.ExpOrderDesc)
            binding?.radioExpenseAmountHighFirst?.isChecked = true
        if (sortOptions == SortFilterOptions.SortOptions.ExpOrderAsc)
            binding?.radioExpenseAmountLowFirst?.isChecked = true
    }

    private fun onRadioButtonClicked() {

        // Check which radio button was clicked
        when (binding?.sortRadioGroup?.checkedRadioButtonId) {
            R.id.radio_sort_newest_first -> {
                sortOptions = SortFilterOptions.SortOptions.DateOrderDesc
            }
            R.id.radio_sort_oldest_first -> {
                sortOptions = SortFilterOptions.SortOptions.DateOrderAsc
            }
            R.id.radio_expense_amount_high_first -> {
                sortOptions = SortFilterOptions.SortOptions.ExpOrderDesc
            }
            R.id.radio_expense_amount_low_first -> {
                sortOptions = SortFilterOptions.SortOptions.ExpOrderAsc
            }
        }
    }


    private fun setDate(start: Date?, end: Date?) {

        if (start != null && end != null) {
            binding?.filterStartDate?.text = dateConverter.fromDate(start)
            binding?.filterEndDate?.text = dateConverter.fromDate(end)
        } else {
            binding?.filterStartDate?.text = null
            binding?.filterEndDate?.text = null
        }
        setDateRange()
    }

    private fun setDateRangeInit() {
        if (viewModel.sortFilterDetails.value?.dateRange?.startDate != null) {
            binding?.filterStartDate?.text =
                dateConverter.fromDate(viewModel.sortFilterDetails?.value?.dateRange?.startDate)
            binding?.filterEndDate?.text =
                dateConverter.fromDate(viewModel.sortFilterDetails.value?.dateRange?.endDate)
            binding?.cvDateRange?.visibility = View.VISIBLE
            binding?.dateRange?.text = getString(R.string.clear_date_range)
        }
    }

    private fun setDateRange() {

        if (binding?.filterStartDate?.text == "" && binding?.filterEndDate?.text == "") {
            binding?.cvDateRange?.visibility = View.GONE
            binding?.dateRange?.text = getString(R.string.select_date_range)
        } else {
            binding?.cvDateRange?.visibility = View.VISIBLE
            binding?.dateRange?.text = getString(R.string.clear_date_range)
        }

    }

    private fun clearDateRange() {
        startDate = null
        endDate = null
        setDate(null, null)
        setDateRange()
    }

    private fun setSelectCategory(str: List<String>) {
        categoryList = str
    }


    private fun clearAll() {
        viewModel.setSortFilterOption(null)
        viewModel.filter(null)
        viewModel.getAllExpenses()
        clearDateRange()
        categoryAdapter.removeAllSelection()
        findNavController().navigateUp()
    }

}