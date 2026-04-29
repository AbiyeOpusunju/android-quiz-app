package com.dma.studentapplication.data

import android.content.Context
import com.dma.studentapplication.model.QuestionList
import kotlinx.serialization.json.Json

class QuizRepository(private val context: Context) {

    fun getTopics(): List<String> {
        return context.assets.list("")
            ?.filter { it.endsWith(".json") }
            ?.map { it.removeSuffix(".json").replace("_", " ") }
            ?: emptyList()
    }
    fun loadQuestions(topic: String): List<com.dma.studentapplication.model.Question> {
        val fileName = topic.replace(" ", "_") + ".json"
        val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        val questionList = Json.decodeFromString<QuestionList>(jsonString)
        return questionList.questions
    }
}