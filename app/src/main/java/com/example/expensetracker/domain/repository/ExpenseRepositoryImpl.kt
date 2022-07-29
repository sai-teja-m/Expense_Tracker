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
    private val dao: ExpenseDao
) : ExpenseRepository {
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

    override fun getCategory(): Flowable<List<String>> {
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

    override fun getByExpenseAsc(): Single<List<Expense>> {
        return dao.getByFilterExpAsc()
    }

    override fun getByFilterDateRange(
        category: String,
        start: Date,
        end: Date
    ): Single<List<Expense>> {
        return dao.getByFilterDateRange(category, start, end)
    }




    override fun getByFilterDateRangeExpAsc(
        category: String,
        start: Date,
        end: Date
    ): Single<List<Expense>> {
        return dao.getByFilterDateRangeExpAsc(category,start, end)
    }

    override fun getByFilterExpenseAsc(category: String): Single<List<Expense>> {
        return dao.getByFilterExpAsc(category)
    }

    override fun getByDateRangeExpAsc(start: Date, end: Date): Single<List<Expense>> {
        return dao.getByDateRangeExpAsc(start, end)
    }

    override fun getByExpenseDesc(): Single<List<Expense>> {
        return dao.getByFilterExpDesc()
    }

    override fun getByFilterDateRangeExpDesc(
        category: String,
        start: Date,
        end: Date
    ): Single<List<Expense>> {
        return dao.getByFilterDateRangeExpDesc(category, start, end)
    }

    override fun getByFilterExpDesc(category: String): Single<List<Expense>> {
        return dao.getByFilterExpDesc(category)
    }

    override fun getByDateRangeExpDesc(start: Date, end: Date): Single<List<Expense>> {
        return dao.getByDateRangeExpDesc(start, end)
    }


}