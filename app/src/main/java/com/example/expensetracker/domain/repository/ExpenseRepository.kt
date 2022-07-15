package com.example.expensetracker.domain.repository


import androidx.room.Query
import com.example.expensetracker.database.expense.Expense
import io.reactivex.Completable
import io.reactivex.Single

interface ExpenseRepository {

    fun updateExpense(exp: Expense) : Completable


    fun insertExpense(exp: Expense) : Completable


    fun deleteExpense(exp : Expense) : Completable


    fun getAll(): Single<List<Expense>>

    fun getByCat(category :String) :Single<List<Expense>>

    fun getCat(): Single<List<String>>

    fun getTotalExpense() : Single<Int>

    fun getCategoryExpense(category: String) : Single<Int>

    fun getById(id : Int) : Single<Expense>

}