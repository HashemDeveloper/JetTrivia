package com.android.jettrivia.network

import com.android.jettrivia.model.Questions
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface QuestionsAPI {
    @GET("world.json")
    suspend fun getAllQuestions(): Questions
}