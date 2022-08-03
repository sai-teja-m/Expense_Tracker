package com.example.expensetracker.domain.usecase

import com.example.expensetracker.database.expense.CategoryAmount
import com.example.expensetracker.database.expense.Expense
import com.example.expensetracker.domain.repository.ExpenseRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*
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

class GetByCategoryUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
) : SingleUseCaseListParams<List<Expense>> {
    override fun execute(data: List<String>): Single<List<Expense>> {
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
    override fun execute(data: List<String>): Single<Int> {
        return expenseRepo.getCategoryExpense(data)
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

class GetByDateRangeUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
) : SingleUseCaseDateRange<List<Expense>>{
    override fun execute(start:Date,end: Date): Single<List<Expense>>{
            return expenseRepo.getByDateRange(start, end)
    }
}

class GetByFilterDateRangeUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
):SingleUseCaseDateRangeFilter<List<Expense>>{
    override fun execute(category: List<String>,start: Date,end: Date): Single<List<Expense>> {
        return expenseRepo.sortByCategoryDateRange(category, start, end)
    }
}

class GetByExpenseAsc @Inject constructor(
    private val expenseRepo: ExpenseRepository
): SingleUseCase<List<Expense>>{
    override fun execute(): Single<List<Expense>> {
        return expenseRepo.sortByExpenseAsc()
    }
}

class GetByFilterDateRangeExpAscUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
): SingleUseCaseDateRangeFilter<List<Expense>>{
    override fun execute(category: List<String>, start: Date, end: Date): Single<List<Expense>> {
        return expenseRepo.sortByCategoryDateRangeExpAsc(category, start, end)
    }
}

class GetByFilterExpAscUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
):SingleUseCaseListParams<List<Expense>>{
    override fun execute(data: List<String>): Single<List<Expense>> {
        return expenseRepo.sortByCategoryExpenseAsc(data)
    }
}

class GetByDateRangeExpAscUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
) : SingleUseCaseDateRange<List<Expense>>{
    override fun execute(start: Date, end: Date): Single<List<Expense>> {
        return expenseRepo.sortByDateRangeExpAsc(start, end)
    }
}

class GetByExpenseDesc @Inject constructor(
    private val expenseRepo: ExpenseRepository
): SingleUseCase<List<Expense>>{
    override fun execute(): Single<List<Expense>> {
        return expenseRepo.sortByExpenseDesc()
    }
}

class GetByFilterDateRangeExpDescUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
): SingleUseCaseDateRangeFilter<List<Expense>>{
    override fun execute(category: List<String>, start: Date, end: Date): Single<List<Expense>> {
        return expenseRepo.sortByCategoryDateRangeExpDesc(category, start, end)
    }
}

class GetByFilterExpDescUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
): SingleUseCaseListParams<List<Expense>>{
    override fun execute(data: List<String>): Single<List<Expense>> {
        return expenseRepo.sortByCategoryExpDesc(data)
    }
}

class GetByDateRangeExpDescUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
): SingleUseCaseDateRange<List<Expense>>{
    override fun execute(start: Date, end: Date): Single<List<Expense>> {
        return  expenseRepo.sortByDateRangeExpDesc(start, end)
    }
}

//SOrt Asc Use cases
class SortByDateAscUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
): SingleUseCase<List<Expense>>{
    override fun execute(): Single<List<Expense>> {
        return expenseRepo.sortByDateAsc()
    }
}

class SortByCategoryDateRangDateAscUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
): SingleUseCaseDateRangeFilter<List<Expense>>{
    override fun execute(category: List<String>, start: Date, end: Date): Single<List<Expense>> {
        return expenseRepo.sortByCategoryDateRangeDateAsc(category, start, end)
    }
}

class SortByCategoryDateAscUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
):SingleUseCaseListParams<List<Expense>>{
    override fun execute(data: List<String>): Single<List<Expense>> {
        return expenseRepo.sortByCategoryDateAsc(data)
    }
}

class SortByDateRangeDateAscUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
) : SingleUseCaseDateRange<List<Expense>>{
    override fun execute(start: Date, end: Date): Single<List<Expense>> {
        return expenseRepo.sortByDateRangeDateAsc(start, end)
    }
}

//sort Desc Use Cases
class SortByDateDescUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
): SingleUseCase<List<Expense>>{
    override fun execute(): Single<List<Expense>> {
        return expenseRepo.sortByDateDesc()
    }
}

class SortByCategoryDateRangDateDescUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
): SingleUseCaseDateRangeFilter<List<Expense>>{
    override fun execute(category: List<String>, start: Date, end: Date): Single<List<Expense>> {
        return expenseRepo.sortByCategoryDateRangeDateDesc(category, start, end)
    }
}

class SortByCategoryDateDescUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
):SingleUseCaseListParams<List<Expense>>{
    override fun execute(data: List<String>): Single<List<Expense>> {
        return expenseRepo.sortByCategoryDateDesc(data)
    }
}

class SortByDateRangeDateDescUseCase @Inject constructor(
    private val expenseRepo: ExpenseRepository
) : SingleUseCaseDateRange<List<Expense>>{
    override fun execute(start: Date, end: Date): Single<List<Expense>> {
        return expenseRepo.sortByDateRangeDateDesc(start, end)
    }
}





interface SingleUseCase<T> {
    fun execute(): Single<T>
}


interface CompletableUseCaseParams<P> {
    fun execute(data: P): Completable
}

interface SingleUseCaseListParams<P> {
    fun execute(data: List<String>): Single<P>
}

interface SingleUseCaseParams<P> {
    fun execute(data: Int): Single<P>
}
interface SingleUseCaseDateRange<P> {
    fun execute(start:Date ,end: Date): Single<P>
}

interface SingleUseCaseDateRangeFilter<P> {
    fun execute(category: List<String>,start: Date , end: Date): Single<P>
}

interface FlowableUseCaseString<P> {
    fun execute(): Flowable<List<P>>
}

interface SingleUseCaseParam<P> {
    fun execute(): Single<List<P>>
}