package com.example.expensetracker

import android.app.Application
import com.example.expensetracker.database.expense.ExpenseDao
import com.example.expensetracker.database.expense.ExpenseDatabase
import com.example.expensetracker.domain.repository.ExpenseRepository
import com.example.expensetracker.domain.repository.ExpenseRepositoryImpl

class ExpenseApplication :Application() {
lateinit var database: ExpenseDatabase
lateinit var expenseRepo: ExpenseRepository
    override fun onCreate() {
        super.onCreate()
        database = ExpenseDatabase.getDatabase(this)
        expenseRepo =ExpenseRepositoryImpl(database.expenseDao())
    }
}