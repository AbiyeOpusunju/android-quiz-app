package com.dma.studentapplication.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color

@Composable
fun SummaryScreen(
    score: Int,
    total: Int,
    questions: List<com.dma.studentapplication.model.Question>,
    userAnswers: List<Int>,
    onRestartClicked: () -> Unit
)  {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Quiz Finished!",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Text(
            text = "You scored $score out of $total",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {            itemsIndexed(questions) { index, question ->
            val isCorrect = userAnswers[index] == question.correctAnswerIndex
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCorrect) Color(0xFFC8E6C9) else Color(0xFFFFCDD2)
                )
            ) {
            }
        }
        }
        Button(
            onClick = onRestartClicked
        ) {
            Text(text = "Restart Quiz")
        }
    }
}