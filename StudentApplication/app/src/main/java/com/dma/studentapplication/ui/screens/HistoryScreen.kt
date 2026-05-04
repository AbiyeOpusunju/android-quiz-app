package com.dma.studentapplication.ui.screens

import androidx.compose.runtime.Composable
import com.dma.studentapplication.data.QuizResult
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HistoryScreen(
    results: List<QuizResult>,
    topics: List<String>,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    onResultClicked: (QuizResult) -> Unit,
    onBackClicked: () -> Unit
) {
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
            IconButton(onClick = onBackClicked) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Quiz History",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.weight(1f)
            )
            var expanded by remember { mutableStateOf(false) }
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Filled.FilterList, contentDescription = "Filter")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("All") },
                    onClick = {
                        onFilterSelected("All")
                        expanded = false
                    }
                )
                topics.forEach { topic ->
                    DropdownMenuItem(
                        text = { Text(topic.replaceFirstChar { it.uppercase() }) },
                        onClick = {
                            onFilterSelected(topic)
                            expanded = false
                        }
                    )
                }
            }
        }
        LazyColumn {
            items(results) { result ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onResultClicked(result) }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = result.topic.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "${result.date} - ${result.score}/${result.total}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}