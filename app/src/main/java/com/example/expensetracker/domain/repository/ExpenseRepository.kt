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

    fun getByCategory(categories: List<String>): Single<List<Expense>>

    fun getCategory(): Flowable<List<String>>

    fun getCategoryAndAmount() : Single<List<CategoryAmount>>

    fun getTotalExpense(): Single<Int>

    fun getCategoryExpense(categories: List<String>): Single<Int>

    fun getById(id: Int): Single<Expense>

    fun getByDateRange(start: Date, end : Date): Single<List<Expense>>

    fun sortByExpenseAsc() : Single<List<Expense>>

    fun sortByCategoryDateRange(categories: List<String>, start: Date, end :Date): Single<List<Expense>>

    fun sortByCategoryDateRangeExpAsc(categories: List<String>, start: Date, end: Date) : Single<List<Expense>>

    fun sortByCategoryExpenseAsc(categories: List<String>) : Single<List<Expense>>

    fun sortByDateRangeExpAsc(start: Date, end: Date) : Single<List<Expense>>

    fun sortByExpenseDesc() : Single<List<Expense>>

    fun sortByCategoryDateRangeExpDesc(categories: List<String>, start: Date, end: Date) : Single<List<Expense>>

    fun sortByCategoryExpDesc(categories: List<String>) : Single<List<Expense>>

    fun sortByDateRangeExpDesc(start: Date, end: Date) : Single<List<Expense>>

    fun sortByDateAsc() : Single<List<Expense>>

    fun sortByCategoryDateRangeDateAsc(categories: List<String>, start: Date, end: Date) : Single<List<Expense>>

    fun sortByCategoryDateAsc(categories: List<String>) : Single<List<Expense>>

    fun sortByDateRangeDateAsc(start: Date, end: Date) : Single<List<Expense>>

    fun sortByDateDesc() : Single<List<Expense>>

    fun sortByCategoryDateRangeDateDesc(categories: List<String>, start: Date, end: Date) : Single<List<Expense>>

    fun sortByCategoryDateDesc(categories: List<String>) : Single<List<Expense>>

    fun sortByDateRangeDateDesc(start: Date, end: Date) : Single<List<Expense>>

}