package com.example.workoutpal

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutpal.customui.theme.AppTheme
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


data class Workout(
    val exercise: String = "",
    val set: Int = 4, // Changed Number to Int for simplicity
)

@Composable
fun WorkoutScreen() {
    val screenContext = LocalContext.current
    val workoutList = remember { mutableStateListOf<Workout>() }

    val onFirebaseQuerySuccess: (QuerySnapshot) -> Unit = { result ->
        if (!result.isEmpty) {
            val resultDocuments = result.documents
            for (document in resultDocuments) {
                val workout: Workout? = document.toObject(Workout::class.java)
                workout?.let {
                    workoutList.add(it)
                }
            }
        }
    }

    val onFirebaseQueryFailed: (Exception) -> Unit = { e ->
        Toast.makeText(screenContext, e.message, Toast.LENGTH_LONG).show()
    }

    getWorkoutsFromFirebase(onFirebaseQuerySuccess, onFirebaseQueryFailed)

    var isTimerRunning by remember { mutableStateOf(false) }
    var currentTime by remember { mutableStateOf(0L) }
    var timerJob by remember { mutableStateOf<Job?>(null) }

    fun startTimer() {
        isTimerRunning = true
        timerJob?.cancel() // Cancel previous job if any
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (isTimerRunning) {
                delay(1000) // Delay for 1 second
                currentTime++
            }
        }
    }

    fun stopTimer() {
        isTimerRunning = false
        timerJob?.cancel()
    }

    fun resetTimer() {
        isTimerRunning = false
        currentTime = 0L
        timerJob?.cancel()
    }

    AppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Workout")
                WorkoutList(workouts = workoutList)
                Spacer(modifier = Modifier.height(16.dp))
                TimerView(currentTime, isTimerRunning, onStart = { startTimer() }, onStop = { stopTimer() }, onReset = { resetTimer() })
            } // End column scope
        }
    }
}

@Composable
fun TimerView(currentTime: Long, isRunning: Boolean, onStart: () -> Unit, onStop: () -> Unit, onReset: () -> Unit) {
    val buttonBackgroundColor = if (isRunning) colorResource(id = R.color.md_theme_error) else colorResource(id = R.color.md_theme_primary)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Time: ${formatTime(currentTime)}", fontSize = 18.sp)
        Button(
            onClick = {
                if (isRunning) {
                    onStop()
                } else {
                    onStart()
                }
            },
            enabled = true, // Always enable the start/stop button
            colors = ButtonDefaults.buttonColors(contentColor = buttonBackgroundColor)
        ) {
            Text(text = if (isRunning) "Stop" else "Start")
        }
        Button(
            onClick = { onReset() },
            enabled = !isRunning // Enable reset button only when timer is not running
        ) {
            Text(text = "Reset")
        }
    }
}





@Composable
fun WorkoutItem(workout: Workout) {
    val isChecked = remember { mutableStateOf(false) }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.md_theme_background)
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth(1f)) {
            Text(
                text = workout.exercise,
                style = TextStyle(
                    color = colorResource(id = R.color.md_theme_primary),
                    fontSize = 20.sp
                ),
                modifier = Modifier.padding(8.dp)
            )
            Row(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked.value,
                    onCheckedChange = { isChecked.value = it },
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Number of sets: ${workout.set}",
                    style = TextStyle(
                        color = colorResource(id = R.color.md_theme_secondary),
                        fontSize = 18.sp
                    )
                )
            }
        }
    }
}

@Composable
fun WorkoutList(workouts: List<Workout>) {
    LazyColumn(contentPadding = PaddingValues(all = 4.dp)) {
        itemsIndexed(workouts) { index, workout ->
            WorkoutItem(workout = workout)
        }
    }
}

private fun getWorkoutsFromFirebase(
    onSuccess: (QuerySnapshot) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val db = Firebase.firestore
    db.collection("workouts").get()
        .addOnSuccessListener { result -> onSuccess(result) }
        .addOnFailureListener { exception -> onFailure(exception) }
}

fun formatTime(timeInSeconds: Long): String {
    val minutes = timeInSeconds / 60
    val seconds = timeInSeconds % 60
    return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
}
