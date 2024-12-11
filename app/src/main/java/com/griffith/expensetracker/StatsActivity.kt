package com.griffith.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.griffith.expensetracker.db.Expense
import com.griffith.expensetracker.db.ExpenseDAO
import com.griffith.expensetracker.ui.theme.ExpenseTrackerTheme
import com.griffith.expensetracker.ui.theme.blue
import com.griffith.expensetracker.ui.theme.brightBlue
import com.griffith.expensetracker.ui.theme.green
import com.griffith.expensetracker.ui.theme.nightDark
import com.griffith.expensetracker.ui.theme.orange
import com.griffith.expensetracker.ui.theme.purple
import com.griffith.expensetracker.ui.theme.redOrange
import com.griffith.expensetracker.ui.theme.teal
import com.griffith.expensetracker.ui.theme.yellow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class StatsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseTrackerTheme {}
        }
    }
}

@Composable
fun StatsContent(
    expenses: List<Expense>, expenseDao: ExpenseDAO, coroutineScope: CoroutineScope
) {
    val expenseByCategory =
        expenses.groupBy { it.category }.mapValues { entry -> entry.value.sumOf { it.amount } }
    val colors = listOf(
        purple, yellow, teal, redOrange, green, blue, brightBlue
    )

    // State to track the selected category
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    // If a category is selected, show its details, else show the main content
    if (selectedCategory != null) {
        CategoryDetailsScreen(selectedCategory!!, expenses, expenseDao, coroutineScope)
    } else {
        // Main content with Pie chart and category list
        Column(
            modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center
        ) {
            PieChartTwo(
                data = expenseByCategory.mapValues { it.value.toInt() }, colors = colors
            )
            CategoryExpenseList(expenseByCategory, colors, onCategoryClick = { category ->
                selectedCategory = category // Set the selected category
            })
        }
    }
}


@Composable
fun PieChartTwo(
    data: Map<String, Int>,
    radiusOuter: Dp = 110.dp,
    chartBarWidth: Dp = 40.dp,
    colors: List<Color>,
) {
    val totalSum = data.values.sum()
    val floatValue = mutableListOf<Float>()

    data.values.forEachIndexed { index, values ->
        floatValue.add(index, 360 * values.toFloat() / totalSum.toFloat())
    }

    var lastValue = 0f

    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(radiusOuter * 2.80f), contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier.size(radiusOuter * 2f)
            ) {
                floatValue.forEachIndexed { index, value ->
                    drawArc(
                        color = colors[index],
                        lastValue,
                        value,
                        useCenter = false,
                        style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Butt)
                    )
                    lastValue += value
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Total",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface
                )
                // Larger total value text
                Text(
                    text = "€${"%.2f".format(totalSum.toDouble())}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun CategoryExpenseList(
    expenseByCategory: Map<String, Double>,
    colors: List<Color>,
    onCategoryClick: (String) -> Unit // Callback for category click
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        expenseByCategory.entries.forEachIndexed { index, entry ->
            val color = colors[index % colors.size]
            CategoryExpenseItem(entry.key, entry.value, color, onCategoryClick)
            HorizontalDivider()
        }
    }
}

@Composable
fun CategoryExpenseItem(
    category: String,
    totalAmount: Double,
    color: Color,
    onCategoryClick: (String) -> Unit // Callback for category click
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .clickable { onCategoryClick(category) }, // Make the category clickable
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color = color, shape = RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = category,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "€${"%.2f".format(totalAmount)}",
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryDetailsScreen(
    category: String,
    expenses: List<Expense>,
    expenseDao: ExpenseDAO,
    coroutineScope: CoroutineScope
) {
    val categoryEntries = expenses.filter { it.category == category }
    var selectedExpense by remember { mutableStateOf<Expense?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = category,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Box(modifier = Modifier.fillMaxSize()) {
            val listState = rememberLazyListState()
            val scope = rememberCoroutineScope()

            val grouped = categoryEntries.sortedByDescending { it.date }.groupBy {
                Instant.ofEpochMilli(it.date).atZone(ZoneId.systemDefault()).toLocalDate()
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
                        ListItem(modifier = Modifier.clickable {
                            selectedExpense = expense
                            showDialog = true
                        }, headlineContent = {
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

            androidx.compose.animation.AnimatedVisibility(
                visible = showButton,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                ScrollToTopButton(onClick = {
                    scope.launch { listState.scrollToItem(0) }
                })
            }

            if (showDialog && selectedExpense != null) {
                ExpenseEditDialog(expense = selectedExpense!!,
                    onDismiss = { showDialog = false },
                    onSave = { updatedExpense ->
                        coroutineScope.launch {
                            expenseDao.upsertExpense(updatedExpense)
                            showDialog = false
                        }
                        showDialog = false
                    },
                    onDelete = { expenseToDelete ->
                        coroutineScope.launch {
                            expenseDao.deleteExpense(expenseToDelete)
                            showDialog = false
                        }
                        showDialog = false
                    })
            }
        }
    }
}
