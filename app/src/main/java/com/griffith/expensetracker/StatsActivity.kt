package com.griffith.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.griffith.expensetracker.ui.theme.ExpenseTrackerTheme
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


class StatsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseTrackerTheme {
                StatsContent()
            }
        }
    }
}

@Composable
fun StatsContent() {
    Column {
        Text("STATS")
        ExpensePieChart()
    }
}

@Composable
fun ExpensePieChart(){
    // Process sampleExpenses data to group by category and calculate totals
    val categoryTotals = sampleExpenses.groupBy { it.category }
        .map { entry -> PieEntry(entry.value.sumOf { it.amount }.toFloat(), entry.key) }

    // Set up the PieChart
    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                data = PieData(
                    PieDataSet(categoryTotals, "Expenses").apply {
                        colors = listOf(
                            0xFFE57373.toInt(), // Food
                            0xFF81C784.toInt(), // Transport
                            0xFF64B5F6.toInt(), // Entertainment
                            0xFFFFF176.toInt(), // Shopping
                            0xFF9E9E9E.toInt(), // Utilities
                            0xFFFFF59D.toInt()  // Health
                        )
                        valueTextSize = 12f
                    }
                )
                invalidate()
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Preview
@Composable
fun PreviewStats() {
    ExpenseTrackerTheme {
        StatsContent()
    }
}