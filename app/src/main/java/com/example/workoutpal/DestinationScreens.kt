package com.example.workoutpal

sealed class DestinationScreens(val route: String) {
    object Schedule : DestinationScreens("schedule")
    object Calories : DestinationScreens("calories")
    object Workout : DestinationScreens("workout")
}