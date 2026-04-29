package com.dma.studentapplication.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.dma.studentapplication.model.Question
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dma.studentapplication.data.QuizRepository

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = QuizRepository(application.applicationContext)
    var questions by mutableStateOf<List<Question>>(emptyList())
        private set

    var currentQuestionIndex by mutableStateOf(0)
        private set

    var score by mutableStateOf(0)
        private set

    var selectedAnswerIndex by mutableStateOf(-1)
        private set

    var isAnswered by mutableStateOf(false)
        private set

    fun getTopics(): List<String> {
        return repository.getTopics()
    }
}