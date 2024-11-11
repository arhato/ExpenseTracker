package com.griffith.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.griffith.expensetracker.ui.theme.ExpenseTrackerTheme
import kotlinx.coroutines.launch


class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseTrackerTheme {
                HomeContent()
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(modifier: Modifier = Modifier) {
    val expenses = listOf(
        Expense("Monday", "12/11/2024", "$50", "Food", "Card"),
        Expense("Tuesday", "13/11/2024", "$30", "Transport", "Cash"),
        Expense("Wednesday", "14/11/2024", "$20", "Bill", "Card"),
        Expense("Monday", "12/11/2024", "$50", "Food", "Card"),
        Expense("Tuesday", "13/11/2024", "$30", "Transport", "Cash"),
        Expense("Wednesday", "14/11/2024", "$20", "Bill", "Card"),
        Expense("Monday", "12/11/2024", "$50", "Food", "Card"),
        Expense("Tuesday", "13/11/2024", "$30", "Transport", "Cash"),
        Expense("Wednesday", "14/11/2024", "$20", "Bill", "Card"),
        Expense("Monday", "12/11/2024", "$50", "Food", "Card"),
        Expense("Tuesday", "13/11/2024", "$30", "Transport", "Cash"),
        Expense("Wednesday", "14/11/2024", "$20", "Bill", "Card")
    )

    Box(modifier) {
        val listState = rememberLazyListState()
        val scope = rememberCoroutineScope()

        // Group expenses by date
        val grouped = expenses.groupBy { it.date }

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(bottom = 80.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            grouped.forEach { (date, expenseList) ->
                stickyHeader {
                    CharacterHeader(date)
                }
                items(expenseList) { expense ->
                    ListItem(
                        headlineContent = {
                            Row(
                                Modifier
                                    .height(IntrinsicSize.Min)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(expense.day)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        VerticalDivider(
                                            modifier = Modifier.fillMaxHeight(),
                                            thickness = 1.dp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(expense.date)
                                    }
                                }
                                Column {
                                    Text(expense.amount)
                                }
                            }
                        },
                        supportingContent = {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(expense.category)
                                }
                                Column {
                                    Text(expense.payType)
                                }
                            }
                        },
                        leadingContent = {
                            val icon = when (expense.category) {
                                "Food" -> painterResource(R.drawable.localdining) // Replace with actual food icon
                                "Transport" -> painterResource(R.drawable.bus) // Replace with actual transport icon
                                "Groceries" -> painterResource(R.drawable.bill) // Replace with actual groceries icon
                                else -> painterResource(R.drawable.money) // Default icon if no match
                            }
                            Icon(
                                painter = icon,
                                contentDescription = expense.category,
                            )
                        }
                    )
                    HorizontalDivider()
                }
            }
        }

        val showButton = listState.firstVisibleItemIndex > 0

        AnimatedVisibility(
            visible = showButton,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            ScrollToTopButton(onClick = {
                scope.launch { listState.scrollToItem(0) }
            })
        }
    }
}

@Composable
fun CharacterHeader(date: String) {
    Text(
        text = date,
        fontSize = 15.sp,
        color = MaterialTheme.colorScheme.primary,

        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer) // Add background color
            .padding(top = 5.dp, start = 16.dp, end = 16.dp, bottom = 5.dp) // Padding for spacing
            .fillMaxWidth() // Ensure the background spans the entire width
    )
}

@Composable
fun ScrollToTopButton(onClick: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(bottom = 10.dp),
        Alignment.BottomCenter
    ) {
        Button(
            onClick = { onClick() }, modifier = Modifier
                .shadow(10.dp, shape = CircleShape)
                .clip(shape = CircleShape)
                .size(65.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Icon(Icons.Filled.KeyboardArrowUp, "arrow up")
        }
    }
}

data class Expense(
    val day: String, val date: String, val amount: String, val category: String, val payType: String
)
