package com.griffith.expensetracker

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ExpensesDAO {
    @Upsert
    suspend fun upsertExpense(expense: Expenses) {
    }

    @Delete
    suspend fun deleteExpense(expense: Expenses) {
    }

    @Query("SELECT * FROM expenses WHERE id=:id")
    fun getExpenseById(id: Long) {
    }

    @Query("SELECT * FROM expenses")
    fun getAllExpenses(): List<Expenses>

    @Query("SELECT * FROM expenses WHERE category=:category")
    fun getExpenseById(category: String) {
    }

    @Query("SELECT * FROM expenses WHERE date=:date")
    fun getExpenseByDate(date: Long) {
    }
}