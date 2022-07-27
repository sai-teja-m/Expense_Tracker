package com.example.expensetracker.database.expense

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

@Dao
interface ExpenseDao {
    @Update
    fun updateExpense(exp: Expense): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExpense(exp: Expense): Completable

    @Delete
    fun deleteExpense(exp: Expense): Completable

    @Query("SELECT * FROM Expense WHERE category = :category ORDER BY date(`when`) DESC")
    fun getByCategory(category: String): Single<List<Expense>>

    @Query("SELECT DISTINCT category FROM Expense ")
    fun getCategory(): Flowable<List<String>>

    @Query("SELECT category as category,SUM(expense) as categoryAmount FROM Expense GROUP BY category")
    fun getCategoryAndAmount() : Single<List<CategoryAmount>>

    @Query("SELECT * FROM Expense ORDER BY date(`when`) DESC")
    fun getAllExpense(): Single<List<Expense>>

    @Query("SELECT SUM(expense) FROM Expense ")
    fun getTotalExpense(): Single<Int>

    @Query("SELECT SUM(expense) FROM Expense WHERE category= :category")
    fun getCategoryExpense(category: String): Single<Int>

    @Query("SELECT * FROM Expense WHERE Id = :id ")
    fun getById(id: Int): Single<Expense>

    //Date Range Filters
    @Query("SELECT * FROM Expense WHERE `when` BETWEEN :start AND :end ORDER BY date(`when`) DESC")
    fun getByDateRange(start: Date, end :Date): Single<List<Expense>>

    @Query("SELECT * FROM Expense WHERE category = :category AND (`when` BETWEEN :start AND :end) ORDER BY date(`when`) DESC")
    fun getByFilterDateRange(category: String,start: Date, end :Date): Single<List<Expense>>

    // Exp Ascending order
    @Query("SELECT * FROM Expense WHERE category= :category AND (`when` BETWEEN :start AND :end) ORDER BY expense ASC")
    fun getByFilterDateRangeExpAsc(category: String, start: Date ,end: Date) : Single<List<Expense>>

    @Query("SELECT * FROM Expense WHERE category= :category  ORDER BY expense ASC")
    fun getByFilterExpAsc(category: String) : Single<List<Expense>>

    @Query("SELECT * FROM Expense WHERE  (`when` BETWEEN :start AND :end) ORDER BY expense ASC")
    fun getByDateRangeExpAsc(start: Date ,end: Date) : Single<List<Expense>>

    //Exp Descending Order
    @Query("SELECT * FROM Expense WHERE category= :category AND (`when` BETWEEN :start AND :end) ORDER BY expense DESC")
    fun getByFilterDateRangeExpDesc(category: String, start: Date ,end: Date) : Single<List<Expense>>

    @Query("SELECT * FROM Expense WHERE category= :category  ORDER BY expense DESC")
    fun getByFilterExpDesc(category: String) : Single<List<Expense>>

    @Query("SELECT * FROM Expense WHERE  (`when` BETWEEN :start AND :end) ORDER BY expense DESC")
    fun getByDateRangeExpDesc(start: Date ,end: Date) : Single<List<Expense>>

}
