package com.example.expensetracker.domain.repository

import com.example.expensetracker.database.expense.CategoryAmount
import com.example.expensetracker.database.expense.Expense
import com.example.expensetracker.database.expense.ExpenseDao
import com.example.expensetracker.domain.SortFilterOptions
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
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

    override fun getCategoryExpense(categories: List<String>): Single<Int> {
        return dao.getCategoryExpense(categories)
    }

    override fun getById(id: Int): Single<Expense> {
        return dao.getById(id)
    }

    override fun sortAndFilter(sortFilterOptions: SortFilterOptions): Single<List<Expense>> {
        if (sortFilterOptions.dateRange == null && sortFilterOptions.categories.isNotEmpty()) {
            return when (sortFilterOptions.sort) {
                SortFilterOptions.SortOptions.ExpOrderAsc -> {
                    dao.sortByCategoryExpAsc(sortFilterOptions.categories)
                }
                SortFilterOptions.SortOptions.ExpOrderDesc -> {
                    dao.sortByCategoryExpDesc(sortFilterOptions.categories)
                }
                SortFilterOptions.SortOptions.DateOrderAsc -> {
                    dao.sortByCategoryDateAsc(sortFilterOptions.categories)
                }
                SortFilterOptions.SortOptions.DateOrderDesc -> {
                    dao.sortByCategoryDateDesc(sortFilterOptions.categories)
                }
            }
        } else if (sortFilterOptions.dateRange == null && sortFilterOptions.categories.isEmpty()) {
            return when (sortFilterOptions.sort) {
                SortFilterOptions.SortOptions.ExpOrderAsc -> {
                    dao.sortByExpAsc()
                }
                SortFilterOptions.SortOptions.ExpOrderDesc -> {
                    dao.sortByExpDesc()
                }
                SortFilterOptions.SortOptions.DateOrderAsc -> {
                    dao.sortByDateAsc()
                }
                SortFilterOptions.SortOptions.DateOrderDesc -> {
                    dao.sortByDateDesc()
                }
            }
        } else if (sortFilterOptions.dateRange != null && sortFilterOptions.categories.isNotEmpty() ) {
            return when (sortFilterOptions.sort) {
                SortFilterOptions.SortOptions.ExpOrderAsc -> {
                    dao.sortByFilterDateRangeExpAsc(
                        sortFilterOptions.categories,
                        sortFilterOptions.dateRange.startDate,
                        sortFilterOptions.dateRange.endDate
                    )
                }
                SortFilterOptions.SortOptions.ExpOrderDesc -> {
                    dao.sortByCategoryDateRangeExpDesc(
                        sortFilterOptions.categories,
                        sortFilterOptions.dateRange.startDate,
                        sortFilterOptions.dateRange.endDate
                    )
                }
                SortFilterOptions.SortOptions.DateOrderAsc -> {
                    dao.sortByCategoryDateRangeDateAsc(
                        sortFilterOptions.categories,
                        sortFilterOptions.dateRange.startDate,
                        sortFilterOptions.dateRange.endDate
                    )
                }
                SortFilterOptions.SortOptions.DateOrderDesc -> {
                    dao.sortByFilterDateRangeDateDesc(
                        sortFilterOptions.categories,
                        sortFilterOptions.dateRange.startDate,
                        sortFilterOptions.dateRange.endDate
                    )
                }
            }
        } else if (sortFilterOptions.dateRange != null && sortFilterOptions.categories.isEmpty() ) {
            return when (sortFilterOptions.sort) {
                SortFilterOptions.SortOptions.ExpOrderAsc -> {
                    dao.sortByDateRangeExpAsc(
                        sortFilterOptions.dateRange.startDate,
                        sortFilterOptions.dateRange.endDate
                    )
                }
                SortFilterOptions.SortOptions.ExpOrderDesc -> {
                    dao.sortByDateRangeExpDesc(
                        sortFilterOptions.dateRange.startDate,
                        sortFilterOptions.dateRange.endDate
                    )
                }
                SortFilterOptions.SortOptions.DateOrderAsc -> {
                    dao.sortByDateRangeDateAsc(
                        sortFilterOptions.dateRange.startDate,
                        sortFilterOptions.dateRange.endDate
                    )
                }
                SortFilterOptions.SortOptions.DateOrderDesc -> {
                    dao.sortByDateRangeDateDesc(
                        sortFilterOptions.dateRange.startDate,
                        sortFilterOptions.dateRange.endDate
                    )
                }
            }
        } else return dao.getAllExpense()
    }


}