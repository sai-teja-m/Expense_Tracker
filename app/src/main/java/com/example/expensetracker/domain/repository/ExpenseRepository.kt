package com.example.expensetracker.domain.repository


import com.example.expensetracker.database.expense.Expense
import io.reactivex.Completable
import io.reactivex.Single

interface ExpenseRepository {

    fun updateExpense(exp: Expense): Completable


    fun insertExpense(exp: Expense): Completable


    fun deleteExpense(exp: Expense): Completable


    fun getAllExpense(): Single<List<Expense>>

    fun getByCategory(category: String): Single<List<Expense>>

    fun getCategory(): Single<List<String>>

    fun getTotalExpense(): Single<Int>

    fun getCategoryExpense(category: String): Single<Int>

    fun getById(id: Int): Single<Expense>

}