package com.example.expensetracker

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.database.expense.Expense
import com.example.expensetracker.databinding.FragmentListDisplayBinding
import com.example.expensetracker.domain.usecase.*
import com.example.expensetracker.viewmodels.ExpenseViewModel
import com.example.expensetracker.viewmodels.ExpenseViewModelFactory
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.expense_view.*
import javax.inject.Inject


class ListDisplay : DaggerFragment() {


    private val expenseAdapter:ExpenseAdapter by lazy { ExpenseAdapter(::onClickEdit, ::onDelete) }

    @Inject
    lateinit var expenseViewModelFactory: ExpenseViewModelFactory

    @Inject
    lateinit var application: Application

    private lateinit var menu: Menu

    private val viewModel: ExpenseViewModel by lazy {
        ViewModelProvider((requireActivity().viewModelStore), expenseViewModelFactory)[ExpenseViewModel::class.java]
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
        return when(item.itemId){
            R.id.total_expense->{
                showSnackBarTotalExp()
                true
            }
            R.id.category_total->{
                viewModel.getCategoryExpense(viewModel.selectedCategory.value.toString())

                true
            }

            R.id.filter->{
                onClickFilter()
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }



    private fun onClickFilter(){
        findNavController().navigate(ListDisplayDirections.actionListDisplayToCategoryFragment())
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
        binding?.run{
            recyclerView.layoutManager = LinearLayoutManager(context)

            recyclerView.adapter = expenseAdapter
            val itemTouchHelper = ItemTouchHelper(SwipeDeleteCallBack(expenseAdapter,requireContext()))
            itemTouchHelper.attachToRecyclerView(recyclerView)
            addButton.setOnClickListener{
                findNavController().navigate(ListDisplayDirections.actionListDisplayToAddEdit(getString(R.string.add_expense),null))
            }

            viewModel.allExpense.observe(viewLifecycleOwner, Observer { expenseAdapter.submitList(it)})
            viewModel.selectedCategory.observe(viewLifecycleOwner) {
                setFilterIcon()
                if (it.isEmpty()) {
                    //DO NOTHING
                } else {

                    viewModel.filterByCategory(it)

                }
            }

            viewModel.categoryExpense.observe(viewLifecycleOwner) {
                if(it != null) {
                    showSnackBarCategoryExp()
                    viewModel.clearCategoryExpense()
                }
            }

            viewModel.getAll()
            viewModel.getCat()
            viewModel.getTotalExpense()


        }
    }

    private fun showSnackBarTotalExp(){
        binding?.let {
            Snackbar.make(it.listDisplay, "Total Expense is ${viewModel.totalExpense.value}", Snackbar.LENGTH_LONG)
                .show()
        }
    }

    private fun showSnackBarCategoryExp(){
        binding?.let {

            if(viewModel.selectedCategory.value.isNullOrEmpty()){
                Snackbar.make(it.listDisplay, "No Filter Applied", Snackbar.LENGTH_LONG)
                    .show()
            }
            else{

            Snackbar.make(it.listDisplay, "Category Expense for ${viewModel.selectedCategory.value.toString()} is ${viewModel.categoryExpense.value}", Snackbar.LENGTH_LONG)
                .show()
        }
        }
    }

    private fun onClickEdit(expense:Expense){
        findNavController().navigate(ListDisplayDirections.actionListDisplayToAddEdit(getString(R.string.edit_expense), expense))
    }
    private fun onDelete(expense: Expense){
        viewModel.deleteItem(expense)
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