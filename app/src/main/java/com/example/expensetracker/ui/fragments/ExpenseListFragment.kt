package com.example.expensetracker.ui.fragments

import android.app.ProgressDialog.show
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.R
import com.example.expensetracker.database.expense.Expense
import com.example.expensetracker.databinding.FragmentListDisplayBinding
import com.example.expensetracker.ui.adapters.ExpenseAdapter
import com.example.expensetracker.utils.SwipeToDeleteCallBack
import com.example.expensetracker.viewmodels.ExpenseViewModel
import com.example.expensetracker.viewmodels.ExpenseViewModelFactory
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class ExpenseListFragment : DaggerFragment() {


    private val expenseAdapter: ExpenseAdapter by lazy { ExpenseAdapter(::onClickEdit, ::onDelete) }

    @Inject
    lateinit var expenseViewModelFactory: ExpenseViewModelFactory


    private lateinit var menu: Menu

    private val viewModel: ExpenseViewModel by lazy {
        ViewModelProvider(
            (requireActivity().viewModelStore),
            expenseViewModelFactory
        )[ExpenseViewModel::class.java]
    }


    private var _binding: FragmentListDisplayBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        this.menu = menu
        setFilterIcon()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.total_expense -> {
                showSnackBarTotalExp()
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
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun onClickFilter() {
        findNavController().navigate(ExpenseListFragmentDirections.actionListDisplayToCategoryFragment())
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

                    viewModel.filterByCategory(it)

                }
            }

            viewModel.categoryExpense.observe(viewLifecycleOwner) {
                if (it != null) {

                    showSnackBarCategoryExp(it)
                    viewModel.clearCategoryExpense()
                }
            }

            viewModel.getAllExpenses()
            viewModel.getCategory()
            viewModel.getTotalExpense()


        }
    }

    private fun showSnackBarTotalExp() {
        binding?.let {
            Snackbar.make(
                it.listDisplay,
                getString(R.string.total_expense_is,viewModel.totalExpense.value.toString()),
                Snackbar.LENGTH_LONG
            )
                .show()
        }
    }

    private fun showSnackBarCategoryExp(categoryExpense: Int?) {
        binding?.let {

            if (categoryExpense == null) {
                Snackbar.make(it.listDisplay, getString(R.string.no_filter_applied), Snackbar.LENGTH_LONG)
                    .show()
            } else {

                Snackbar.make(
                    it.listDisplay,
                    getString(R.string.expense_for ,viewModel.selectedCategory.value.toString(), categoryExpense.toString()),
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

}