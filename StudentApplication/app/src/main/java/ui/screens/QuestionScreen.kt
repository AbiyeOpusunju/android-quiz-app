package com.dma.studentapplication.ui.screens

import androidx.compose.runtime.Composable
import com.dma.studentapplication.model.Question

@Composable
fun QuestionScreen(
    question: Question,
    questionNumber: Int,
    selectedAnswerIndex: Int,
    isAnswered: Boolean,
    onAnswerSelected: (Int) -> Unit,
    onNextClicked: () -> Unit
) {
}