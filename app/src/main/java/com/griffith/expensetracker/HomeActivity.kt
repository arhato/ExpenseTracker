package com.griffith.expensetracker


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.griffith.expensetracker.db.Expense
import com.griffith.expensetracker.db.ExpenseDAO
import com.griffith.expensetracker.ui.theme.ExpenseTrackerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

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
    expenses: List<Expense>, expenseDao: ExpenseDAO, coroutineScope: CoroutineScope, modifier: Modifier = Modifier
) {
    var selectedExpense by remember { mutableStateOf<Expense?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier) {
        val listState = rememberLazyListState()
        val scope = rememberCoroutineScope()

        val grouped = expenses.sortedByDescending { it.date } // Sort by date in descending order
            .groupBy {
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

        if (showDialog && selectedExpense != null) {
            ExpenseEditDialog(
                expense = selectedExpense!!,
                onDismiss = { showDialog = false },
                onSave = { updatedExpense ->
                    coroutineScope.launch {
                        expenseDao.upsertExpense(updatedExpense)
                        showDialog = false
                    }
                    showDialog = false
                },
                onDelete = {expenseToDelete ->
                    coroutineScope.launch {
                        expenseDao.deleteExpense(expenseToDelete)
                        showDialog = false
                    }
                    showDialog = false
                }
            )
        }

    }
}

@Composable
fun ExpenseEditDialog(
    expense: Expense, onDismiss: () -> Unit, onSave: (Expense) -> Unit,
    onDelete: (Expense) -> Unit
) {
    val amountState = remember { mutableStateOf(TextFieldValue(expense.amount.toString())) }
    val descriptionState = remember { mutableStateOf(TextFieldValue(expense.description ?: "")) }
    val selectedDateState = remember { mutableLongStateOf(expense.date) }
    val isDatePickerVisible = remember { mutableStateOf(false) }

    val categoryList = listOf("Food", "Utilities", "Transport", "Shopping", "Entertainment", "Health", "Other")
    val selectedCategoryState = remember { mutableStateOf(expense.category) }

    val payTypeList = listOf("Card", "Cash")
    val selectedPayTypeState = remember { mutableStateOf(expense.payType) }

    val isAmountValid = remember { mutableStateOf(true) }
    val isFormValid = remember { mutableStateOf(false) }

    val hasChanges = remember { mutableStateOf(false) }

    val context = LocalContext.current

    if (isDatePickerVisible.value) {
        DatePickerModal(initialDate = selectedDateState.longValue,
            onDateSelected = { selectedDate ->
                selectedDateState.longValue = selectedDate ?: System.currentTimeMillis()
                isDatePickerVisible.value = false
            },
            onDismiss = { isDatePickerVisible.value = false })
    }

    AlertDialog(onDismissRequest = onDismiss, title = { Text("Expense") }, text = {
        val focusManager = LocalFocusManager.current

        Column {
            OutlinedTextField(
                value = amountState.value,
                onValueChange = {
                    amountState.value = it
                    isAmountValid.value =
                        it.text.isNotBlank() && it.text.isNotEmpty() && it.text.toDoubleOrNull()
                            ?.let { amount -> amount > 0 } ?: false
                    isFormValid.value = isAmountValid.value
                    hasChanges.value = true
                },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()

                    if (isAmountValid.value) {
                        Toast.makeText(
                            context,
                            "Amount confirmed: ${amountState.value.text}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(context, "Invalid entry!", Toast.LENGTH_SHORT).show()
                    }
                }),
                isError = !isAmountValid.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                OutlinedTextField(value = TextFieldValue(
                    text = SimpleDateFormat("dd/MM/yyyy").format(Date(selectedDateState.longValue))
                ),
                    enabled = false,
                    onValueChange = {},
                    label = { Text("Date") },
                    readOnly = true,
                    modifier = Modifier
                        .clickable { isDatePickerVisible.value = true }
                        .fillMaxWidth()
                        .padding(bottom = 10.dp))
            }

            DropDownMenu(menuList = payTypeList,
                selectedState = selectedPayTypeState.value,
                onSelectionChange = {
                    selectedPayTypeState.value = it
                    hasChanges.value = true
                })

            DropDownMenu(menuList = categoryList,
                selectedState = selectedCategoryState.value,
                onSelectionChange = {
                    selectedCategoryState.value = it
                    hasChanges.value = true
                })

            OutlinedTextField(value = descriptionState.value, onValueChange = {
                descriptionState.value = it
                hasChanges.value = true
            }, label = { Text("Description") }, keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ), keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }), modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
            )

            Box(modifier = Modifier
                .fillMaxWidth()
                .padding()) {
                if (expense.latitude != null && expense.longitude != null) {
                    MapWindow(expense.latitude,expense.longitude)
                } else {
                    Text(
                        text = "Location not available",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(10.dp),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }, confirmButton = {
        Button(
            onClick = {
                if (hasChanges.value) {
                    onSave(expense.copy(
                        amount = amountState.value.text.toDoubleOrNull() ?: expense.amount,
                        date = selectedDateState.longValue,
                        category = selectedCategoryState.value,
                        payType = selectedPayTypeState.value,
                        description = descriptionState.value.text
                    ))
                } else {
                    onDelete(expense)
                }
                onDismiss()
            },
        ) {
            Text(if (hasChanges.value) "Save" else "Delete")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    })
}

@Composable
fun MapWindow(latitude:Double,longitude:Double){
    val mapLocation = LatLng(latitude, longitude)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(mapLocation, 17f)
    }
    var uiSettings by remember {
        mutableStateOf(MapUiSettings(zoomControlsEnabled = false))
    }
    var properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }
    val markerState = remember { MarkerState(position = mapLocation) }

    GoogleMap(
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings,
        modifier = Modifier.height(200.dp)
    ) {
        Marker(
            state = markerState ,
            title = "Location"
        )
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
