package com.example.expensetracker.di

import android.app.Application
import com.example.expensetracker.ExpenseApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component( modules = [AppModule::class, AndroidInjectionModule::class])
interface ApplicationComponent : AndroidInjector<ExpenseApplication>{

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(application: Application): Builder
        fun build() : ApplicationComponent
    }
}