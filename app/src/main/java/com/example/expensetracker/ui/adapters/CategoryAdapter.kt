package com.example.expensetracker.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.R
import com.example.expensetracker.databinding.ItemCategoryBinding


class CategoryAdapter(private val onClick: (String) -> Unit) :
    ListAdapter<String, CategoryAdapter.ExpenseHolder>(
        DIFF
    ) {
    companion object {
        val DIFF = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var currentSelectedCategory: String = ""

    fun setCurrentCategory(category: String) {
        currentSelectedCategory = category
    }

    inner class ExpenseHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.run {
                val category: String = getItem(position)
                if (position == itemCount - 1)
                    divider.visibility = View.INVISIBLE
                else {
                    divider.visibility = View.VISIBLE
                }
                if (category == currentSelectedCategory) {
                    TextViewCompat.setTextAppearance(itemText, R.style.selectedCategoryStyle)
                    ivSelection.visibility = View.VISIBLE
                } else {
                    TextViewCompat.setTextAppearance(itemText, R.style.unSelectedCategoryStyle)
                    ivSelection.visibility = View.GONE
                }
                itemText.text = category
                root.setOnClickListener {

                    onClick(category)
                }
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExpenseHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExpenseHolder(binding)
    }


    override fun onBindViewHolder(holder: ExpenseHolder, position: Int) {
        holder.bind(position)

    }
}