package com.example.expensetracker

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.LinearLayoutManager

import com.example.expensetracker.database.expense.Expense
import com.example.expensetracker.databinding.FragmentListDisplayBinding
import com.example.expensetracker.domain.usecase.*
import com.example.expensetracker.viewmodels.ExpenseViewModel
import com.example.expensetracker.viewmodels.ExpenseViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class ListDisplay : DaggerFragment() {


    private val expenseAdapter:ExpenseAdapter by lazy { ExpenseAdapter(::onClickEdit, ::onClickDlt) }

    @Inject
    lateinit var expenseViewModelFactory: ExpenseViewModelFactory

    @Inject
    lateinit var application: Application

    private val viewModel: ExpenseViewModel by lazy {
        ViewModelProvider((requireActivity().viewModelStore), expenseViewModelFactory)[ExpenseViewModel::class.java]
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