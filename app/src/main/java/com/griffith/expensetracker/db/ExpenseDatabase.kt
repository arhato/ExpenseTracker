package com.griffith.expensetracker.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Expense::class],
    version=1
)
abstract class ExpenseDatabase: RoomDatabase() {
    abstract fun expenseDAO(): ExpenseDAO
}