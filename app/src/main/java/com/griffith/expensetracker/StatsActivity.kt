package com.griffith.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.griffith.expensetracker.db.Expense
import com.griffith.expensetracker.ui.theme.ExpenseTrackerTheme
import com.griffith.expensetracker.ui.theme.blue
import com.griffith.expensetracker.ui.theme.brightBlue
import com.griffith.expensetracker.ui.theme.green
import com.griffith.expensetracker.ui.theme.nightDark
import com.griffith.expensetracker.ui.theme.purple
import com.griffith.expensetracker.ui.theme.redOrange


class StatsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseTrackerTheme {
            }
        }
    }
}

@Composable
fun StatsContent(expenses: List<Expense>,
) {
    val expenseByCategory = expenses
        .groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount } }
    val colors = listOf(
        redOrange,
        green,
        blue,
        brightBlue,
        purple,
        nightDark
    )
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        PieChartTwo(
            data = expenseByCategory.mapValues { it.value.toInt() },
            colors=colors
        )
        CategoryExpenseList(expenseByCategory)
    }
}

@Composable
fun PieChartTwo(
    data: Map<String, Int>,
    radiusOuter: Dp = 120.dp,
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
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(radiusOuter * 3f),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .size(radiusOuter * 2f)
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
        }
    }
}

@Composable
fun CategoryExpenseList(expenseByCategory: Map<String, Double>) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        expenseByCategory.forEach { (category, totalAmount) ->
            CategoryExpenseItem(category, totalAmount)
            HorizontalDivider()
        }
    }
}

@Composable
fun CategoryExpenseItem(category: String, totalAmount: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

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
