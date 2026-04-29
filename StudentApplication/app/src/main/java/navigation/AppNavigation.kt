package com.dma.studentapplication.navigation

import androidx.navigation.compose.composable
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.dma.studentapplication.ui.screens.TopicListScreen
import com.dma.studentapplication.viewmodel.QuizViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

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
        composable(route = Screen.Summary.name + "/{score}/{total}/{topic}") {
            // SummaryScreen - placeholder
        }

    }
}
