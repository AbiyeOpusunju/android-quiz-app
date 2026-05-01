package com.dma.studentapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_results")
data class QuizResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val topic: String,
    val score: Int,
    val total: Int,
    val date: String,
    val questionsJson: String = "",
    val userAnswersJson: String = ""
)