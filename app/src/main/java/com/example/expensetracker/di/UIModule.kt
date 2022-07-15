package com.example.expensetracker.di

import com.example.expensetracker.domain.usecase.*
import com.example.expensetracker.ui.activity.MainActivity
import com.example.expensetracker.ui.fragments.AddEditExpenseFragment
import com.example.expensetracker.ui.fragments.CategoryFragment
import com.example.expensetracker.ui.fragments.DatePickerFragment
import com.example.expensetracker.ui.fragments.ExpenseListFragment
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
    abstract fun bindsMainActivity(): MainActivity

    companion object {
        @Provides
        @JvmStatic
        fun provideExpenseViewModelFactory(
            insertExpenseUseCase: InsertExpenseUseCase,
            updateExpenseUseCase: UpdateExpenseUseCase,
            deleteExpenseUseCase: DeleteExpenseUseCase,
            getAllExpensesUseCase: GetAllUseCase,
            getByCatUseCase: GetByCatUseCase,
            getCatUseCase: GetCategoryUseCase,
            getTotalExpenseUseCase: GetTotalExpenseUseCase,
            getCategoryExpenseUseCase: GetCategoryExpenseUseCase
        ) = ExpenseViewModelFactory(
            insertExpenseUseCase,
            updateExpenseUseCase,
            deleteExpenseUseCase,
            getAllExpensesUseCase,
            getByCatUseCase,
            getCatUseCase,
            getTotalExpenseUseCase,
            getCategoryExpenseUseCase
        )
    }
}