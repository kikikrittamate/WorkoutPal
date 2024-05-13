package com.example.workoutpal

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.TextField

@Composable
fun CaloriesScreen(context: Context) {
    var foodList by remember { mutableStateOf(loadFoodList(context)) }
    var foodName by remember { mutableStateOf("") }
    var calorieCount by remember { mutableStateOf("") }
    var totalCalories by remember { mutableStateOf(calculateTotalCalories(foodList)) }

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
                        saveFoodList(context, foodList)
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
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                // Reset all states and clear saved data
                foodList = emptyList()
                totalCalories = 0
                foodName = ""
                calorieCount = ""
                saveFoodList(context, emptyList())
            }
        ) {
            Text("Reset")
        }
    }
}


private fun loadFoodList(context: Context): List<Pair<String, Int>> {
    val sharedPreferences = context.getSharedPreferences("WorkoutPal", MODE_PRIVATE)
    val serializedFoodList = sharedPreferences.getString("foodList", "") ?: ""
    return if (serializedFoodList.isNotEmpty()) {
        serializedFoodList.split(";").map {
            val (food, calories) = it.split(",")
            food to calories.toInt()
        }
    } else {
        emptyList()
    }
}

private fun saveFoodList(context: Context, foodList: List<Pair<String, Int>>) {
    val serializedFoodList = foodList.joinToString(";") { (food, calories) ->
        "$food,$calories"
    }
    val sharedPreferences = context.getSharedPreferences("WorkoutPal", MODE_PRIVATE)
    sharedPreferences.edit().putString("foodList", serializedFoodList).apply()
}

private fun calculateTotalCalories(foodList: List<Pair<String, Int>>): Int {
    return foodList.sumBy { it.second }
}
