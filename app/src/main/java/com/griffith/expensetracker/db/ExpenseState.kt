package com.griffith.expensetracker.db

data class ExpenseState(
    val expenses: List<Expense> = emptyList(),
    val expense: Expense? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)