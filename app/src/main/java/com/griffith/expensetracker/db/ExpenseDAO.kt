package com.griffith.expensetracker.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDAO {
    @Upsert
    suspend fun upsertExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expenses WHERE id=:id")
    fun getExpenseById(id: Long): Expense?

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE category=:category")
    fun getExpenseByCategory(category: String): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE date=:date")
    fun getExpenseByDate(date: Long): Flow<List<Expense>>

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpensesLive(): LiveData<List<Expense>>

    @Query("SELECT * FROM expenses WHERE strftime('%Y-%m', date / 1000, 'unixepoch') = :yearMonth")
    fun getExpensesByMonth(yearMonth: String): Flow<List<Expense>>

    @Query("DELETE FROM expenses")
    suspend fun deleteAllExpenses()

}