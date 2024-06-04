package com.example.mytimer

import AppDB
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.mytimer.ui.theme.MyTimerTheme
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {

    private lateinit var database: TimerDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = (application as AppDB).database

        setContent {
            // Your UI content here
            MyTimerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CountdownTimerWithReset()
                }
            }
        }

        // Insert a user into the database

    }
}


@Composable
fun CountdownTimerWithReset() {
    var timeInput by remember { mutableStateOf("00:00") }
    var timeLeft by remember { mutableStateOf(0) }
    var isPaused by remember { mutableStateOf(false) }
    var sessionComplete by remember { mutableStateOf(false) }
    val tags = listOf("Mobile Dev", "Reading", "Writing", "Ukulele", "Study")
    var selectedTag by remember { mutableStateOf(tags.first()) }
    var showDropdownMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current


    LaunchedEffect(timeLeft, isPaused) {
        if (timeLeft > 0 && !isPaused) {
            delay(1000L)
            timeLeft--
            if (timeLeft == 0) {
                sessionComplete = true
            }
        }
    }

    fun enforceTimeFormat(input: String): String {
        val cleanInput = input.filter { it.isDigit() }
        val minutes = cleanInput.take(2).padStart(2, '0')
        val seconds = cleanInput.drop(2).take(2).padStart(2, '0')
        return "$minutes:$seconds"
    }

    fun startTimer() {
        val parts = timeInput.split(":")
        if (parts.size == 2) {
            val minutes = parts[0].toIntOrNull() ?: 0
            val seconds = parts[1].toIntOrNull() ?: 0
            timeLeft = minutes * 60 + seconds
            isPaused = false
        }
    }

    fun resetTimer() {
        val parts = timeInput.split(":")
        if (parts.size == 2) {
            val minutes = parts[0].toIntOrNull() ?: 0
            val seconds = parts[1].toIntOrNull() ?: 0
            timeLeft = minutes * 60 + seconds
            isPaused = true
        }
    }

    fun clearTimer() {
        timeLeft = 0
        isPaused = false
    }

    Column {
        TextField(
            value = timeInput,
            onValueChange = {
                val formattedInput = enforceTimeFormat(it)
                if (formattedInput.length <= 5) {
                    timeInput = formattedInput
                }
            },
            label = { Text("Enter Time (mm:ss)") },
            singleLine = true
        )

        Text(text = "Time left: ${timeLeft / 60}:${"%02d".format(timeLeft % 60)}")
        Button(onClick = { isPaused = !isPaused }) {
            Text(text = if (isPaused) "Resume" else "Pause")
        }
        Button(onClick = { if (timeLeft > 0) resetTimer() else startTimer() }) {
            Text(text = if (timeLeft > 0) "Reset" else "Start")
        }
        Button(onClick = { clearTimer() }) {
            Text(text = "Clear")
        }

        if (sessionComplete) {
            Text(text = "Would you like to save the session?")
            Button(onClick = { showDropdownMenu = true }) {
                Text(text = "Save Session")
            }
            DropdownMenu(
                expanded = showDropdownMenu,
                onDismissRequest = { showDropdownMenu = false }
            ) {
                tags.forEach { tag ->
                    DropdownMenuItem(text = { Text(tag) }, onClick = {
                        selectedTag = tag
                        showDropdownMenu = false
                        sessionComplete = false
                        //create a toast message selected tag is saved with time input
                        //save time with tag to timer database


                        Toast.makeText(
                            context,
                            "Session saved with tag $selectedTag and time $timeInput",
                            Toast.LENGTH_SHORT
                        ).show()
                })
            }
            }
            Button(onClick = { sessionComplete = false }) {
                Text(text = "Don't Save")
            }
        }
    }
}
