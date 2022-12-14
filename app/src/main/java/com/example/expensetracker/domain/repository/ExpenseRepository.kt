package com.example.expensetracker.domain.repository


import com.example.expensetracker.database.expense.CategoryAmount
import com.example.expensetracker.database.expense.Expense
import com.example.expensetracker.domain.SortFilterOptions
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface ExpenseRepository {

    fun updateExpense(exp: Expense): Completable


    fun insertExpense(exp: Expense): Completable


    fun deleteExpense(exp: Expense): Completable

    fun getAllExpense(): Single<List<Expense>>


    fun getCategory(): Flowable<List<String>>

    fun getCategoryAndAmount(): Single<List<CategoryAmount>>

    fun getTotalExpense(): Single<Int>

    fun getCategoryExpense(categories: List<String>): Single<Int>

    fun getById(id: Int): Single<Expense>

    fun sortAndFilter(sortFilterOptions: SortFilterOptions): Single<List<Expense>>
}