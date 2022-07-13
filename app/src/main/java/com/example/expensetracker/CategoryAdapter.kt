package com.example.expensetracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.database.expense.Expense
import com.example.expensetracker.databinding.ItemCategoryBinding


class CategoryAdapter(private val onClick: (String) -> Unit ) : ListAdapter<String, CategoryAdapter.ExpenseHolder>(
DIFF
) {
    companion object{
        val DIFF = object: DiffUtil.ItemCallback<String>(){
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ExpenseHolder(private val binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(category : String){
            binding.run {
                itemText.text = category
                root.setOnClickListener{
                    onClick(category)
                }
            }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ExpenseHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExpenseHolder(binding)
    }


    override fun onBindViewHolder(holder: CategoryAdapter.ExpenseHolder, position: Int) {
        holder.bind(getItem(position))
    }

}