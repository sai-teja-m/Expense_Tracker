package com.example.expensetracker

import com.example.expensetracker.database.expense.ExpenseDatabase
import com.example.expensetracker.di.ApplicationComponent
import com.example.expensetracker.di.DaggerApplicationComponent
import com.example.expensetracker.domain.repository.ExpenseRepository
import com.example.expensetracker.domain.repository.ExpenseRepositoryImpl
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject


class ExpenseApplication : DaggerApplication() {
    @Inject
    @Volatile
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>
    lateinit var database: ExpenseDatabase
    lateinit var expenseRepo: ExpenseRepository
    private var appComponent: ApplicationComponent? = null

    override fun onCreate() {
        super.onCreate()

        database = ExpenseDatabase.getDatabase(this)
        expenseRepo = ExpenseRepositoryImpl(database.expenseDao())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent = DaggerApplicationComponent.builder().application(this).build()
        return appComponent!!
    }
}