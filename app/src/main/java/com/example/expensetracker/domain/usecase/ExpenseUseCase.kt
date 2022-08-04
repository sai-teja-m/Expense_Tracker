package com.example.expensetracker.domain.usecase

import com.example.expensetracker.database.expense.CategoryAmount
import com.example.expensetracker.database.expense.Expense
import com.example.expensetracker.domain.SortFilterOptions
import com.example.expensetracker.domain.repository.ExpenseRepository
import io.reactivex.Completable
import io.reactivex.Flowable
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


class GetByIdUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
) : SingleUseCaseParams<Expense> {
    override fun execute(data: Any): Single<Expense> {
        return expenseRepo.getById(data as Int)
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
) : SingleUseCaseParams<Int> {
    override fun execute(data: Any): Single<Int> {
        return expenseRepo.getCategoryExpense(data as List<String>)
    }


}

class GetCategoryUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
) : FlowableUseCaseString<String> {
    override fun execute(): Flowable<List<String>> {
        return expenseRepo.getCategory()
    }
}

class GetCategoryAndAmountUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
) : SingleUseCaseParam<CategoryAmount> {
    override fun execute(): Single<List<CategoryAmount>> {
        return expenseRepo.getCategoryAndAmount()
    }
}

class SortFilterOptionsUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
) : SingleUseCaseParams<List<Expense>> {
    override fun execute(data: Any): Single<List<Expense>> {
        return expenseRepo.sortAndFilter(data as SortFilterOptions)
    }

}

interface SingleUseCase<T> {
    fun execute(): Single<T>
}


interface CompletableUseCaseParams<P> {
    fun execute(data: P): Completable
}

interface SingleUseCaseParams<P> {
    fun execute(data: Any): Single<P>
}

interface FlowableUseCaseString<P> {
    fun execute(): Flowable<List<P>>
}

interface SingleUseCaseParam<P> {
    fun execute(): Single<List<P>>
}