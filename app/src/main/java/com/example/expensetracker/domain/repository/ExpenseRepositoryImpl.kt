package com.example.expensetracker.domain.repository

import com.example.expensetracker.database.expense.Expense
import com.example.expensetracker.database.expense.ExpenseDao
import io.reactivex.Completable
import io.reactivex.Single
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

    override fun getAll(): Single<List<Expense>> {
        return dao.getAll()
    }

    override fun getCat() : Single<List<String>>{
        return dao.getCat()
    }

    override fun getByCat(cat: String): Single<List<Expense>> {
        return dao.getByCat(cat)
    }
    override fun getById(id: Int): Single<Expense> {
        return dao.getById(id)
    }


}