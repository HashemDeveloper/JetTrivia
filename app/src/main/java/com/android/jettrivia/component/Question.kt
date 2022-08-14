package com.android.jettrivia.component

import android.util.Log
import androidx.compose.runtime.Composable
import com.android.jettrivia.MainActivity
import com.android.jettrivia.model.QuestionsItem
import com.android.jettrivia.screens.TrivaViewModel

@Composable
fun Question(viewModel: TrivaViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val isLoading: Boolean? = viewModel.data.value.loading
    val questions: MutableList<QuestionsItem>? = viewModel.data.value.data?.toMutableList()
    val isError: Exception? = viewModel.data.value.e
    isLoading?.let { loading ->
        if (loading) {
            Log.d("${MainActivity.TAG}: {LOADING}","Loading")
        } else {
            questions?.let { list ->
                list.forEach { data ->
                    Log.d("${MainActivity.TAG}: {QuestionItem}","$data")
                }
            }
        }
    }
}