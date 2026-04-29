package com.dma.studentapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dma.studentapplication.data.QuizRepository

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = QuizRepository(application.applicationContext)

    fun getTopics(): List<String> {
        return repository.getTopics()
    }
}