package com.griffith.expensetracker.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val date: Long,
    val payType: String,
    val category: String,
    val description: String?,
    val latitude: Double? = null,
    val longitude: Double? = null

)

