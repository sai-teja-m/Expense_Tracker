package com.example.expensetracker.ui.adapters

import android.hardware.biometrics.BiometricManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.R
import com.example.expensetracker.databinding.ItemCategoryBinding
import com.github.mikephil.charting.utils.Utils.init
import kotlinx.android.synthetic.main.expense_view.view.*


class CategoryAdapter() :
    ListAdapter<String, CategoryAdapter.CategoryHolder>(
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

    private var currentSelectedCategories: MutableList<String> = mutableListOf()

    fun setCurrentCategory(categories: List<String>) {
        currentSelectedCategories.clear()
        currentSelectedCategories.addAll(categories)
    }

    inner class CategoryHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.run {
                val category: String = getItem(position)
                if(currentSelectedCategories.contains(category)){
                    itemText.isChecked = true
                }else{
                    itemText.isChecked =false
                }

                itemText.text = category

                itemText.setOnCheckedChangeListener { _, isChecked ->
                    if(isChecked) {
                        addSelection(position)
                    }
                    else {
                        removeSelection(position)
                    }

                    }

                }

            }
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryAdapter.CategoryHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryHolder(binding)
    }




    override fun onBindViewHolder(holder: CategoryAdapter.CategoryHolder, position: Int) {
        holder.bind(position)
    }

    fun addSelection(position: Int){
        currentSelectedCategories.add(getItem(position))
//        notifyItemChanged(position)
    }

    fun removeSelection(position: Int){
        currentSelectedCategories.remove(getItem(position))
//        notifyItemChanged(position)
    }
    fun removeAllSelection(){
        currentSelectedCategories.clear()
        notifyDataSetChanged()
    }

    fun getSelectedCategories():List<String> { return currentSelectedCategories.distinct()}
    fun isItemSelected(item : String):Boolean{
        return currentSelectedCategories.contains(item)
    }
}