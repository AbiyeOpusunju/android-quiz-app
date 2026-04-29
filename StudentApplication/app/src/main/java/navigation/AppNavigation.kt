package com.dma.studentapplication.navigation

import androidx.navigation.compose.composable
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.TopicList.name
    ) {
        composable(route = Screen.TopicList.name) {
            // TopicListScreen - placeholder
        }
        composable(route = Screen.Question.name + "/{topic}") {
            // QuestionScreen - placeholder
        }

    }
}
