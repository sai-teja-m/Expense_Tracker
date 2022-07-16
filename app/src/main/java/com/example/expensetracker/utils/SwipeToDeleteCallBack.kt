package com.example.expensetracker.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.R
import com.example.expensetracker.ui.adapters.ExpenseAdapter
import kotlin.math.abs

class SwipeToDeleteCallBack(private val adapter: ExpenseAdapter, private val context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    private var deleteIcon: Drawable? =
        ContextCompat.getDrawable(context, R.drawable.ic_delete_expense)
    private var deleteBackground: ColorDrawable? = ColorDrawable(Color.RED)
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.absoluteAdapterPosition
        adapter.onDelete(adapter.getItemAtPosition(position))
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        val backgroundCornerOffSet = -20
        if (dX > 0) {
            deleteBackground?.setBounds(
                itemView.left,
                itemView.top,
                itemView.left + dX.toInt() + backgroundCornerOffSet,
                itemView.bottom
            )

        } else if (dX < 0) {
            deleteBackground?.setBounds(
                itemView.right + dX.toInt() - backgroundCornerOffSet,
                itemView.top,
                itemView.right,
                itemView.bottom
            )
        } else {
            deleteBackground?.setBounds(0, 0, 0, 0)
        }
        val iconMargin = (itemView.height - (deleteIcon?.intrinsicHeight ?: 0)) / 2
        val iconTop = itemView.top + iconMargin
        val iconBottom = iconTop + (deleteIcon?.intrinsicHeight ?: 0)
        if (dX > 0) { // Swiping to the right
            val iconLeft = itemView.left + iconMargin
            val iconRight = iconLeft + (deleteIcon?.intrinsicWidth ?: 0)
            if (abs(dX) > (iconMargin + (deleteIcon?.intrinsicWidth ?: 0) + iconMargin / 2)) {
                deleteIcon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            } else {
                deleteIcon?.setBounds(0, 0, 0, 0)
            }
            deleteBackground?.setBounds(
                itemView.left,
                itemView.top,
                itemView.left + dX.toInt() + backgroundCornerOffSet,
                itemView.bottom
            )
        } else if (dX < 0) { // Swiping to the left
            val iconLeft = itemView.right - iconMargin - (deleteIcon?.intrinsicWidth ?: 0)
            val iconRight = itemView.right - iconMargin
            if (abs(dX) > (iconMargin + (deleteIcon?.intrinsicWidth ?: 0) + iconMargin / 2)) {
                deleteIcon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            } else {
                deleteIcon?.setBounds(0, 0, 0, 0)
            }
            deleteBackground?.setBounds(
                itemView.right + dX.toInt() - backgroundCornerOffSet,
                itemView.top,
                itemView.right,
                itemView.bottom
            )
        } else {
            deleteBackground?.setBounds(0, 0, 0, 0)
            deleteIcon?.setBounds(0, 0, 0, 0)
        }

        deleteBackground?.draw(c)
        deleteIcon?.draw(c)
    }


}