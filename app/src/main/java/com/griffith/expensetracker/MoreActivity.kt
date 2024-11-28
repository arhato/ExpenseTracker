package com.griffith.expensetracker

import BiometricAuthPrompt
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.griffith.expensetracker.ui.theme.ExpenseTrackerTheme
import xyz.teamgravity.pin_lock_compose.ChangePinLock
import xyz.teamgravity.pin_lock_compose.PinLock
import xyz.teamgravity.pin_lock_compose.PinManager

class MoreActivity : ComponentActivity() {
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
fun MoreContent(navController: NavHostController) {
    ExpenseTrackerTheme() {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            LazyGrid(navController)
        }
    }
}


@Composable
fun LazyGrid(navController: NavHostController) {
    val context = LocalContext.current

    val icons = listOf(
        MoreOptions.Export, MoreOptions.Settings, MoreOptions.SecureLockScreen, MoreOptions.Info
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
            items(icons) { item ->
                GridIconCell(item) {
                    Toast.makeText(context, item.IconResId, Toast.LENGTH_SHORT).show()
                    navController.navigate(item.route)
                }
            }
        }
    }
}

@Composable
fun GridIconCell(icon: MoreOptions, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.4f)
            .aspectRatio(1f)
            .padding(4.dp)
            .border(1.dp, MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(12.dp))
            .clickable { onClick() }, contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(icon.IconResId),
            contentDescription = icon.title,
            modifier = Modifier.fillMaxSize(0.6f),
            colorFilter = tint(MaterialTheme.colorScheme.onSurface)

        )
    }
}

@Composable
fun SettingsScreen() {
    val darkMode = isSystemInDarkTheme()
    var isDarkMode by remember { mutableStateOf(darkMode) }
    val currencyList = listOf("USD", "EUR", "GBP", "JPY", "AUD")
    var selectedCurrency by remember { mutableStateOf("EUR") }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface), contentAlignment = Alignment.TopCenter
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Settings",
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            SettingOption(label = "Dark Mode",
                description = "Toggle between light and dark mode",
                content = {
                    Switch(checked = isDarkMode, onCheckedChange = { isDarkMode = it })
                })

            SettingOption(label = "Currency",
                description = "Select your preferred currency",
                content = {
                    DropDownMenu(currencyList,
                        selectedState = selectedCurrency,
                        onSelectionChange = { selectedCurrency = it })
                })

        }
    }
}

@Composable
fun InfoScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Expense Tracker",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "A simple app to track your expenses and manage your finances. This project demonstrates the usage of Kotlin, Jetpack Compose, and Material 3 Design principles.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Key Features:",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "- Add, view, and manage expenses.\n" + "- Visualization of expense data in a pie chart.\n" + "- Export expense data.\n" + "- Currency change options.\n" + "- Theme change mode support.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Developed By:",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Arrhat Maharjan\nComputing Science\nGriffith College Cork",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ExportScreen() {
    var selectedFormat by remember { mutableStateOf("JSON") }
    var exportLocation by remember { mutableStateOf("Documents") }
    var showLocationDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Export Options",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Select Export Format:",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SettingsButton(label = "JSON",
                    isSelected = selectedFormat == "JSON",
                    onClick = { selectedFormat = "JSON" })
                SettingsButton(label = "CSV",
                    isSelected = selectedFormat == "CSV",
                    onClick = { selectedFormat = "CSV" })
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Export Location:",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { showLocationDialog = true }, colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(text = exportLocation)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    // TODO export logic
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = "Export $selectedFormat")
            }
        }
    }

    if (showLocationDialog) {
        AlertDialog(onDismissRequest = { showLocationDialog = false }, confirmButton = {
            TextButton(onClick = {
                exportLocation = "PLACEHOLDER"
                showLocationDialog = false
            }) {
                Text("Select")
            }
        }, dismissButton = {
            TextButton(onClick = { showLocationDialog = false }) {
                Text("Cancel")
            }
        }, text = {
            Text("TODO Export location selection")
        })
    }
}

