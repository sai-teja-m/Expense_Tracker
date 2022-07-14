package com.example.expensetracker

import android.content.Context

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeDelete() : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
//    private  val adapter : ExpenseAdapter = ExpenseAdapter()
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            TODO("Not yet implemented")
        }



}