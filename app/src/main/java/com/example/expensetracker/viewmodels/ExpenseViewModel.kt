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
    private val getByFilterDateRangeExpAscUseCase: GetByFilterDateRangeExpAscUseCase,
    private val getByFilterExpAscUseCase: GetByFilterExpAscUseCase,
    private val getByDateRangeExpAscUseCase: GetByDateRangeExpAscUseCase,
    private val getByFilterDateRangeExpDescUseCase: GetByFilterDateRangeExpDescUseCase,
    private val getByFilterExpDescUseCase: GetByFilterExpDescUseCase,
    private val GetByDateRangeExpDescUseCase: GetByDateRangeExpDescUseCase,
    private val getByExpenseAsc: GetByExpenseAsc,
    private val getByExpenseDesc: GetByExpenseDesc,
    private val sortByDateAscUseCase: SortByDateAscUseCase,
    private val sortByCategoryDateRangeDateAscUseCase: SortByCategoryDateRangDateAscUseCase,
    private val sortByCategoryDateAscUseCase: SortByCategoryDateAscUseCase,
    private val sortByDateRangeDateAscUseCase: SortByDateRangeDateAscUseCase,
    private val sortByDateDescUseCase: SortByDateDescUseCase,
    private val sortByCategoryDateRangeDateDescUseCase: SortByCategoryDateRangDateDescUseCase,
    private val sortByCategoryDateDescUseCase: SortByCategoryDateDescUseCase,
    private val sortByDateRangeDateDescUseCase: SortByDateRangeDateDescUseCase,
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

    private val _startDate: MutableLiveData<Date> = MutableLiveData()
    val startDate: LiveData<Date> = _startDate

    private val _endDate: MutableLiveData<Date> = MutableLiveData()
    val endDate: LiveData<Date> = _endDate

    private val _expenseOrder: MutableLiveData<Int> = MutableLiveData()
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

    fun selectCategory(category: String) {
        _selectedCategory.postValue(category)
    }

    fun selectDateRange(start: Date? = null, end: Date? = null) {
        _startDate.postValue(start)
        _endDate.postValue(end)
    }

    //default is date Desc -1 ,for exp Asc 0 exp Desc 1 Date Asc 2 Date Desc 3
    fun setExpenseOrder(order: Int = -1) {
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

    private fun filterByCategory(cat: String) {
        getByCatUseCase.execute(cat).subscribeOn(ioScheduler).observeOn(uiScheduler).subscribe({
            _allExpense.postValue(it)
        }, {
            Log.e("getByCat", "error in get By CAt")
        }).let {
            compositeDisposable.add(it)
        }
    }

    private fun filterByDateRange(start: Date, end: Date) {
        getByDateRangeUseCase.execute(start, end).subscribeOn(ioScheduler).observeOn(uiScheduler)
            .subscribe({
                _allExpense.postValue(it)
            }, {
                Log.e("filter by date range", "error in collecting")
            }).let {
            compositeDisposable.add(it)
        }
    }

    private fun filterByDateRangeAndCategory(category: String, start: Date, end: Date) {
        getByFilterDateRangeUseCase.execute(category, start, end).subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe({
                _allExpense.postValue(it)
            }, {

            }).let {
                compositeDisposable.add(it)
            }
    }

    private fun getByExpenseAsc() {
        getByExpenseAsc.execute().subscribeOn(ioScheduler).observeOn(uiScheduler)
            .subscribe({
                _allExpense.postValue(it)
            }, {
                Log.e("ExpenseAsc", "error in Sort exp Asc")
            }).let {
                compositeDisposable.add(it)
            }
    }

    private fun filterByCategoryDateExpenseAsc(category: String, start: Date, end: Date) {
        getByFilterDateRangeExpAscUseCase.execute(category, start, end).subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe({
                _allExpense.postValue(it)
            }, {

            }).let {
                compositeDisposable.add(it)
            }
    }

    private fun filterByCategoryAndExpAsc(category: String) {
        getByFilterExpAscUseCase.execute(category).subscribeOn(ioScheduler).observeOn(uiScheduler)
            .subscribe({
                _allExpense.postValue(it)
            }, {
                Log.e("getByCat", "error in get By CAt")
            }).let {
            compositeDisposable.add(it)
        }
    }

    private fun filterByDateRangeAndExpAsc(start: Date, end: Date) {
        getByDateRangeExpAscUseCase.execute(start, end).subscribeOn(ioScheduler)
            .observeOn(uiScheduler).subscribe({
            _allExpense.postValue(it)
        }, {
            Log.e("filter by date range", "error in collecting")
        }).let {
            compositeDisposable.add(it)
        }
    }

    private fun getByExpenseDesc() {
        getByExpenseDesc.execute().subscribeOn(ioScheduler).observeOn(uiScheduler)
            .subscribe({
                _allExpense.postValue(it)
            }, {
                Log.e("ExpenseDesc", "error in Sort exp Desc")
            }).let {
                compositeDisposable.add(it)
            }
    }

    private fun filterByCategoryDateExpenseDesc(category: String, start: Date, end: Date) {
        getByFilterDateRangeExpDescUseCase.execute(category, start, end).subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe({
                _allExpense.postValue(it)
            }, {

            }).let {
                compositeDisposable.add(it)
            }
    }

    private fun filterByCategoryAndExpDesc(category: String) {
        getByFilterExpDescUseCase.execute(category).subscribeOn(ioScheduler).observeOn(uiScheduler)
            .subscribe({
                _allExpense.postValue(it)
            }, {
                Log.e("getByCat", "error in get By CAt")
            }).let {
            compositeDisposable.add(it)
        }
    }

    private fun filterByDateRangeAndExpDesc(start: Date, end: Date) {
        GetByDateRangeExpDescUseCase.execute(start, end).subscribeOn(ioScheduler)
            .observeOn(uiScheduler).subscribe({
            _allExpense.postValue(it)
        }, {
            Log.e("filter by date range", "error in collecting")
        }).let {
            compositeDisposable.add(it)
        }
    }

    // functions for sorting and filtering by Date
    private fun sortByDateAsc() {
        sortByDateAscUseCase.execute().subscribeOn(ioScheduler).observeOn(uiScheduler).subscribe({
            _allExpense.postValue(it)
        }, {
            Log.e("sortByDateAsc", "error in getting expenses by date ASC")
        }).let {
            compositeDisposable.add(it)
        }
    }

    private fun sortByCategoryDateRangeDateAsc(category: String, start: Date, end: Date) {
        sortByCategoryDateRangeDateAscUseCase.execute(category, start, end).subscribeOn(ioScheduler)
            .observeOn(uiScheduler).subscribe({
            _allExpense.postValue(it)
        }, {
            Log.e(
                "CatDateRangeDateAsc",
                "error in getting expenses by sortByCategoryDateRangeDateAsc"
            )
        }).let {
            compositeDisposable.add(it)
        }
    }

    private fun sortByCategoryDateAsc(category: String) {
        sortByCategoryDateAscUseCase.execute(category).subscribeOn(ioScheduler)
            .observeOn(uiScheduler).subscribe({
            _allExpense.postValue(it)
        }, {
            Log.e("sortByCategoryDateAsc", "error in getting expenses by sortByCategoryDateAsc")
        }).let {
            compositeDisposable.add(it)
        }
    }

    private fun sortByDateRangeDateAsc(start: Date, end: Date) {
        sortByDateRangeDateAscUseCase.execute(start, end).subscribeOn(ioScheduler)
            .observeOn(uiScheduler).subscribe({
            _allExpense.postValue(it)
        }, {
            Log.e("sortByDateRangeDateAsc", "error in getting expenses by sortByDateRangeDateAsc")
        }).let {
            compositeDisposable.add(it)
        }
    }

    // functions for sorting and filtering by Date Desc
    private fun sortByDateDesc() {
        sortByDateDescUseCase.execute().subscribeOn(ioScheduler).observeOn(uiScheduler).subscribe({
            _allExpense.postValue(it)
        }, {
            Log.e("sortByDateDesc", "error in getting expenses by date DeSC")
        }).let {
            compositeDisposable.add(it)
        }
    }

    private fun sortByCategoryDateRangeDateDesc(category: String, start: Date, end: Date) {
        sortByCategoryDateRangeDateDescUseCase.execute(category, start, end)
            .subscribeOn(ioScheduler).observeOn(uiScheduler).subscribe({
            _allExpense.postValue(it)
        }, {
            Log.e(
                "CatDateRangeDateDesc",
                "error in getting expenses by sortByCategoryDateRangeDateDesc"
            )
        }).let {
            compositeDisposable.add(it)
        }
    }

    private fun sortByCategoryDateDesc(category: String) {
        sortByCategoryDateDescUseCase.execute(category).subscribeOn(ioScheduler)
            .observeOn(uiScheduler).subscribe({
            _allExpense.postValue(it)
        }, {
            Log.e("sortByCategoryDateDesc", "error in getting expenses by sortByCategoryDateDesc")
        }).let {
            compositeDisposable.add(it)
        }
    }

    private fun sortByDateRangeDateDesc(start: Date, end: Date) {
        sortByDateRangeDateDescUseCase.execute(start, end).subscribeOn(ioScheduler)
            .observeOn(uiScheduler).subscribe({
            _allExpense.postValue(it)
        }, {
            Log.e("sortByDateRangeDateDesc", "error in getting expenses by sortByDateRangeDateDesc")
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

    fun filter(
        category: String? = null,
        start: Date? = null,
        end: Date? = null,
        expOrder: Int? = -1
    ) {
        if (expOrder == -1) {
            if (category != null && category != "" && (start != null && end != null)) {
                filterByDateRangeAndCategory(category, start, end)
            } else if (category != null && category != "") {
                filterByCategory(category)
            } else if (start != null && end != null) {
                filterByDateRange(start, end)
            } else {
                getAllExpenses()
            }
        } else if (expOrder == 0) {
            if (category != null && category != "" && (start != null && end != null)) {
                filterByCategoryDateExpenseAsc(category, start, end)
            } else if (category != null && category != "") {
                filterByCategoryAndExpAsc(category)
            } else if (start != null && end != null) {
                filterByDateRangeAndExpAsc(start, end)
            } else
                getByExpenseAsc()
        } else if (expOrder == 1) {
            if (category != null && category != "" && (start != null && end != null)) {
                filterByCategoryDateExpenseDesc(category, start, end)
            } else if (category != null && category != "") {
                filterByCategoryAndExpDesc(category)
            } else if (start != null && end != null) {
                filterByDateRangeAndExpDesc(start, end)
            } else
                getByExpenseDesc()
        } else if (expOrder == 2) {
            if (category != null && category != "" && (start != null && end != null)) {
                sortByCategoryDateRangeDateAsc(category, start, end)
            } else if (category != null && category != "") {
                sortByCategoryDateAsc(category)
            } else if (start != null && end != null) {
                sortByDateRangeDateAsc(start, end)
            } else
                sortByDateAsc()
        } else if (expOrder == 3) {
            if (category != null && category != "" && (start != null && end != null)) {
                sortByCategoryDateRangeDateDesc(category, start, end)
            } else if (category != null && category != "") {
                sortByCategoryDateDesc(category)
            } else if (start != null && end != null) {
                sortByDateRangeDateDesc(start, end)
            } else
                sortByDateDesc()
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
    private val getByFilterDateRangeExpAscUseCase: GetByFilterDateRangeExpAscUseCase,
    private val getByFilterExpAscUseCase: GetByFilterExpAscUseCase,
    private val getByDateRangeExpAscUseCase: GetByDateRangeExpAscUseCase,
    private val getByFilterDateRangeExpDescUseCase: GetByFilterDateRangeExpDescUseCase,
    private val getByFilterExpDescUseCase: GetByFilterExpDescUseCase,
    private val getByDateRangeExpDescUseCase: GetByDateRangeExpDescUseCase,
    private val getByExpenseAsc: GetByExpenseAsc,
    private val getByExpenseDesc: GetByExpenseDesc,
    private val sortByDateAscUseCase: SortByDateAscUseCase,
    private val sortByCategoryDateRangeDateAscUseCase: SortByCategoryDateRangDateAscUseCase,
    private val sortByCategoryDateAscUseCase: SortByCategoryDateAscUseCase,
    private val sortByDateRangeDateAscUseCase: SortByDateRangeDateAscUseCase,
    private val sortByDateDescUseCase: SortByDateDescUseCase,
    private val sortByCategoryDateRangeDateDescUseCase: SortByCategoryDateRangDateDescUseCase,
    private val sortByCategoryDateDescUseCase: SortByCategoryDateDescUseCase,
    private val sortByDateRangeDateDescUseCase: SortByDateRangeDateDescUseCase,
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
                getByDateRangeExpDescUseCase,
                getByExpenseAsc,
                getByExpenseDesc,
                sortByDateAscUseCase,
                sortByCategoryDateRangeDateAscUseCase,
                sortByCategoryDateAscUseCase,
                sortByDateRangeDateAscUseCase,
                sortByDateDescUseCase,
                sortByCategoryDateRangeDateDescUseCase,
                sortByCategoryDateDescUseCase,
                sortByDateRangeDateDescUseCase,
                converters
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}