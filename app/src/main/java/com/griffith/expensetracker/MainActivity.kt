package com.griffith.expensetracker

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.griffith.expensetracker.ui.theme.ExpenseTrackerTheme
import xyz.teamgravity.pin_lock_compose.PinLock
import xyz.teamgravity.pin_lock_compose.PinManager
import java.text.SimpleDateFormat
import java.util.Date


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        PinManager.initialize(this)

        setContent {
            var pinEntered by remember { mutableStateOf(false) }
            val pinExists = PinManager.pinExists()

            val context = LocalContext.current
            val navController = rememberNavController()
            ExpenseTrackerTheme {
                if (PinManager.pinExists()) {
                    if (!pinEntered) {
                        CheckPIN(
                            onCorrect = {
                                pinEntered = true
                            },
                            onIncorrect = { },
                            onCancel = { },
                            context
                        )
                    }
                }
                if (pinEntered || !pinExists ) MainContent(navController)
            }
        }
    }
}

@Composable
fun CheckPIN(
    onCorrect: () -> Unit, onIncorrect: () -> Unit, onCancel: () -> Unit, context: Context
) {
    BackHandler(onBack = onCancel)
    PinLock(title = { pinExists ->
        Text(
            text = if (pinExists) "Enter your pin" else "Create pin",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 22.sp
        )
    }, color = MaterialTheme.colorScheme.surface, onPinCorrect = {
        Toast.makeText(
            context, "App unlocked", Toast.LENGTH_SHORT
        ).show()
        onCorrect()
    }, onPinIncorrect = {
        Toast.makeText(
            context, "Pin is incorrect", Toast.LENGTH_SHORT
        ).show()
        onIncorrect()
    }, onPinCreated = {
        Toast.makeText(
            context, "Pin is created", Toast.LENGTH_SHORT
        ).show()
    })

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(navController: NavHostController) {

    var showExpenseDialog by remember { mutableStateOf(false) }
    var topMenuExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(topBar = {
            TopAppBar(colors = topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ), title = {
                Text("Expense Tracker")
            }, actions = {
                IconButton(onClick = { topMenuExpanded = !topMenuExpanded }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert, contentDescription = "More"
                    )
                }

                DropdownMenu(expanded = topMenuExpanded,
                    onDismissRequest = { topMenuExpanded = false }) {
                    DropdownMenuItem(text = { Text("Settings") }, onClick = {
                        Toast.makeText(
                            context, "Settings", Toast.LENGTH_SHORT
                        ).show()
                        navController.navigate(MoreOptions.Settings.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                        topMenuExpanded = false
                    })
                    DropdownMenuItem(text = { Text("About") }, onClick = {
                        Toast.makeText(
                            context, "About", Toast.LENGTH_SHORT
                        ).show()
                        navController.navigate(MoreOptions.Info.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                        topMenuExpanded = false
                    })

                }

            })

        }, bottomBar = {
            BottomAppBar(containerColor = MaterialTheme.colorScheme.primaryContainer,
                actions = { BottomNavigationBar(navController) })
        }, floatingActionButton = {
            FloatingActionButton(onClick = { showExpenseDialog = true }) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface
                )

            }
        }) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = BottomNavigationItem.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = BottomNavigationItem.Home.route) {
                    HomeContent()
                }
                composable(route = BottomNavigationItem.More.route) {
                    MoreContent(navController)
                }
                composable(route = BottomNavigationItem.Stats.route) {
                    StatsContent()
                }
                composable(route = MoreOptions.Settings.route) {
                    SettingsScreen()
                }
                composable(route = MoreOptions.Info.route) {
                    InfoScreen()
                }
                composable(route = MoreOptions.SecureLockScreen.route) {
                    SecureLockScreen()
                }
                composable(route = MoreOptions.Export.route) {
                    ExportScreen()
                }
            }
        }
        if (showExpenseDialog) {
            ExpenseFormDialog(onDismiss = { showExpenseDialog = false },
                onAddExpense = { amount, date, payType, category, description ->
                    // Handle saving the expense here
                    showExpenseDialog = false
                })
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavigationItem.Home, BottomNavigationItem.Stats, BottomNavigationItem.More
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEachIndexed { index, item ->
            val selected = currentDestination?.route?.startsWith(item.route) == true

            val selectedIcon = painterResource(id = item.selectedIconResId)
            val unselectedIcon = painterResource(id = item.unselectedIconResId)

            NavigationBarItem(selected = selected, onClick = {
                if (currentDestination?.route != item.route) {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }, label = { Text(text = item.title) }, icon = {
                Icon(
                    painter = if (selected) selectedIcon else unselectedIcon,
                    contentDescription = item.title
                )
            })
        }
    }
}