@Composable
fun SecureLockScreen() {
    var isLockSetup by remember { mutableStateOf(false) }
    var isBiometricEnabled by remember { mutableStateOf(false) }
    var changePinLock by remember { mutableStateOf(false) }
    var removePIN by remember { mutableStateOf(false) }
    var setupPIN by remember { mutableStateOf(false) }
    val pinExists = PinManager.pinExists()
    val selectedLockOption by remember(isLockSetup, pinExists) {
        mutableStateOf(if (pinExists) "Change" else "Setup")
    }
    val context = LocalContext.current
    var showBiometricDialog by remember { mutableStateOf(false) }

    val biometricManager = BiometricManager.from(context)
    val isBiometricAvailable = biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS

    if (!isBiometricAvailable) {
        isBiometricEnabled = false
        // Optionally, notify the user that biometrics aren't available.
    }

    // Biometric prompt launch
    if (showBiometricDialog) {
        BiometricAuthPrompt(
            onAuthenticationSuccess = {
                showBiometricDialog = false
                // Handle success logic here
            },
            onAuthenticationFailed = {
                showBiometricDialog = false
                // Handle failure logic here
            }
        )
    }

    when {
        changePinLock -> {
            ChangePIN(onPinChanged = {
                isLockSetup = true
                changePinLock = false
            }, onCancel = { changePinLock = false }, context
            )
        }

        setupPIN -> {
            SetUpPIN(onPinCreated = {
                isLockSetup = true
                setupPIN = false
            }, onCancel = { setupPIN = false }, context
            )
        }

        removePIN -> {
            RemovePIN(onPinRemoved = {
                isLockSetup = false
                removePIN = false
            }, onCancel = { setupPIN = false }, context
            )
        }

        else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Secure Lock",
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                    if (pinExists) {
                        SettingOption(
                            label = "Change Lock PIN",
                            description = "App is secure",
                            content = {
                                Row() {
                                    Button(
                                        onClick = { changePinLock = true },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary
                                        )
                                    ) {
                                        Text(text = selectedLockOption)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))

                                    Button(
                                        onClick = { removePIN = true },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary
                                        )
                                    ) {
                                        Text(text = "Remove")
                                    }
                                }
                            })
                    } else {
                        SettingOption(label = "Setup PIN",
                            description = "Lock the app for privacy",
                            content = {
                                Button(
                                    onClick = { setupPIN = true },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text(text = selectedLockOption)
                                }
                            })
                    }
                    if (pinExists) {
                        SettingOption(
                            label = "Biometric",
                            description = "Toggle biometric lock",
                            content = {
                                Switch(checked = isBiometricEnabled,
                                    onCheckedChange = { checked ->
                                        if (checked) {
                                            if (isBiometricAvailable) {
                                                showBiometricDialog = true
                                            } else {
                                                Toast.makeText(context, "Biometrics not available", Toast.LENGTH_SHORT).show()
                                            }
                                        } else {
                                            isBiometricEnabled = false
                                        }
                                    })
                            })
                    }

                }
            }
        }
    }
}

@Composable
fun RemovePIN(
    onPinRemoved: () -> Unit, onCancel: () -> Unit, context: Context
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
            context, "Pin is removed", Toast.LENGTH_SHORT
        ).show()
        PinManager.clearPin()
        onPinRemoved()
    }, onPinIncorrect = {
        Toast.makeText(
            context, "Pin is incorrect", Toast.LENGTH_SHORT
        ).show()

    }, onPinCreated = {
        Toast.makeText(
            context, "Pin is created", Toast.LENGTH_SHORT
        ).show()

    })

}

@Composable
fun ChangePIN(
    onPinChanged: () -> Unit, onCancel: () -> Unit, context: Context
) {

    BackHandler(onBack = onCancel)
    ChangePinLock(title = { authenticated ->
        Text(
            text = if (authenticated) "Enter new pin" else "Enter your pin",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 22.sp
        )
    }, color = MaterialTheme.colorScheme.surface, onPinIncorrect = {
        Toast.makeText(
            context, "Pin is incorrect", Toast.LENGTH_SHORT
        ).show()
    }, onPinChanged = {
        Toast.makeText(
            context, "Pin is changed", Toast.LENGTH_SHORT
        ).show()
        onPinChanged()
    })
}

@Composable
fun SetUpPIN(
    onPinCreated: () -> Unit, onCancel: () -> Unit, context: Context
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
            context, "Pin is correct", Toast.LENGTH_SHORT
        ).show()
    }, onPinIncorrect = {
        Toast.makeText(
            context, "Pin is incorrect", Toast.LENGTH_SHORT
        ).show()

    }, onPinCreated = {
        Toast.makeText(
            context, "Pin is created", Toast.LENGTH_SHORT
        ).show()
        onPinCreated()
    })
}


@Composable
fun SettingOption(label: String, description: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = label, color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description, color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            content()
        }
    }
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    )
}

@Composable
fun SettingsButton(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick, colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        )
    ) {
        Text(text = label)
    }
}
