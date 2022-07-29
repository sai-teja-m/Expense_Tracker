package com.example.expensetracker.domain.repository


import androidx.room.Query
import com.example.expensetracker.database.expense.CategoryAmount
import com.example.expensetracker.database.expense.Expense
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

interface ExpenseRepository {

    fun updateExpense(exp: Expense): Completable


    fun insertExpense(exp: Expense): Completable


    fun deleteExpense(exp: Expense): Completable


    fun getAllExpense(): Single<List<Expense>>

    fun getByCategory(category: String): Single<List<Expense>>

    fun getCategory(): Flowable<List<String>>

    fun getCategoryAndAmount() : Single<List<CategoryAmount>>

    fun getTotalExpense(): Single<Int>

    fun getCategoryExpense(category: String): Single<Int>

    fun getById(id: Int): Single<Expense>

    fun getByDateRange(start: Date, end : Date): Single<List<Expense>>

    fun getByExpenseAsc() : Single<List<Expense>>

    fun getByFilterDateRange(category: String,start: Date, end :Date): Single<List<Expense>>

    fun getByFilterDateRangeExpAsc(category: String, start: Date ,end: Date) : Single<List<Expense>>

    fun getByFilterExpenseAsc(category: String) : Single<List<Expense>>

    fun getByDateRangeExpAsc(start: Date ,end: Date) : Single<List<Expense>>

    fun getByExpenseDesc() : Single<List<Expense>>

    fun getByFilterDateRangeExpDesc(category: String, start: Date ,end: Date) : Single<List<Expense>>

    fun getByFilterExpDesc(category: String) : Single<List<Expense>>

    fun getByDateRangeExpDesc(start: Date ,end: Date) : Single<List<Expense>>
}