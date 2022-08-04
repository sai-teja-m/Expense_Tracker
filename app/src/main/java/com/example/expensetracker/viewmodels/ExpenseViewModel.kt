package com.example.expensetracker.viewmodels

import SingleLiveEvent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.R
import com.example.expensetracker.database.expense.CategoryAmount
import com.example.expensetracker.database.expense.DateConverter
import com.example.expensetracker.database.expense.Expense
import com.example.expensetracker.domain.SortFilterOptions
import com.example.expensetracker.domain.usecase.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject


class ExpenseViewModel(
    private val insertUse: InsertExpenseUseCase,
    private val updateUse: UpdateExpenseUseCase,
    private val deleteCase: DeleteExpenseUseCase,
    private val getAllUse: GetAllUseCase,
    private val getCatUseCase: GetCategoryUseCase,
    private val getTotalExpenseUseCase: GetTotalExpenseUseCase,
    private val getCategoryExpenseUseCase: GetCategoryExpenseUseCase,
    private val getCategoryAndAmountUseCase: GetCategoryAndAmountUseCase,
    private val sortFilterOptionsUseCase: SortFilterOptionsUseCase,
    private val converters: DateConverter
) : ViewModel() {

    private val uiScheduler by lazy { AndroidSchedulers.mainThread() }
    private val ioScheduler by lazy { Schedulers.io() }
    private val disposableDelegate = lazy { CompositeDisposable() }
    private val compositeDisposable by disposableDelegate


    private val _allExpense: MutableLiveData<List<Expense>> = MutableLiveData()
    val allExpense: LiveData<List<Expense>> = _allExpense

    private val _categoryList: MutableLiveData<List<String>> = MutableLiveData()
    val categoryList: LiveData<List<String>> = _categoryList

    private val _selectedCategory: MutableLiveData<List<String>> = MutableLiveData()
    val selectedCategory: LiveData<List<String>> = _selectedCategory

    private val _totalExpense: MutableLiveData<Int> = MutableLiveData()
    val totalExpense: LiveData<Int> = _totalExpense

    private val _categoryExpense: MutableLiveData<Int> = MutableLiveData()
    val categoryExpense: LiveData<Int> = _categoryExpense

    private val _categoryAndAmount: SingleLiveEvent<List<CategoryAmount>> = SingleLiveEvent()
    val categoryAndAmount: LiveData<List<CategoryAmount>> = _categoryAndAmount

    private val _startDate: MutableLiveData<Date> = MutableLiveData()
    val startDate: LiveData<Date> = _startDate

    private val _endDate: MutableLiveData<Date> = MutableLiveData()
    val endDate: LiveData<Date> = _endDate

    private val _expenseOrder: MutableLiveData<Int> = MutableLiveData(3)
    val expenseOrder: LiveData<Int> = _expenseOrder

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
        if (expense.isBlank() || expense.toInt() > 2147483646) {
            if (expense.isBlank())
                errorMap[2] = R.string.empty_amount_msg
            else
                errorMap[2] = R.string.exceed_amount_msg
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

    fun selectCategory(categories: List<String>) {
        _selectedCategory.postValue(categories)
    }

    fun selectDateRange(start: Date? = null, end: Date? = null) {
        _startDate.postValue(start)
        _endDate.postValue(end)
    }

    //default is date Desc -1 ,for exp Asc 0 exp Desc 1 Date Asc 2 Date Desc 3
    fun setExpenseOrder(order: Int = 3) {
        _expenseOrder.postValue(order)
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

    fun getCategoryExpense(category: List<String>) {
        getCategoryExpenseUseCase.execute(category).subscribeOn(ioScheduler).observeOn(uiScheduler)
            .subscribe({
                _categoryExpense.postValue(it)
            }, {
                Log.e("CategoryExpenseVM", "Error in Getting Category Expense")
            }).let {
                compositeDisposable.add(it)
            }
    }

    fun getCategoryAndAmount() {
        getCategoryAndAmountUseCase.execute().subscribeOn(ioScheduler).observeOn(uiScheduler)
            .subscribe({
                _categoryAndAmount.postValue(it)
            }, {
                Log.e("CategoryAndAmount", "error in getting categories and amounts")
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
            }, { err ->
                Log.e("get ALL Exp", err.toString())
            }
            ).let {
                compositeDisposable.add(it)
            }
    }

    fun addNewExpense(expenseTitle: String, expense: String, `when`: Date?, category: String) {
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

    private fun getNewExpenseEntry(
        expenseTitle: String,
        expense: String,
        `when`: Date?,
        category: String
    ): Expense {
        return Expense(
            expenseTitle = expenseTitle,
            expense = expense.toInt(),
            `when` = `when`,
            category = category
        )
    }

    override fun onCleared() {
        super.onCleared()
        if (disposableDelegate.isInitialized()) {
            compositeDisposable.dispose()
            compositeDisposable.clear()
        }
    }

    private fun sortFilter(sort: SortFilterOptions) {
        sortFilterOptionsUseCase.execute(sort).subscribeOn(ioScheduler)
            .observeOn(uiScheduler).subscribe({
                _allExpense.postValue(it)
            }, {
                Log.e("SortFilterOptionsFun", "error in getting expenses in Sort Filter options ")
            }).let {
                compositeDisposable.add(it)
            }
    }

    fun filter(sort: SortFilterOptions) {
        sortFilter(sort)
    }


}


class ExpenseViewModelFactory @Inject constructor(
    private val insertUse: InsertExpenseUseCase,
    private val updateUse: UpdateExpenseUseCase,
    private val deleteCase: DeleteExpenseUseCase,
    private val getAllUse: GetAllUseCase,
    private val getCatUseCase: GetCategoryUseCase,
    private val getTotalExpenseUseCase: GetTotalExpenseUseCase,
    private val getCategoryExpenseUseCase: GetCategoryExpenseUseCase,
    private val getCategoryAndAmountUseCase: GetCategoryAndAmountUseCase,
    private val sortFilterOptionsUseCase: SortFilterOptionsUseCase,
    private val converters: DateConverter
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(
                insertUse,
                updateUse,
                deleteCase,
                getAllUse,
                getCatUseCase,
                getTotalExpenseUseCase,
                getCategoryExpenseUseCase,
                getCategoryAndAmountUseCase,
                sortFilterOptionsUseCase,
                converters
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}