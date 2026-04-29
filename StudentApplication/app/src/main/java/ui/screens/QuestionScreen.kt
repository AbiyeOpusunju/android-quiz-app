package com.dma.studentapplication.ui.screens

import androidx.compose.runtime.Composable
import com.dma.studentapplication.model.Question
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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