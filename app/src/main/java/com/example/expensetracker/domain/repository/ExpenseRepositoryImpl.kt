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
        return dao.sortByDateRange(start, end)
    }

    override fun sortByExpenseAsc(): Single<List<Expense>> {
        return dao.sortByExpAsc()
    }

    override fun sortByCategoryDateRange(
        category: String,
        start: Date,
        end: Date
    ): Single<List<Expense>> {
        return dao.sortByFilterDateRange(category, start, end)
    }




    override fun sortByCategoryDateRangeExpAsc(
        category: String,
        start: Date,
        end: Date
    ): Single<List<Expense>> {
        return dao.sortByFilterDateRangeExpAsc(category,start, end)
    }

    override fun sortByCategoryExpenseAsc(category: String): Single<List<Expense>> {
        return dao.sortByCategoryExpAsc(category)
    }

    override fun sortByDateRangeExpAsc(start: Date, end: Date): Single<List<Expense>> {
        return dao.sortByDateRangeExpAsc(start, end)
    }

    override fun sortByExpenseDesc(): Single<List<Expense>> {
        return dao.sortByExpDesc()
    }

    override fun sortByCategoryDateRangeExpDesc(
        category: String,
        start: Date,
        end: Date
    ): Single<List<Expense>> {
        return dao.sortByCategoryDateRangeExpDesc(category, start, end)
    }

    override fun sortByCategoryExpDesc(category: String): Single<List<Expense>> {
        return dao.sortByCategoryExpDesc(category)
    }

    override fun sortByDateRangeExpDesc(start: Date, end: Date): Single<List<Expense>> {
        return dao.sortByDateRangeExpDesc(start, end)
    }

    override fun sortByDateAsc(): Single<List<Expense>> {
        return dao.sortByDateAsc()
    }

    override fun sortByCategoryDateRangeDateAsc(
        category: String,
        start: Date,
        end: Date
    ): Single<List<Expense>> {
        return dao.sortByCategoryDateRangeDateAsc(category, start, end)
    }

    override fun sortByCategoryDateAsc(category: String): Single<List<Expense>> {
        return dao.sortByCategoryDateAsc(category)
    }

    override fun sortByDateRangeDateAsc(start: Date, end: Date): Single<List<Expense>> {
        return dao.sortByDateRangeDateAsc(start, end)
    }

    override fun sortByDateDesc(): Single<List<Expense>> {
        return dao.sortByDateDesc()
    }

    override fun sortByCategoryDateRangeDateDesc(
        category: String,
        start: Date,
        end: Date
    ): Single<List<Expense>> {
        return dao.sortByCategoryDateRangeExpDesc(category, start, end)
    }

    override fun sortByCategoryDateDesc(category: String): Single<List<Expense>> {
        return dao.sortByCategoryDateDesc(category)
    }

    override fun sortByDateRangeDateDesc(start: Date, end: Date): Single<List<Expense>> {
        return dao.sortByDateRangeDateDesc(start, end)
    }


}