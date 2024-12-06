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
import com.griffith.expensetracker.db.DatabaseInstance
import com.griffith.expensetracker.db.Expense
import com.griffith.expensetracker.db.ExpenseEvent
import com.griffith.expensetracker.ui.theme.ExpenseTrackerTheme
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ExpenseTrackerTheme {

            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    expenses: List<Expense>,
    modifier: Modifier = Modifier) {
    Box(modifier) {
        val listState = rememberLazyListState()
        val scope = rememberCoroutineScope()

        val grouped = expenses.groupBy {
            Instant.ofEpochMilli(it.date)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(bottom = 80.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            grouped.forEach { (date, expenseList) ->
                stickyHeader {
                    CharacterHeader(date.format(DateTimeFormatter.ofPattern("dd MMM yyyy")))
                }
                items(expenseList) { expense ->
                    ListItem(headlineContent = {
                        Row(
                            Modifier
                                .height(IntrinsicSize.Min)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(getDayFromDate(expense.date))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    VerticalDivider(
                                        modifier = Modifier.fillMaxHeight(),
                                        thickness = 1.dp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(formatDate(expense.date))
                                }
                            }
                            Column {
                                Text("€" + expense.amount.toString())
                            }
                        }
                    }, supportingContent = {
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
                    }, leadingContent = {
                        val icon = when (expense.category) {
                            "Food" -> painterResource(R.drawable.localdining)
                            "Utilities" -> painterResource(R.drawable.utilities)
                            "Transport" -> painterResource(R.drawable.bus)
                            "Shopping" -> painterResource(R.drawable.shopping_basket)
                            "Entertainment" -> painterResource(R.drawable.movies)
                            "Health" -> painterResource(R.drawable.health)
                            else -> painterResource(R.drawable.money)
                        }
                        Icon(
                            painter = icon,
                            contentDescription = expense.category,
                        )
                    })
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
        text = date, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSecondaryContainer,

        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(top = 2.dp, start = 15.dp, end = 15.dp, bottom = 2.dp)
            .fillMaxWidth()
    )
}

@Composable
fun ScrollToTopButton(onClick: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(bottom = 10.dp), Alignment.BottomCenter
    ) {
        Button(
            onClick = { onClick() },
            modifier = Modifier
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

fun getDayFromDate(timestamp: Long): String {
    val date = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate()
    return date.dayOfWeek.toString().lowercase().replaceFirstChar { it.uppercase() }
}

fun formatDate(timestamp: Long): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    val date = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate()
    return date.format(formatter)
}

val sampleExpenses = listOf(
    Expense(
        amount = 20.5,
        date = 1695465600000,
        payType = "Cash",
        category = "Food",
        description = "Lunch at a cafe"
    ), Expense(
        amount = 20.5,
        date = 1695465600000,
        payType = "Cash",
        category = "Food",
        description = "Lunch at a cafe"
    ), Expense(
        amount = 20.5,
        date = 1695465600000,
        payType = "Cash",
        category = "Food",
        description = "Lunch at a cafe"
    ), Expense(
        amount = 50.0,
        date = 1695552000000,
        payType = "Card",
        category = "Transport",
        description = "Gas refill"
    ), Expense(
        amount = 15.75,
        date = 1695638400000,
        payType = "Cash",
        category = "Entertainment",
        description = "Movie ticket"
    ), Expense(
        amount = 200.0,
        date = 1695724800000,
        payType = "Card",
        category = "Shopping",
        description = "New shoes"
    ), Expense(
        amount = 120.0,
        date = 1695811200000,
        payType = "Card",
        category = "Utilities",
        description = "Electricity bill"
    ), Expense(
        amount = 30.0,
        date = 1695897600000,
        payType = "Card",
        category = "Food",
        description = "Weekly groceries"
    ), Expense(
        amount = 60.0,
        date = 1695984000000,
        payType = "Cash",
        category = "Health",
        description = "Pharmacy purchase"
    ), Expense(
        amount = 10.0,
        date = 1696070400000,
        payType = "Cash",
        category = "Food",
        description = "Snacks"
    ), Expense(
        amount = 100.0,
        date = 1696156800000,
        payType = "Card",
        category = "Transport",
        description = "Monthly metro pass"
    ), Expense(
        amount = 25.0,
        date = 1696243200000,
        payType = "Card",
        category = "Entertainment",
        description = "Concert ticket"
    )
)
