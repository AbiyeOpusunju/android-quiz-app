package com.dma.studentapplication.navigation

import com.dma.studentapplication.ui.screens.SummaryScreen
import com.dma.studentapplication.ui.screens.QuestionScreen
import androidx.navigation.compose.composable
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.dma.studentapplication.ui.screens.TopicListScreen
import com.dma.studentapplication.viewmodel.QuizViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dma.studentapplication.ui.screens.HistoryScreen
import com.dma.studentapplication.ui.screens.HistoryDetailScreen
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: QuizViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.TopicList.name
    ) {
        composable(route = Screen.TopicList.name) {
            TopicListScreen(
                topics = viewModel.getTopics(),
                onTopicSelected = { topic ->
                    viewModel.loadQuiz(topic)
                    navController.navigate(Screen.Question.name + "/$topic")
                },

                onHistoryClicked = {
                    navController.navigate(Screen.History.name)
                }
            )
        }
        composable(route = Screen.Question.name + "/{topic}") { backStackEntry ->
            val topic = backStackEntry.arguments?.getString("topic") ?: ""
            val currentQuestion = viewModel.questions[viewModel.currentQuestionIndex]

            QuestionScreen(
                question = currentQuestion,
                questionNumber = viewModel.currentQuestionIndex + 1,
                selectedAnswerIndex = viewModel.selectedAnswerIndex,
                isAnswered = viewModel.isAnswered,
                onAnswerSelected = { viewModel.selectAnswer(it) },
                onNextClicked = {
                    if (viewModel.currentQuestionIndex < viewModel.questions.size - 1) {
                        viewModel.nextQuestion()
                    } else {
                        viewModel.saveQuizResult(topic)
                        navController.navigate(
                            Screen.Summary.name + "/${viewModel.score}/${viewModel.questions.size}/$topic"
                        )
                    }
                }
            )
        }
        composable(route = Screen.Summary.name + "/{score}/{total}/{topic}") { backStackEntry ->
            val score = backStackEntry.arguments?.getString("score")?.toIntOrNull() ?: 0
            val total = backStackEntry.arguments?.getString("total")?.toIntOrNull() ?: 0

            SummaryScreen(
                score = score,
                total = total,
                questions = viewModel.questions,
                userAnswers = viewModel.userAnswers,
                onRestartClicked = {
                    navController.popBackStack(Screen.TopicList.name, inclusive = false)
                }
            )
        }
        composable(route = Screen.History.name) {
            var selectedFilter by remember { mutableStateOf("All") }
            val results by (
                if (selectedFilter == "All") viewModel.getAllResults()
                else viewModel.getResultsByTopic(selectedFilter)
            ).collectAsState(initial = emptyList())

            HistoryScreen(
                results = results,
                topics = viewModel.getTopics(),
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it },
                onResultClicked = { result ->
                    navController.navigate(Screen.HistoryDetail.name + "/${result.id}")
                },
                onBackClicked = { navController.popBackStack() }
            )
        }
        composable(route = Screen.HistoryDetail.name + "/{resultId}") { backStackEntry ->
            val resultId = backStackEntry.arguments?.getString("resultId")?.toIntOrNull() ?: 0
            val result by viewModel.getResultById(resultId).collectAsState(initial = null)

            result?.let {
                HistoryDetailScreen(
                    result = it,
                    onBackClicked = { navController.popBackStack() }
                )
            }
        }
    }
}
