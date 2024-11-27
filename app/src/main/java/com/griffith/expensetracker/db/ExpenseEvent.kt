package com.griffith.expensetracker.db

sealed interface ExpenseEvent {
    data class SaveExpense(val expense: Expense) : ExpenseEvent
    data class DeleteExpense(val expense: Expense) : ExpenseEvent
    data class FetchExpenseById(val id: Long) : ExpenseEvent
    data class FetchExpensesByCategory(val category: String) : ExpenseEvent
    data class FetchExpensesByDate(val date: Long) : ExpenseEvent
    object FetchAllExpenses : ExpenseEvent
}
