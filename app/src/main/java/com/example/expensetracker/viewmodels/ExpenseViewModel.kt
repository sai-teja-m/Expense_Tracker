package com.example.expensetracker.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.expensetracker.database.expense.Expense
import com.example.expensetracker.domain.usecase.DeleteExpenseUseCase
import com.example.expensetracker.domain.usecase.GetAllUseCase
import com.example.expensetracker.domain.usecase.InsertExpenseUseCase
import com.example.expensetracker.domain.usecase.UpdateExpenseUseCase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ExpenseViewModel(private val insertUse: InsertExpenseUseCase,private val updateUse:UpdateExpenseUseCase , private  val deleteCase:DeleteExpenseUseCase,private val getAllUse:GetAllUseCase):ViewModel(){

    private val uiScheduler by lazy { AndroidSchedulers.mainThread() }
    private val ioScheduler by lazy { Schedulers.io() }
    private val disposableDelegate = lazy { CompositeDisposable() }
    private val compositeDisposable by disposableDelegate

    fun isEntryValid(expenseTitle: String, expense:String, `when`:String, category:String): Boolean {
        if (expenseTitle.isBlank() ||expense.isBlank() || `when`.isBlank() || category.isBlank()) {
            return false
        }
        return true
    }

    private val _allExpense : MutableLiveData<List<Expense>> = MutableLiveData()
    val allExpense: LiveData<List<Expense>> = _allExpense

    fun getAll(){
        getAllUse.execute().subscribeOn(ioScheduler).observeOn(uiScheduler).subscribe({list->
            _allExpense.postValue(list)
        },{
            Log.e("getAllError","error in loading DB in getALL")
        }
        ).let {
            compositeDisposable.add(it)
        }
    }
    fun addNewExpense(expenseTitle:String , expense:String , `when` :String,category:String){
        Log.d("addExp","$expenseTitle")
        val newExpense = getNewExpenseEntry(expenseTitle,expense,`when`,category)
        insertUse.execute(newExpense).subscribeOn(ioScheduler).observeOn(uiScheduler).subscribe ({
            getAll()
        },{
            Log.e("addNewExpense","error in loading new list ")
        }).let {
            compositeDisposable.add(it)
        }
    }

    fun updateExpense(expenseTitle:String , expense:String , `when` :String,category:String){
        val exp = getNewExpenseEntry(expenseTitle,expense,`when`,category)
        updateUse.execute(exp).subscribeOn(ioScheduler).observeOn(uiScheduler).subscribe({
            getAll()
        },{
            Log.e("updateUse","error in Loading new list from updateUse")
        }).let {
            compositeDisposable.add(it)
        }
    }

    fun deleteItem(expense: Expense) {

        deleteCase.execute(expense).subscribeOn(ioScheduler).observeOn(uiScheduler).subscribe({
            getAll()
        },{
            Log.e("deleteUse","error in delete Use")
        }).let {
            compositeDisposable.add(it)
        }
    }



    private fun getNewExpenseEntry(expenseTitle:String , expense:String , `when` :String,category:String): Expense{
        return Expense(
            expenseTitle = expenseTitle,
            expense = expense.toInt(),
            `when` = `when`,
            category = category
        )
    }

}


class ExpenseViewModelFactory(private val insertUse: InsertExpenseUseCase,private val updateUse:UpdateExpenseUseCase , private  val deleteCase:DeleteExpenseUseCase,private val getAllUse:GetAllUseCase): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(insertUse, updateUse , deleteCase , getAllUse ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}