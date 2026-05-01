package com.dma.studentapplication.ui.screens

import androidx.compose.runtime.Composable
import com.dma.studentapplication.data.QuizResult

@Composable
fun HistoryScreen(
    results: List<QuizResult>,
    topics: List<String>,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    onResultClicked: (QuizResult) -> Unit,
    onBackClicked: () -> Unit
) {
}