package com.example.expensetracker.di

import com.example.expensetracker.database.expense.DateConverter
import com.example.expensetracker.domain.usecase.*
import com.example.expensetracker.ui.activity.MainActivity
import com.example.expensetracker.ui.fragments.*
import com.example.expensetracker.viewmodels.ExpenseViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class UIModule {


    @ContributesAndroidInjector
    abstract fun bindsListDisplay(): ExpenseListFragment

    @ContributesAndroidInjector
    abstract fun bindsAddEdit(): AddEditExpenseFragment

    @ContributesAndroidInjector
    abstract fun bindsCategoryFragment(): CategoryFragment

    @ContributesAndroidInjector
    abstract fun bindsDatePickerFragment(): DatePickerFragment

    @ContributesAndroidInjector
    abstract fun bindsGraphFragment() : GraphFragment

    @ContributesAndroidInjector
    abstract fun bindsMainActivity(): MainActivity

    companion object {
        @Provides
        @JvmStatic
        fun provideExpenseViewModelFactory(
            insertExpenseUseCase: InsertExpenseUseCase,
            updateExpenseUseCase: UpdateExpenseUseCase,
            deleteExpenseUseCase: DeleteExpenseUseCase,
            getAllExpensesUseCase: GetAllUseCase,
            getByCategoryUseCase: GetByCategoryUseCase,
            getCategoryUseCase: GetCategoryUseCase,
            getTotalExpenseUseCase: GetTotalExpenseUseCase,
            getCategoryExpenseUseCase: GetCategoryExpenseUseCase,
            getCategoryAndAmountUseCase: GetCategoryAndAmountUseCase,
            dateConverter: DateConverter
        ) = ExpenseViewModelFactory(
            insertExpenseUseCase,
            updateExpenseUseCase,
            deleteExpenseUseCase,
            getAllExpensesUseCase,
            getByCategoryUseCase,
            getCategoryUseCase,
            getTotalExpenseUseCase,
            getCategoryExpenseUseCase,
            getCategoryAndAmountUseCase,
            dateConverter
        )
    }
}