@Composable
fun ExpenseFormDialog(
    onDismiss: () -> Unit, onAddExpense: (String, String, String, String, String) -> Unit
) {
    val amountState = remember { mutableStateOf(TextFieldValue()) }
    val descriptionState = remember { mutableStateOf(TextFieldValue()) }

    val selectedDateState = remember { mutableLongStateOf(System.currentTimeMillis()) }
    val isDatePickerVisible = remember { mutableStateOf(false) }

    val categoryList =
        listOf("Food", "Utilities", "Transport", "Shopping", "Entertainment", "Health", "Other")
    val selectedCategoryState = remember { mutableStateOf(categoryList[0]) }

    val payTypeList = listOf("Card", "Cash")
    val selectedPayTypeState = remember { mutableStateOf(payTypeList[0]) }

    val locationSwitchState = remember { mutableStateOf(false) }

    val isAmountValid = remember { mutableStateOf(true) }
    val isFormValid = remember { mutableStateOf(false) }

    val context = LocalContext.current


    if (isDatePickerVisible.value) {
        DatePickerModal(initialDate = selectedDateState.longValue,
            onDateSelected = { selectedDate ->
                selectedDateState.longValue = selectedDate ?: System.currentTimeMillis()
                isDatePickerVisible.value = false
            },
            onDismiss = { isDatePickerVisible.value = false })
    }

    AlertDialog(onDismissRequest = onDismiss, title = { Text("Add Expense") }, text = {
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
                onSelectionChange = { selectedPayTypeState.value = it })


            DropDownMenu(menuList = categoryList,
                selectedState = selectedCategoryState.value,
                onSelectionChange = { selectedCategoryState.value = it })

            OutlinedTextField(value = descriptionState.value, onValueChange = {
                descriptionState.value = it
            }, label = { Text("Description") }, keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ), keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }), modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
            )


            Row(
                Modifier
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Include location", textAlign = TextAlign.Center)
                Switch(checked = locationSwitchState.value,
                    onCheckedChange = { locationSwitchState.value = it })
            }
        }

    }, confirmButton = {
        Button(
            onClick = {
                onAddExpense(
                    amountState.value.text,
                    selectedDateState.longValue.toString(),
                    selectedPayTypeState.value,
                    selectedCategoryState.value,
                    descriptionState.value.text
                )
                onDismiss()
            }, enabled = isFormValid.value
        ) {
            Text("Save")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    initialDate: Long, onDateSelected: (Long?) -> Unit, onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate
    )
    DatePickerDialog(onDismissRequest = onDismiss, confirmButton = {
        TextButton(onClick = {
            onDateSelected(datePickerState.selectedDateMillis)
            onDismiss()
        }) {
            Text("OK")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    }) {
        DatePicker(state = datePickerState)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(
    menuList: List<String>, selectedState: String, onSelectionChange: (String) -> Unit
) {
    val context = LocalContext.current

    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
    ) {
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded }) {
            OutlinedTextField(
                value = selectedState,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
            )
            ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                menuList.forEachIndexed { index, item ->
                    DropdownMenuItem(text = { Text(text = item) }, onClick = {
                        onSelectionChange(item)
                        isExpanded = false
                        Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                    })
                }
            }
        }
    }
}

sealed class BottomNavigationItem(
    val title: String, val selectedIconResId: Int, val unselectedIconResId: Int, val route: String
) {
    data object Home : BottomNavigationItem(
        title = "Home",
        selectedIconResId = R.drawable.filled_home,
        unselectedIconResId = R.drawable.outline_home,
        route = "home_screen"
    )

    data object Stats : BottomNavigationItem(
        title = "Stats",
        selectedIconResId = R.drawable.barchart,
        unselectedIconResId = R.drawable.barchart,
        route = "stats_screen"
    )

    data object More : BottomNavigationItem(
        title = "More",
        selectedIconResId = R.drawable.menu,
        unselectedIconResId = R.drawable.menu,
        route = "more_screen"
    )
}

sealed class MoreOptions(
    val title: String, val IconResId: Int, val route: String
) {
    data object Export : MoreOptions(
        title = "Export", IconResId = R.drawable.file_export, route = "more_screen/file_export"
    )

    data object Settings : MoreOptions(
        title = "Settings", IconResId = R.drawable.settings, route = "more_screen/settings"
    )

    data object SecureLockScreen : MoreOptions(
        title = "SecureLockScreen",
        IconResId = R.drawable.secure_lock,
        route = "more_screen/secure_lock"
    )

    data object Info : MoreOptions(
        title = "Info", IconResId = R.drawable.info, route = "more_screen/info"
    )
}

const val TAG = "PinLock"
