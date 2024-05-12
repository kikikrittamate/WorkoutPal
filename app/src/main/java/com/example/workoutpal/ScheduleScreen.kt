package com.example.workoutpal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.util.Locale
import androidx.compose.foundation.text.KeyboardOptions


@Composable
fun ScheduleScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Workout Schedule",
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        WeekTable(title = "Workout", columns = listOf("Day", "Workout"))
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Calories Schedule",
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        WeekTable(title = "Calories", columns = listOf("Day", "Calories"))
    }
}

@Composable
fun WeekTable(title: String, columns: List<String>) {
    val borderColor = Color(0xFFCCCCCC)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(color = Color.White, shape = RoundedCornerShape(4.dp))
            .padding(4.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black),
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.CenterHorizontally)
        )
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
        val daysOfWeek = DayOfWeek.values()
        for (day in daysOfWeek.take(7)) {
            TableRow(day = day)
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
fun TableRow(day: DayOfWeek) {
    val dayName = day.name.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    // State to hold the workout/calories input
    val workoutInput = remember { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = dayName,
            style = TextStyle(fontSize = 12.sp, color = Color.Black),
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            textAlign = TextAlign.Start
        )

        // Input field for workout/calories
        TextField(
            value = workoutInput.value,
            onValueChange = { workoutInput.value = it },
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            textStyle = TextStyle(fontSize = 12.sp, color = Color.White),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            singleLine = true
        )


    }
}

