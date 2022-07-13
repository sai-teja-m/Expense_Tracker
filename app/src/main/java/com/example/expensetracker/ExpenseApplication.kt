package com.example.expensetracker

import android.app.Application
import com.example.expensetracker.database.expense.ExpenseDatabase

import com.example.expensetracker.domain.repository.ExpenseRepository
import com.example.expensetracker.domain.repository.ExpenseRepositoryImpl
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject


class ExpenseApplication :Application() {
    @Inject
    @Volatile
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>
lateinit var database: ExpenseDatabase
lateinit var expenseRepo: ExpenseRepository
    override fun onCreate() {
        super.onCreate()
        if(!::dispatchingAndroidInjector.isInitialized){
            synchronized(this){
                if(!::dispatchingAndroidInjector.isInitialized){
                    val applicationInjector = applicationInjector()
                    applicationInjector.inject(this)
                }
            }
        }
        database = ExpenseDatabase.getDatabase(this)
        expenseRepo =ExpenseRepositoryImpl(database.expenseDao())
    }

    private fun applicationInjector(): AndroidInjector<ExpenseApplication> {
        return DaggerApplicationComponent.builder().build()
    }
}