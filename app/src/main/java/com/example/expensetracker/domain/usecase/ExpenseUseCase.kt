package com.example.expensetracker.domain.usecase

import com.example.expensetracker.database.expense.Expense
import com.example.expensetracker.domain.repository.ExpenseRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.internal.operators.single.SingleLift


class UpdateExpenseUseCase(
    private val expenseRepo : ExpenseRepository
) : CompletableUseCaseParams<Expense>{

    override fun execute(data:Expense): Completable {
        return expenseRepo.updateExpense(data)
    }

}

class InsertExpenseUseCase(
    private val expenseRepo : ExpenseRepository
) : CompletableUseCaseParams<Expense>{

    override fun execute(data: Expense): Completable {
        return expenseRepo.insertExpense(data)
    }
}

class DeleteExpenseUseCase(
    private val expenseRepo: ExpenseRepository
):CompletableUseCaseParams<Expense>{
    override fun execute(data: Expense): Completable {
        return expenseRepo.deleteExpense(data)
    }
}

class GetAllUseCase(
    private val expenseRepo: ExpenseRepository
):SingleUseCase<List<Expense>>{
    override fun execute(): Single<List<Expense>> {
        return expenseRepo.getAll()
    }
}

class GetByCatUseCase(
    private val expenseRepo: ExpenseRepository
):SingleUseCaseListParams<List<Expense>>{
    override fun execute(data:String): Single<List<Expense>> {
        return expenseRepo.getByCat(data)
    }
}

class GetByIdUseCase(
    private val expenseRepo: ExpenseRepository
):SingleUseCaseParams<Expense>{
    override fun execute(data: Int): Single<Expense> {
        return expenseRepo.getById(data)
    }
}

class GetCatUseCase(
    private val expenseRepo: ExpenseRepository
):SingleUseCaseString<String>{
    override fun execute(): Single<List<String>> {
        return expenseRepo.getCat()
    }
}

interface SingleUseCase<T> {
    fun execute() : Single<T>
}


interface CompletableUseCaseParams<P> {
    fun execute(data : P): Completable
}

interface SingleUseCaseListParams<P>{
    fun execute(data:String) : Single<P>
}
interface SingleUseCaseParams<P>{
    fun execute(data:Int) : Single<P>
}

interface SingleUseCaseString<P>{
    fun execute(): Single<List<P>>
}