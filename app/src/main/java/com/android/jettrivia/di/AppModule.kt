package com.android.jettrivia.di

import com.android.jettrivia.network.QuestionsAPI
import com.android.jettrivia.repository.QuestionsRepository
import com.android.jettrivia.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideQuestionsRepository(api: QuestionsAPI) = QuestionsRepository(api)

    @Singleton
    @Provides
    fun provideQuestionAPI(): QuestionsAPI {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuestionsAPI::class.java)
    }
}