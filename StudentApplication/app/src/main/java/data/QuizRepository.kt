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
}