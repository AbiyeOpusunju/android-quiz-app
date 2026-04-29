package com.dma.studentapplication.navigation

import com.dma.studentapplication.ui.screens.QuestionScreen
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
                        navController.navigate(
                            Screen.Summary.name + "/${viewModel.score}/${viewModel.questions.size}/$topic"
                        )
                    }
                }
            )
        }
        composable(route = Screen.Summary.name + "/{score}/{total}/{topic}") {
            // SummaryScreen - placeholder
        }

    }
}
