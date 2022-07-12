package com.example.expensetracker

import android.app.Application
import com.example.expensetracker.database.expense.ExpenseDao
import com.example.expensetracker.database.expense.ExpenseDatabase
import com.example.expensetracker.domain.repository.ExpenseRepository
import com.example.expensetracker.domain.repository.ExpenseRepositoryImpl

class ExpenseApplication :Application() {
    val database: ExpenseDatabase by lazy { ExpenseDatabase.getDatabase(this) }
    val expenseRepo: ExpenseRepository = ExpenseRepositoryImpl(database.expenseDao())
}