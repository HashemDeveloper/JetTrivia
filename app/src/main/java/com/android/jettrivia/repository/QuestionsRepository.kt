package com.android.jettrivia.repository

import android.util.Log
import com.android.jettrivia.BuildConfig
import com.android.jettrivia.data.DataOrException
import com.android.jettrivia.model.QuestionsItem
import com.android.jettrivia.network.QuestionsAPI
import javax.inject.Inject

class QuestionsRepository @Inject constructor(private val api: QuestionsAPI) {
    private val dataOrException: DataOrException<MutableList<QuestionsItem>, Boolean, Exception> = DataOrException()

    suspend fun getAllQuestions(): DataOrException<MutableList<QuestionsItem>,Boolean,Exception> {
        try {
            this.dataOrException.loading = true
            this.dataOrException.data = this.api.getAllQuestions()
            if (this.dataOrException.data.toString().isNotEmpty()) {
                this.dataOrException.loading = false
            }
        } catch (e: Exception) {
            this.dataOrException.e = e
            if (BuildConfig.DEBUG) {
                e.localizedMessage?.let { Log.d(TAG, it) }
            }
            this.dataOrException.loading = false
        }
        return this.dataOrException
    }
    companion object {
        private val TAG = QuestionsRepository::class.java.canonicalName
    }
}