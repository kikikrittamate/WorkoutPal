package com.example.workoutpal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.util.Locale
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.MutableState

@Composable
fun ScheduleScreen(viewModel: ScheduleViewModel) {
    val scheduleState = viewModel.scheduleState.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Workout and Calories Schedule",
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        CombinedWeekTable(columns = listOf("Day", "Workout", "Calories"), scheduleState = scheduleState, onScheduleChange = { day, workout, calories ->
            viewModel.updateSchedule(day, workout, calories)
        })
    }
}

@Composable
fun CombinedWeekTable(columns: List<String>, scheduleState: List<DaySchedule>, onScheduleChange: (DayOfWeek, String, Int) -> Unit) {
    val borderColor = Color(0xFFCCCCCC)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(color = Color.White, shape = RoundedCornerShape(4.dp))
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (column in columns) {
                Text(
                    text = column,
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black),
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                )
            }
        }
        // Add rows for each day of the week here
        for (daySchedule in scheduleState) {
            CombinedTableRow(daySchedule = daySchedule, onScheduleChange = onScheduleChange)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = borderColor)
            )
        }
    }
}

@Composable
fun CombinedTableRow(daySchedule: DaySchedule, onScheduleChange: (DayOfWeek, String, Int) -> Unit) {
    val workoutInput = remember { mutableStateOf(daySchedule.workout) }
    val caloriesInput = remember { mutableStateOf(daySchedule.calories.toString()) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = daySchedule.day.name.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
            style = TextStyle(fontSize = 12.sp, color = Color.Black),
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            textAlign = TextAlign.Start
        )

        // Input field for workout
        TextField(
            value = workoutInput.value,
            onValueChange = {
                workoutInput.value = it
                onScheduleChange(daySchedule.day, it, caloriesInput.value.toIntOrNull() ?: 0)
            },
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            textStyle = TextStyle(fontSize = 12.sp, color = Color.White),
            singleLine = true
        )

        // Input field for calories
        TextField(
            value = caloriesInput.value,
            onValueChange = {
                caloriesInput.value = it
                onScheduleChange(daySchedule.day, workoutInput.value, it.toIntOrNull() ?: 0)
            },
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            textStyle = TextStyle(fontSize = 12.sp, color = Color.White),
            singleLine = true
        )
    }
}

data class DaySchedule(val day: DayOfWeek, var workout: String, var calories: Int)

class ScheduleViewModel : ViewModel() {
    private val _scheduleState = mutableStateOf<List<DaySchedule>>(listOf())

    val scheduleState: MutableState<List<DaySchedule>> = _scheduleState

    init {
        // Initialize the scheduleState with default data
        val daysOfWeek = DayOfWeek.values()
        val defaultSchedule = daysOfWeek.map { DaySchedule(it, "", 0) }
        _scheduleState.value = defaultSchedule
    }

    fun updateSchedule(day: DayOfWeek, workout: String, calories: Int) {
        val updatedSchedule = scheduleState.value.toMutableList()
        val index = updatedSchedule.indexOfFirst { it.day == day }
        if (index != -1) {
            updatedSchedule[index] = DaySchedule(day, workout, calories)
            _scheduleState.value = updatedSchedule
        }
    }
}
