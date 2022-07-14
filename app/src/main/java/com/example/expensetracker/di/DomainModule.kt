package com.example.expensetracker.di

import android.app.Application
import com.example.expensetracker.AppScope
import com.example.expensetracker.database.expense.ExpenseDao
import com.example.expensetracker.database.expense.ExpenseDatabase
import com.example.expensetracker.domain.repository.ExpenseRepository
import com.example.expensetracker.domain.repository.ExpenseRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class DomainModule {
    companion object{
        @Provides
        @JvmStatic
        fun provideExpenseDatabase(app: Application) : ExpenseDatabase {
            return ExpenseDatabase.getDatabase(app)
        }

        @Provides
        @JvmStatic
        fun provideExpenseDao(db : ExpenseDatabase): ExpenseDao {
            return db.expenseDao()
        }
    }


    @Binds
    abstract fun bindsExpenseRepository(expenseRepo : ExpenseRepositoryImpl) : ExpenseRepository
}