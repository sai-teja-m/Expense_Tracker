package com.example.expensetracker.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.R
import com.example.expensetracker.database.expense.Expense
import com.example.expensetracker.domain.usecase.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class ExpenseViewModel(
    private val insertUse: InsertExpenseUseCase,
    private val updateUse: UpdateExpenseUseCase,
    private val deleteCase: DeleteExpenseUseCase,
    private val getAllUse: GetAllUseCase,
    private val getByCatUseCase: GetByCategoryUseCase,
    private val getCatUseCase: GetCategoryUseCase,
    private val getTotalExpenseUseCase: GetTotalExpenseUseCase,
    private val getCategoryExpenseUseCase: GetCategoryExpenseUseCase
) : ViewModel() {

    private val uiScheduler by lazy { AndroidSchedulers.mainThread() }
    private val ioScheduler by lazy { Schedulers.io() }
    private val disposableDelegate = lazy { CompositeDisposable() }
    private val compositeDisposable by disposableDelegate


    private val _allExpense: MutableLiveData<List<Expense>> = MutableLiveData()
    val allExpense: LiveData<List<Expense>> = _allExpense
    private val _categoryList: MutableLiveData<List<String>> = MutableLiveData()
    val categoryList: LiveData<List<String>> = _categoryList
    private val _selectedCategory: MutableLiveData<String> = MutableLiveData()
    val selectedCategory: LiveData<String> = _selectedCategory
    private val _totalExpense: MutableLiveData<Int> = MutableLiveData()
    val totalExpense: LiveData<Int> = _totalExpense
    private val _categoryExpense: MutableLiveData<Int> = MutableLiveData()
    val categoryExpense: LiveData<Int> = _categoryExpense

    fun isEntryValid(
        expenseTitle: String,
        expense: String,
        `when`: String,
        category: String,
        errorMap: MutableMap<Int, Int>
    ): Boolean {
        var validEntry = true
        if (expenseTitle.isBlank()) {
            errorMap[1] = R.string.empty_title_msg
            validEntry = false
        }
        if (expense.isBlank()) {
            errorMap[2] = R.string.empty_amount_msg
            validEntry = false
        }
        if (category.isBlank()) {
            errorMap[3] = R.string.empty_category_msg
            validEntry = false
        }
        if (`when`.isBlank()) {
            errorMap[4] = R.string.empty_date_msg
            validEntry = false
        }
        return validEntry
    }

    fun selectCategory(category: String) {
        _selectedCategory.postValue(category)
    }

    fun getTotalExpense() {
        getTotalExpenseUseCase.execute().subscribeOn(ioScheduler).observeOn(uiScheduler).subscribe({
            _totalExpense.postValue(it)
        }, {
            Log.e("TotalExpenseViewModel", "Error in Getting Total Expense")
        }).let {
            compositeDisposable.add(it)
        }
    }

    fun getCategoryExpense(category: String) {
        getCategoryExpenseUseCase.execute(category).subscribeOn(ioScheduler).observeOn(uiScheduler)
            .subscribe({
                _categoryExpense.postValue(it)
            }, {
                Log.e("CategoryExpenseVM", "Error in Getting Category Expense")
            }).let {
                compositeDisposable.add(it)
            }
    }

    fun clearCategoryExpense() {
        _categoryExpense.postValue(null)
    }

    fun getCategory() {
        getCatUseCase
            .execute()
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe(
                {
                    _categoryList.postValue(it)
                }, {
                    Log.e("catList", "Error in Generating a List")
                }).let {
                compositeDisposable.add(it)
            }
    }


    fun getAllExpenses() {
        getAllUse
            .execute().subscribeOn(ioScheduler).observeOn(uiScheduler).subscribe({ list ->
                _allExpense.postValue(list)
            }, {
                Log.e("getAllError", "error in loading DB in getALL")
            }
            ).let {
                compositeDisposable.add(it)
            }
    }

    fun addNewExpense(expenseTitle: String, expense: String, `when`: String, category: String) {
        Log.d("addExp", "$expenseTitle")
        val newExpense = getNewExpenseEntry(expenseTitle, expense, `when`, category)
        insertUse.execute(newExpense).subscribeOn(ioScheduler).observeOn(uiScheduler).subscribe({
            getAllExpenses()
        }, {
            Log.e("addNewExpense", "error in loading new list ")
        }).let {
            compositeDisposable.add(it)
        }
    }

    fun updateExpense(exp: Expense) {

        updateUse.execute(exp).subscribeOn(ioScheduler).observeOn(uiScheduler).subscribe({
            getAllExpenses()
        }, {
            Log.e("updateUse", "error in Loading new list from updateUse")
        }).let {
            compositeDisposable.add(it)
        }
    }

    fun deleteExpense(expense: Expense) {

        deleteCase.execute(expense).subscribeOn(ioScheduler).observeOn(uiScheduler).subscribe({
            getAllExpenses()
        }, {
            Log.e("deleteUse", "error in delete Use")
        }).let {
            compositeDisposable.add(it)
        }
    }

    fun filterByCategory(cat: String) {
        getByCatUseCase.execute(cat).subscribeOn(ioScheduler).observeOn(uiScheduler).subscribe({
            _allExpense.postValue(it)
        }, {
            Log.e("getByCat", "error in get By CAt")
        }).let {
            compositeDisposable.add(it)
        }
    }


    private fun getNewExpenseEntry(
        expenseTitle: String,
        expense: String,
        `when`: String,
        category: String
    ): Expense {
        return Expense(
            expenseTitle = expenseTitle,
            expense = expense.toInt(),
            `when` = `when`,
            category = category
        )
    }

}

class ExpenseViewModelFactory @Inject constructor(
    private val insertUse: InsertExpenseUseCase,
    private val updateUse: UpdateExpenseUseCase,
    private val deleteCase: DeleteExpenseUseCase,
    private val getAllUse: GetAllUseCase,
    private val getByCatUseCase: GetByCategoryUseCase,
    private val getCatUseCase: GetCategoryUseCase,
    private val getTotalExpenseUseCase: GetTotalExpenseUseCase,
    private val getCategoryExpenseUseCase: GetCategoryExpenseUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(
                insertUse,
                updateUse,
                deleteCase,
                getAllUse,
                getByCatUseCase,
                getCatUseCase,
                getTotalExpenseUseCase,
                getCategoryExpenseUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}