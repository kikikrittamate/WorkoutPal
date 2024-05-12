package com.example.workoutpal

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector

class NavItemInfo (
    val label: String = "",
    val icon: ImageVector = Icons.Filled.List,
    val route: String = ""
) {

    fun getAllNavItems(): List<NavItemInfo> {
        return listOf(
            NavItemInfo("Schedule", Icons.Filled.DateRange, DestinationScreens.Schedule.route),
            NavItemInfo("Calories", Icons.Filled.List, DestinationScreens.Calories.route),
            NavItemInfo("Workout", Icons.Filled.PlayArrow, DestinationScreens.Workout.route)
        )
    }
}