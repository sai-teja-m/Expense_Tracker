package com.example.expensetracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider


    @AppScope
    class ViewModelFactory @Inject constructor(
        private val viewModels: MutableMap<Class<out ViewModel>,
                @JvmSuppressWildcards Provider<ViewModel>>
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            viewModels[modelClass]?.get() as T
    }
