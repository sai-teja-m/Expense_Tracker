package com.example.expensetracker.database.expense

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull
import java.util.*

@Parcelize
@Entity
data class Expense(
    @PrimaryKey (autoGenerate = true) val Id:Int =0,
    @NotNull @ColumnInfo(name = "expense_title") val expenseTitle: String,
    @NotNull @ColumnInfo(name = "expense") val expense: Int,
    @NotNull @ColumnInfo(name = "when") val `when`: Date?,
    @NotNull @ColumnInfo(name = "category") val category: String
):Parcelable

data class CategoryAmount(
    val category: String,
    val categoryAmount: Int
)
