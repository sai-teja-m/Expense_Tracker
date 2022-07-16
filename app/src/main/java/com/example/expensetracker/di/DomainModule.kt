package com.example.expensetracker.di

import android.app.Application
import com.example.expensetracker.database.expense.ExpenseDao
import com.example.expensetracker.database.expense.ExpenseDatabase
import com.example.expensetracker.domain.repository.ExpenseRepository
import com.example.expensetracker.domain.repository.ExpenseRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class DomainModule {
    companion object {
        @Provides
        @Singleton
        @JvmStatic
        fun provideExpenseDatabase(app: Application): ExpenseDatabase {
            return ExpenseDatabase.getDatabase(app)
        }

        @Provides
        @Singleton
        @JvmStatic
        fun provideExpenseDao(db: ExpenseDatabase): ExpenseDao {
            return db.expenseDao()
        }
    }


    @Binds
    @Singleton
    abstract fun bindsExpenseRepository(expenseRepo: ExpenseRepositoryImpl): ExpenseRepository
}