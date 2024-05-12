package com.example.workoutpal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.TextField

@Composable
fun CaloriesScreen() {
    var foodList by remember { mutableStateOf(emptyList<Pair<String, Int>>()) }
    var foodName by remember { mutableStateOf("") }
    var calorieCount by remember { mutableStateOf("") }
    var totalCalories by remember { mutableStateOf(0) }

    val body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp // Approximating the body1 size
    )

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = foodName,
                onValueChange = { foodName = it },
                label = { Text("Food") },
                modifier = Modifier.weight(1f).padding(end = 8.dp),
                textStyle = TextStyle(color = Color.Unspecified)
            )
            TextField(
                value = calorieCount,
                onValueChange = { calorieCount = it },
                label = { Text("Calories") },
                modifier = Modifier.weight(1f).padding(start = 8.dp),
                textStyle = TextStyle(color = Color.Unspecified)
            )
            Button(
                onClick = {
                    val calories = calorieCount.toIntOrNull() ?: 0
                    if (calories > 0) {
                        val food = foodName
                        foodList = foodList + Pair(food, calories)
                        totalCalories += calories
                        foodName = ""
                        calorieCount = ""
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Add")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Food",
                style = body1,
                modifier = Modifier.weight(1f)
            )
            Text(
                "Calories",
                style = body1,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        foodList.forEach { (food, calories) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(food, style = body1, modifier = Modifier.weight(1f))
                Text(calories.toString(), style = body1, modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Total Calories: $totalCalories",
            style = body1
        )
    }
}
