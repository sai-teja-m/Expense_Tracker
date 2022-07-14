package com.example.expensetracker.di

import dagger.Module


@Module(includes = [UIModule::class, DomainModule::class])
abstract class AppModule