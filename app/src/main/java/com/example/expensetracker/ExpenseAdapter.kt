package com.example.expensetracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.database.expense.Expense
import com.example.expensetracker.databinding.ExpenseViewBinding
import com.example.expensetracker.databinding.FragmentListDisplayBinding


class ExpenseAdapter( private val onClick: (Expense) -> Unit ) : ListAdapter<Expense,ExpenseAdapter.ExpenseHolder>(
    DIFF
) {
    companion object{
        val DIFF = object:DiffUtil.ItemCallback<Expense>(){
            override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
                return oldItem.Id == newItem.Id
            }

            override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
                return oldItem == newItem
            }
        }
    }



    class ExpenseHolder(private val binding: ExpenseViewBinding): RecyclerView.ViewHolder(binding.root){
       fun bind(exp: Expense){
           binding.run {
               expenseTitle.text = exp.expenseTitle
               expense.text = exp.expense.toString()
               `when`.text = exp.`when`
               category.text = exp.category
           }
       }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseHolder {
        val binding = ExpenseViewBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExpenseHolder(binding)
    }


    override fun onBindViewHolder(holder: ExpenseHolder, position: Int) {
        holder.bind(getItem(position))
    }


}