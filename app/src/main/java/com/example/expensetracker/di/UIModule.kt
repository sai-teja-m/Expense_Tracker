package com.example.expensetracker.di

import com.example.expensetracker.*
import com.example.expensetracker.domain.usecase.*
import com.example.expensetracker.viewmodels.ExpenseViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class UIModule {

//    @Binds
//    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

//    @Binds
//    @IntoMap
//    @ViewModelKey(ExpenseViewModel::class)
//    abstract fun provideExpenseViewModel(viewModel: ExpenseViewModel) : ViewModel

    @ContributesAndroidInjector
    abstract  fun bindsListDisplay() : ListDisplay

    @ContributesAndroidInjector
    abstract  fun bindsAddEdit() : AddEdit

    @ContributesAndroidInjector
    abstract fun bindsCategoryFragment() : CategoryFragment

    @ContributesAndroidInjector
    abstract fun bindsDatePickerFragment() : DatePickerFragment

    @ContributesAndroidInjector
    abstract fun bindsMainActivity() : MainActivity

    companion object {
        @Provides
        @JvmStatic
        fun provideExpenseViewModelFactory(
            insertExpenseUseCase: InsertExpenseUseCase,
            updateExpenseUseCase: UpdateExpenseUseCase,
            deleteExpenseUseCase: DeleteExpenseUseCase,
            getAllExpensesUseCase: GetAllUseCase,
            getByCatUseCase: GetByCatUseCase,
            getCatUseCase: GetCatUseCase,
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