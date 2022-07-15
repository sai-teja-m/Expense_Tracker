package com.example.expensetracker.domain.usecase

import com.example.expensetracker.database.expense.Expense
import com.example.expensetracker.domain.repository.ExpenseRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject


class UpdateExpenseUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
) : CompletableUseCaseParams<Expense> {

    override fun execute(data: Expense): Completable {
        return expenseRepo.updateExpense(data)
    }

}

class InsertExpenseUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
) : CompletableUseCaseParams<Expense> {

    override fun execute(data: Expense): Completable {
        return expenseRepo.insertExpense(data)
    }
}

class DeleteExpenseUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
) : CompletableUseCaseParams<Expense> {
    override fun execute(data: Expense): Completable {
        return expenseRepo.deleteExpense(data)
    }
}

class GetAllUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
) : SingleUseCase<List<Expense>> {
    override fun execute(): Single<List<Expense>> {
        return expenseRepo.getAllExpense()
    }
}

class GetByCatUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
) : SingleUseCaseListParams<List<Expense>> {
    override fun execute(data: String): Single<List<Expense>> {
        return expenseRepo.getByCategory(data)
    }
}

class GetByIdUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
) : SingleUseCaseParams<Expense> {
    override fun execute(data: Int): Single<Expense> {
        return expenseRepo.getById(data)
    }
}

class GetTotalExpenseUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
) : SingleUseCase<Int> {
    override fun execute(): Single<Int> {
        return expenseRepo.getTotalExpense()
    }
}

class GetCategoryExpenseUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
) : SingleUseCaseListParams<Int> {
    override fun execute(data: String): Single<Int> {
        return expenseRepo.getCategoryExpense(data)
    }
}

class GetCatUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
) : SingleUseCaseString<String> {
    override fun execute(): Single<List<String>> {
        return expenseRepo.getCategory()
    }
}

interface SingleUseCase<T> {
    fun execute(): Single<T>
}


interface CompletableUseCaseParams<P> {
    fun execute(data: P): Completable
}

interface SingleUseCaseListParams<P> {
    fun execute(data: String): Single<P>
}

interface SingleUseCaseParams<P> {
    fun execute(data: Int): Single<P>
}

interface SingleUseCaseString<P> {
    fun execute(): Single<List<P>>
}