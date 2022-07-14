package com.example.expensetracker

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.expensetracker.database.expense.Expense
import com.example.expensetracker.databinding.FragmentListDisplayBinding
import com.example.expensetracker.domain.usecase.*
import com.example.expensetracker.viewmodels.ExpenseViewModel
import com.example.expensetracker.viewmodels.ExpenseViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.expense_view.*
import javax.inject.Inject


class ListDisplay : DaggerFragment() {


    private val expenseAdapter:ExpenseAdapter by lazy { ExpenseAdapter(::onClickEdit, ::onDelete) }

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
            Log.d("totalExpense","${viewModel.totalExoense}")

//            val swipeDelete = object : SwipeDelete(){
//                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                    onDelete(expenseAdapter.getItemAtPosition(viewHolder.adapterPosition))
//                    super.onSwiped(viewHolder, direction)
//                }
//            }
//
//            val itemTouchHelper = ItemTouchHelper(swipeDelete)
//            itemTouchHelper.attachToRecyclerView(recyclerView)

        }
    }



    private fun onClickEdit(expense:Expense){
        findNavController().navigate(ListDisplayDirections.actionListDisplayToAddEdit( expense ))
    }
    private fun onDelete(expense: Expense){
        viewModel.deleteItem(expense)
    }


    private fun totalExpense(){

    }

}