import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import xyz.teamgravity.pin_lock_compose.PinLock

//package com.griffith.expensetracker
//
//import android.os.Build
//import android.widget.Toast
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.Modifier
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.RectangleShape
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.sp
//import androidx.fragment.app.FragmentActivity
//import kotlinx.coroutines.runBlocking
//
//@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
//@Composable
//fun LockScreen(
//    tonavigate: () -> Unit
//) {
//    val pin = remember {
//        mutableStateListOf<Int>(
//        )
//    }
//    val useBio = remember {
//        mutableStateOf(false)
//    }
//
//    Surface {
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//
//            Spacer(modifier = Modifier.height(10.dp))
//            Image(
//                painter = painterResource(id = R.drawable.utilities),
//                contentDescription = "User",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(15.dp)
//                    .clip(CircleShape)
//            )
//
//            Spacer(modifier = Modifier.height(10.dp))
//            Text(text = "Hii, Ravi")
//            Text(text = "ravisahuXXX@gmail.com")
//
//            Spacer(modifier = Modifier.weight(0.1f))
//            Text(text = "Verify 4-digit security PIN")
//
//            Spacer(modifier = Modifier.height(10.dp))
//
//            InputDots(pin)
//
//            Text(text = "Use Touch ID", color = Color(0xFF00695C), modifier = Modifier.clickable {
//                useBio.value = true
//            })
//            Spacer(modifier = Modifier.weight(0.1f))
//
//            NumberBoard( onNumberClick = { mynumber ->
//                when (mynumber) {
//                    "." -> {}
//                    "X" -> {
//                        if (pin.isNotEmpty()) pin.removeLast()
//                    }
//
//                    else -> {
//                        if (pin.size < 4)
//                            pin.add(mynumber.toInt())
//                    }
//                }
//            })
//
//            Spacer(modifier = Modifier.height(10.dp))
//
//            if (pin.size == 4) {
//                Spacer(modifier = Modifier.height(10.dp))
//                if (pin.size == 4) {
//                    CheckUserAuth (pin.toList(), tonavigate)
//                }
//                if (useBio.value) {
//                    UseBioMetric()
//                    useBio.value = false
//                }            }
//
//            if (useBio.value) {
//                //use bio auth here
//                useBio.value = false
//            }
//
//        }
//    }
//}
//
//@Preview
//@Composable
//fun InputDots(
//    numbers: List<Int> = listOf(1, 2),
//) {
//    //1st way
//    Row(
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        for (i in 0..3) {
//            PinIndicator(
//                filled = when (i) {
//                    0 -> numbers.isNotEmpty()
//                    else -> numbers.size > i
//                }
//            )
//        }
//
//    }
//    //2nd way
//    Row(
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        PinIndicator(filled = numbers.isNotEmpty())
//        PinIndicator(filled = numbers.size > 1)
//        PinIndicator(filled = numbers.size > 2)
//        PinIndicator(filled = numbers.size > 3)
//    }
//}
//
//@Composable
//private fun PinIndicator(
//    filled: Boolean,
//) {
//    Box(
//        modifier = Modifier
//            .size(15.dp)
//            .padding(15.dp)
//            .clip(CircleShape)
//            .background(if (filled) Color.Black else Color.Transparent)
//            .border(2.dp, Color.Black, CircleShape)
//
//    )
//}
//
//
//@Preview(showBackground = true)
//@Composable
//private fun NumberButton(
//    modifier: Modifier = Modifier,
//    number: String = "1",
//    onClick: (number: String) -> Unit = {},
//) {
//    Button(
//        onClick = {
//            onClick(number)
//        },
//        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
//        modifier = modifier
//            .size(90.dp)
//            .padding(0.dp),
//        shape = RectangleShape,
//    ) {
//        Text(
//            text = number, color = Color.Black, fontSize = 22.sp
//        )
//    }
//}
//
//@Composable
//fun NumberBoard(
//    onNumberClick: (num: String) -> Unit,
//) {
//    val buttons = (1..9).toList()
//
//    Column(
//        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.SpaceEvenly
//    ) {
//        buttons.chunked(3).forEach { buttonRow ->
//            Row(
//            ) {
//                buttonRow.forEach { buttonNumber ->
//
//                    NumberButton(
//                        number = buttonNumber.toString(),
//                        onClick = onNumberClick,
//                        modifier = Modifier
//                            .weight(1f)
//                    )
//
//                }
//            }
//        }
//        NumberBoardRow(listOf(".", "0", "X"), onNumberClick = onNumberClick)
//    }
//}
//
//@Composable
//fun NumberBoardRow(
//    num: List<String>,
//    onNumberClick: (num: String) -> Unit
//) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.Center
//    ) {
//        for (i in num) {
//            NumberButton(
//                modifier = Modifier.weight(1f),
//                number = i,
//                onClick = { onNumberClick(it) })
//        }
//    }
//}
//
//@Composable
//fun CheckUserAuth(pin: List<Int>, onLoginSuccess: () -> Unit) {
//    val isPinCorrect = pin == listOf(1, 2, 3, 4)
//    if (isPinCorrect) {
//        LaunchedEffect(Unit) {
//            onLoginSuccess()
//        }
//    } else {
//        Toast.makeText(LocalContext.current, "Login Failed", Toast.LENGTH_SHORT).show()
//    }
//}
//@Composable
//fun UseBioMetric() {
//
//    val activity = LocalContext.current
//    LaunchedEffect(key1 = true) {
//        Biometric.authenticate(
//            activity as FragmentActivity,
//            title = "Sharp Wallet",
//            subtitle = "Please Authenticate in order to use Sharp Wallet",
//            description = "Authentication is must",
//            negativeText = "Cancel",
//            onSuccess = {
//                runBlocking {
//                    Toast.makeText(
//                        activity,
//                        "Authenticated successfully",
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()
//
//                }
//            },
//            onError = { errorCode, errorString ->
//                runBlocking {
//                    Toast.makeText(
//                        activity,
//                        "Authentication error: $errorCode, $errorString",
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()
//                }
//            },
//            onFailed = {
//                runBlocking {
//                    Toast.makeText(
//                        activity,
//                        "Authentication failed",
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()
//                }
//            }
//        )
//
//    }
//}
@Composable
fun LockScreen(){
    PinLock(
        title = { pinExists ->
            Text(text = if (pinExists) "Enter your pin" else "Create pin")
        },
        color = MaterialTheme.colorScheme.primary,
        onPinCorrect = {
            // pin is correct, navigate or hide pin lock
        },
        onPinIncorrect = {
            // pin is incorrect, show error
        },
        onPinCreated = {
            // pin created for the first time, navigate or hide pin lock
        }
    )
}