package com.example.expensetracker.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.*
import com.example.expensetracker.ViewModelKey
import com.example.expensetracker.viewmodels.ExpenseViewModel
import com.example.expensetracker.viewmodels.ExpenseViewModelFactory
import dagger.Binds
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import javax.inject.Inject


abstract class UIModule {
    @Binds
    internal abstract fun bindExpenseViewModelFactory(factory : ExpenseViewModelFactory) :ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ExpenseViewModel::class)
    abstract fun provideExpenseViewModel(viewModel: ExpenseViewModel) : ViewModel

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
}