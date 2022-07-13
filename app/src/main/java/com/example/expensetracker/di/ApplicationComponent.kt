package com.example.expensetracker.di

import dagger.Component

@Component( modules = [AppModule::class])
interface ApplicationComponent{

    @Component.Builder
    interface Builder{
        fun build() : ApplicationComponent
    }
}