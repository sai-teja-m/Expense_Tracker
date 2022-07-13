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
import com.example.expensetracker.domain.usecase.*
import com.example.expensetracker.viewmodels.ExpenseViewModel
import com.example.expensetracker.viewmodels.ExpenseViewModelFactory
import kotlinx.android.synthetic.main.expense_view.*
import kotlinx.android.synthetic.main.fragment_list_display.*

class ListDisplay : Fragment() {



    private val expenseApplication by lazy {  requireActivity().application as ExpenseApplication}
    private val insertExpenseUseCase by lazy {   InsertExpenseUseCase(expenseApplication.expenseRepo)}
    private val updateExpenseUseCase by lazy {  UpdateExpenseUseCase(expenseApplication.expenseRepo)}
    private val deleteExpenseUseCase by lazy {   DeleteExpenseUseCase(expenseApplication.expenseRepo)}
    private val getAllUseCase by lazy {   GetAllUseCase(expenseApplication.expenseRepo)}
    private val getByCatUseCase by lazy { GetByCatUseCase(expenseApplication.expenseRepo)}
    private val getCatUseCase by lazy { GetCatUseCase(expenseApplication.expenseRepo) }


    private val expenseAdapter:ExpenseAdapter by lazy { ExpenseAdapter(::onClickEdit, ::onClickDlt) }


    private val viewModel: ExpenseViewModel by activityViewModels {
        ExpenseViewModelFactory(
            insertExpenseUseCase,updateExpenseUseCase,deleteExpenseUseCase,getAllUseCase,getByCatUseCase,getCatUseCase
        )
    }


    private var _binding: FragmentListDisplayBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

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
            addButton.setOnClickListener{
                findNavController().navigate(ListDisplayDirections.actionListDisplayToAddEdit(null ))
            }
            filter.setOnClickListener{
                findNavController().navigate(ListDisplayDirections.actionListDisplayToCategoryFragment())
            }
            clearFilter.setOnClickListener{
                viewModel.selectCategory("")
            }
            viewModel.allExpense.observe(viewLifecycleOwner, Observer { expenseAdapter.submitList(it)})
            viewModel.selectedCategory.observe(viewLifecycleOwner) {
                filter.text = if (it.isEmpty()) {
                    viewModel.getAll()
                    clearFilter.visibility = View.GONE
                    getString(R.string.filter)
                } else {
                    viewModel.filterByCategory(it)
                    clearFilter.visibility = View.VISIBLE
                    it
                }
            }
            viewModel.getAll()
            viewModel.getCat()
        }
    }
    private fun onClickEdit(expense:Expense){
        findNavController().navigate(ListDisplayDirections.actionListDisplayToAddEdit( expense ))
    }
    private fun onClickDlt(expense: Expense){
        viewModel.deleteItem(expense)
    }


    private fun totalExpense(){

    }

}