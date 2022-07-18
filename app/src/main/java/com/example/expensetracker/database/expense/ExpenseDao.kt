package com.example.expensetracker.database.expense

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface ExpenseDao {
    @Update
    fun updateExpense(exp: Expense): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExpense(exp: Expense): Completable

    @Delete
    fun deleteExpense(exp: Expense): Completable

    @Query("SELECT * FROM Expense WHERE category = :category")
    fun getByCategory(category: String): Single<List<Expense>>

    @Query("SELECT DISTINCT category FROM Expense ")
    fun getCategory(): Flowable<List<String>>

    @Query("SELECT category as category,SUM(expense) as categoryAmount FROM Expense GROUP BY category")
    fun getCategoryAndAmount() : Single<List<CategoryAmount>>

    @Query("SELECT * FROM Expense ORDER BY `when` ASC")
    fun getAllExpense(): Single<List<Expense>>

    @Query("SELECT SUM(expense) FROM Expense ")
    fun getTotalExpense(): Single<Int>

    @Query("SELECT SUM(expense) FROM Expense WHERE category= :category")
    fun getCategoryExpense(category: String): Single<Int>

    @Query("SELECT * FROM Expense WHERE Id = :id ")
    fun getById(id: Int): Single<Expense>
}
