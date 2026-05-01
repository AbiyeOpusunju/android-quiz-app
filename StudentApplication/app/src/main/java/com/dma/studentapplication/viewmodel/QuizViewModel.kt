package com.dma.studentapplication.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.dma.studentapplication.data.QuizRepository
import com.dma.studentapplication.model.Question
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.dma.studentapplication.data.QuizDatabase
import com.dma.studentapplication.data.QuizResult
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = QuizRepository(application.applicationContext)
    private val database = QuizDatabase.getDatabase(application.applicationContext)
    private val dao = database.quizResultDao()
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

    val userAnswers = mutableStateListOf<Int>()

    fun loadQuiz(topic: String) {
        questions = repository.loadQuestions(topic).shuffled().take(10)
        currentQuestionIndex = 0
        score = 0
        selectedAnswerIndex = -1
        isAnswered = false
        userAnswers.clear()
    }
    fun selectAnswer(index: Int) {
        if (!isAnswered) {
            selectedAnswerIndex = index
            isAnswered = true
            userAnswers.add(index)
            if (index == questions[currentQuestionIndex].correctAnswerIndex) {
                score++
            }
        }
    }
    fun nextQuestion() {
        selectedAnswerIndex = -1
        isAnswered = false
        currentQuestionIndex++
    }
    fun getTopics(): List<String> {
        return repository.getTopics()
    }
    fun saveQuizResult(topic: String) {
        val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        viewModelScope.launch {
            dao.insert(
                QuizResult(
                    topic = topic,
                    score = score,
                    total = questions.size,
                    date = currentDate
                )
            )
        }
    }
    fun getAllResults(): Flow<List<QuizResult>> {
        return dao.getAllResults()
    }

    fun getResultsByTopic(topic: String): Flow<List<QuizResult>> {
        return dao.getResultsByTopic(topic)
    }
}