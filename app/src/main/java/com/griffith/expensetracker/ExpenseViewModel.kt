package com.griffith.expensetracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.griffith.expensetracker.db.ExpenseEvent
import com.griffith.expensetracker.db.ExpenseState
import com.griffith.expensetracker.db.ExpenseDAO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ExpenseViewModel(
    private val dao: ExpenseDAO
) : ViewModel() {
    private val _state = MutableStateFlow(ExpenseState())
    val state: StateFlow<ExpenseState> = _state

    fun onEvent(event: ExpenseEvent) {
        when (event) {
            is ExpenseEvent.DeleteExpense -> {
                viewModelScope.launch {
                    try {
                        dao.deleteExpense(event.expense)
                        val updatedExpenses = dao.getAllExpenses().first()
                        _state.value = _state.value.copy(expenses = updatedExpenses)
                    } catch (e: Exception) {
                        _state.value = _state.value.copy(errorMessage = e.localizedMessage)
                    }
                }
            }

            ExpenseEvent.FetchAllExpenses -> {
                viewModelScope.launch {
                    dao.getAllExpenses().collect { expenses ->
                        _state.value = _state.value.copy(expenses = expenses)
                    }
                }
            }

            is ExpenseEvent.FetchExpenseById -> {
                viewModelScope.launch {
                    try {
                        val expense = dao.getExpenseById(event.id)
                        _state.value = _state.value.copy(expense = expense)
                    } catch (e: Exception) {
                        _state.value = _state.value.copy(errorMessage = e.localizedMessage)
                    }
                }
            }

            is ExpenseEvent.FetchExpensesByCategory -> {
                viewModelScope.launch {
                    dao.getExpenseByCategory(event.category).collect { expenses ->
                        _state.value = _state.value.copy(expenses = expenses)
                    }
                }
            }

            is ExpenseEvent.FetchExpensesByDate -> {
                viewModelScope.launch {
                    dao.getExpenseByDate(event.date).collect { expenses ->
                        _state.value = _state.value.copy(expenses = expenses)
                    }
                }
            }

            is ExpenseEvent.SaveExpense -> {
                viewModelScope.launch {
                    try {
                        dao.upsertExpense(event.expense)
                        val updatedExpenses = dao.getAllExpenses().first()
                        _state.value = _state.value.copy(expenses = updatedExpenses)
                    } catch (e: Exception) {
                        _state.value = _state.value.copy(errorMessage = e.localizedMessage)
                    }
                }
            }
        }
    }
}
