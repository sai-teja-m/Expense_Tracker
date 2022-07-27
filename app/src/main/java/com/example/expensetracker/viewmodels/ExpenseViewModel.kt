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
    private val getByCatUseCase: GetByCategoryUseCase,
    private val getCatUseCase: GetCategoryUseCase,
    private val getTotalExpenseUseCase: GetTotalExpenseUseCase,
    private val getCategoryExpenseUseCase: GetCategoryExpenseUseCase,
    private val getCategoryAndAmountUseCase: GetCategoryAndAmountUseCase,
    private val getByDateRangeUseCase: GetByDateRangeUseCase,
    private val getByFilterDateRangeUseCase: GetByFilterDateRangeUseCase,
    private val getByFilterDateRangeExpAscUseCase:GetByFilterDateRangeExpAscUseCase,
    private val getByFilterExpAscUseCase:GetByFilterExpAscUseCase,
    private val getByDateRangeExpAscUseCase:GetByDateRangeExpAscUseCase,
    private val getByFilterDateRangeExpDescUseCase:GetByFilterDateRangeExpDescUseCase,
    private val getByFilterExpDescUseCase:GetByFilterExpDescUseCase,
    private val GetByDateRangeExpDescUseCase:GetByDateRangeExpDescUseCase,
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
    private val _selectedCategory: MutableLiveData<String> = MutableLiveData()
    val selectedCategory: LiveData<String> = _selectedCategory
    private val _totalExpense: MutableLiveData<Int> = MutableLiveData()
    val totalExpense: LiveData<Int> = _totalExpense
    private val _categoryExpense: MutableLiveData<Int> = MutableLiveData()
    val categoryExpense: LiveData<Int> = _categoryExpense

    private val _categoryAndAmount: SingleLiveEvent<List<CategoryAmount>> = SingleLiveEvent()
    val categoryAndAmount: LiveData<List<CategoryAmount>> = _categoryAndAmount

    private val _startDate : MutableLiveData<Date> = MutableLiveData()
    val startDate : LiveData<Date> = _startDate

    private val _endDate : MutableLiveData<Date> = MutableLiveData()
    val endDate : LiveData<Date> = _endDate

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

    fun selectCategory(category: String) {
        _selectedCategory.postValue(category)
    }
    fun selectDateRange(start: Date? =null ,end: Date? =null){
        _startDate.postValue(start)
        _endDate.postValue(end)
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

    fun filterByDateRange(start: Date, end: Date){
        getByDateRangeUseCase.execute(start,end).subscribeOn(ioScheduler).observeOn(uiScheduler).subscribe({
            _allExpense.postValue(it)
        },{
            Log.e("filter by date range","error in collecting")
        }).let {
            compositeDisposable.add(it)
        }
    }

    fun filterByDateRangeAndCategory(category: String, start: Date,end: Date){
        getByFilterDateRangeUseCase.execute(category,start, end).subscribeOn(ioScheduler).observeOn(uiScheduler)
            .subscribe({
                _allExpense.postValue(it)
            },{

            }).let {
                compositeDisposable.add(it)
            }
    }

    fun filterByExpenseAsc(category: String,start: Date,end: Date){
        getByFilterDateRangeExpAscUseCase.execute(category,start, end).subscribeOn(ioScheduler).observeOn(uiScheduler)
            .subscribe({
                _allExpense.postValue(it)
            },{

            }).let {
                compositeDisposable.add(it)
            }
    }

    fun filterByCategoryAndExpAsc(category: String){
        getByFilterExpAscUseCase.execute(category).subscribeOn(ioScheduler).observeOn(uiScheduler).subscribe({
            _allExpense.postValue(it)
        }, {
            Log.e("getByCat", "error in get By CAt")
        }).let {
            compositeDisposable.add(it)
        }
    }

    fun filterByDateRangeAndExpAsc(start: Date,end: Date){
        getByDateRangeExpAscUseCase.execute(start,end).subscribeOn(ioScheduler).observeOn(uiScheduler).subscribe({
            _allExpense.postValue(it)
        },{
            Log.e("filter by date range","error in collecting")
        }).let {
            compositeDisposable.add(it)
        }
    }


    fun filterByExpenseDesc(category: String,start: Date,end: Date){
        getByFilterDateRangeExpDescUseCase.execute(category,start, end).subscribeOn(ioScheduler).observeOn(uiScheduler)
            .subscribe({
                _allExpense.postValue(it)
            },{

            }).let {
                compositeDisposable.add(it)
            }
    }

    fun filterByCategoryAndExpDesc(category: String){
        getByFilterExpDescUseCase.execute(category).subscribeOn(ioScheduler).observeOn(uiScheduler).subscribe({
            _allExpense.postValue(it)
        }, {
            Log.e("getByCat", "error in get By CAt")
        }).let {
            compositeDisposable.add(it)
        }
    }

    fun filterByDateRangeAndExpDesc(start: Date,end: Date){
        GetByDateRangeExpDescUseCase.execute(start,end).subscribeOn(ioScheduler).observeOn(uiScheduler).subscribe({
            _allExpense.postValue(it)
        },{
            Log.e("filter by date range","error in collecting")
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

    fun filter(category: String? = null,start: Date? = null, end: Date?= null){
        if(category!= null && category!= "" && (start != null && end!= null)){
            filterByDateRangeAndCategory(category,
                start, end
            )
        }
        else if(category != null && category != ""){
            filterByCategory(category)
        }
        else if(start != null && end!= null){
            filterByDateRange(start,end)
        }
        else{
            getAllExpenses()
        }
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
    private val getCategoryExpenseUseCase: GetCategoryExpenseUseCase,
    private val getCategoryAndAmountUseCase: GetCategoryAndAmountUseCase,
    private val getByDateRangeUseCase: GetByDateRangeUseCase,
    private val getByFilterDateRangeUseCase: GetByFilterDateRangeUseCase,
    private val getByFilterDateRangeExpAscUseCase:GetByFilterDateRangeExpAscUseCase,
    private val getByFilterExpAscUseCase:GetByFilterExpAscUseCase,
    private val getByDateRangeExpAscUseCase:GetByDateRangeExpAscUseCase,
    private val getByFilterDateRangeExpDescUseCase:GetByFilterDateRangeExpDescUseCase,
    private val getByFilterExpDescUseCase:GetByFilterExpDescUseCase,
    private val GetByDateRangeExpDescUseCase:GetByDateRangeExpDescUseCase,

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
                getByCatUseCase,
                getCatUseCase,
                getTotalExpenseUseCase,
                getCategoryExpenseUseCase,
                getCategoryAndAmountUseCase,
                getByDateRangeUseCase,
                getByFilterDateRangeUseCase,
                getByFilterDateRangeExpAscUseCase,
                getByFilterExpAscUseCase,
                getByDateRangeExpAscUseCase,
                getByFilterDateRangeExpDescUseCase,
                getByFilterExpDescUseCase,
                GetByDateRangeExpDescUseCase,
                converters
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}