package com.griffith.expensetracker

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.griffith.expensetracker.ui.theme.ExpenseTrackerTheme

class MoreActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseTrackerTheme {
                MoreContent()
            }
        }
    }
}


@Composable
fun MoreContent() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        LazyGrid()
    }
}


@Composable
fun LazyGrid() {
    val context = LocalContext.current

    val icons = listOf(
        R.drawable.file_export, R.drawable.settings, R.drawable.currency_exchange, R.drawable.info
    )
    Box(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .aspectRatio(1f)
            .background(
                MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(20.dp)
            )
            .padding(8.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items(icons) { iconRes ->
                GridIconCell(iconRes = iconRes) {
                    Toast.makeText(context, iconRes, Toast.LENGTH_SHORT).show()/*TODO*/
                }
            }
        }
    }
}

@Composable
fun GridIconCell(iconRes: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.4f)
            .aspectRatio(1f)
            .padding(4.dp)
            .border(1.dp, MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(12.dp))
            .clickable { onClick() }, contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(0.6f)
        )
    }
}