package com.example.expensetracker.domain.repository

import com.example.expensetracker.database.expense.CategoryAmount
import com.example.expensetracker.database.expense.Expense
import com.example.expensetracker.database.expense.ExpenseDao
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val dao:ExpenseDao
) : ExpenseRepository{
    override fun updateExpense(exp: Expense): Completable {
        return dao.updateExpense(exp)
    }

    override fun insertExpense(exp: Expense): Completable {
        return dao.insertExpense(exp)
    }

    override fun deleteExpense(exp: Expense): Completable {
        return dao.deleteExpense(exp)
    }

    override fun getAllExpense(): Single<List<Expense>> {
        return dao.getAllExpense()
    }

    override fun getCategory() : Flowable<List<String>>{
        return dao.getCategory()
    }

    override fun getCategoryAndAmount(): Single<List<CategoryAmount>> {
        return dao.getCategoryAndAmount()
    }

    override fun getTotalExpense(): Single<Int> {
        return dao.getTotalExpense()
    }

    override fun getCategoryExpense(category: String): Single<Int> {
       return dao.getCategoryExpense(category)
    }

    override fun getByCategory(category: String): Single<List<Expense>> {
        return dao.getByCategory(category)
    }
    override fun getById(id: Int): Single<Expense> {
        return dao.getById(id)
    }

    override fun getByDateRange(start: Date, end: Date): Single<List<Expense>> {
        return dao.getByDateRange(start, end)
    }


}