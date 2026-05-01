package com.dma.studentapplication.ui.screens

import androidx.compose.runtime.Composable
import com.dma.studentapplication.data.QuizResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dma.studentapplication.model.Question
import kotlinx.serialization.json.Json


@Composable
fun HistoryDetailScreen(
    result: QuizResult,
    onBackClicked: () -> Unit
) {
    val questions = Json.decodeFromString<List<Question>>(result.questionsJson)
    val userAnswers = result.userAnswersJson.split(",").map { it.toInt() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${result.topic.replaceFirstChar { it.uppercase() }} - ${result.score}/${result.total} - ${result.date}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onBackClicked) {
                Icon(Icons.Filled.Close, contentDescription = "Close")
            }
        }
    }
}