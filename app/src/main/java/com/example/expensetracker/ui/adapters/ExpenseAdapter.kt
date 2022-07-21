package com.example.expensetracker.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.R
import com.example.expensetracker.database.expense.DateConverter
import com.example.expensetracker.database.expense.Expense
import com.example.expensetracker.databinding.ExpenseViewBinding


class ExpenseAdapter(private val onClick: (Expense) -> Unit, val onDelete: (Expense) -> Unit, private val dateConverter: DateConverter) :
    ListAdapter<Expense, ExpenseAdapter.ExpenseHolder>(
        DIFF
    ) {
    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Expense>() {
            override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
                return oldItem.Id == newItem.Id
            }

            override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
                return oldItem == newItem
            }
        }
    }


    inner class ExpenseHolder(private val binding: ExpenseViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val exp: Expense = getItem(position)
            val str: String? = dateConverter.fromDate(exp.`when`)
            if(str != null)
            Log.d("date",str)
            val arr = str?.split(" ")
            if(str != null && arr != null) {
                val mon = arr[0]
                val date = arr[1]
                val yr = arr[2]

                binding.run {
//                if (position == itemCount - 1)
//                    divider.visibility = View.INVISIBLE
//                else {
//                    divider.visibility = View.VISIBLE
//                }
                    expenseTitle.text =
                        root.context.getString(R.string.expense_title, exp.expenseTitle)
                    expense.text = root.context.getString(R.string.expense_amount, exp.expense)
                    whenMonth.text = mon
                    whenDate.text = date
                    whenYear.text = yr
//                `when`.text = root.context.getString(R.string.expense_date, exp.`when`)
                    category.text = root.context.getString(R.string.expense_category, exp.category)
                    root.setOnClickListener {
                        onClick(exp)
                    }
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseHolder {
        val binding = ExpenseViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExpenseHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseHolder, position: Int) {
        holder.bind(position)
    }

    fun getItemAtPosition(position: Int): Expense {
        return getItem(position)
    }


}