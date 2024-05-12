package com.example.workoutpal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.workoutpal.customui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.wrapContentSize(),
                    color = Color.Black
                ) {
                    MainScreenWithBottomNavBar()
                }
            }
        }
    }
}

@Composable
fun MainScreenWithBottomNavBar() {
    val navController = rememberNavController()
    var navSelectedItem by remember {
        mutableStateOf(0)
    }
    Scaffold(bottomBar = {
        NavigationBar {
            NavItemInfo().getAllNavItems().forEachIndexed { index, itemInfo ->
                NavigationBarItem(selected = (index == navSelectedItem),
                    onClick = {
                        navSelectedItem = index
                        navController.navigate(itemInfo.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(imageVector = itemInfo.icon, contentDescription = itemInfo.label) },
                    label = { Text(text = itemInfo.label) })
            }
        }
    }) { paddingValues -> NavHost(navController = navController,
        startDestination = DestinationScreens.Schedule.route,
        Modifier.padding(paddingValues)) {
        composable(route = DestinationScreens.Schedule.route) {
            ScheduleScreen()
        }
        composable(route = DestinationScreens.Calories.route) {
            CaloriesScreen()
        }
        composable(route = DestinationScreens.Workout.route) {
            WorkoutScreen()
        }
    }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
    Button(onClick = { /*TODO*/ }) {
        Text(text = "Click Me")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppTheme {
        MainScreenWithBottomNavBar()
    }
}