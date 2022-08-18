package com.android.jettrivia.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.jettrivia.data.DataOrException
import com.android.jettrivia.model.QuestionsItem
import com.android.jettrivia.repository.QuestionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrivaViewModel @Inject constructor(private val repository: QuestionsRepository): ViewModel() {
    // this MutableState is meant to use with composable and to simplify MutableStateFlow
val data: MutableState<DataOrException<MutableList<QuestionsItem>,Boolean,Exception>> = mutableStateOf(
    DataOrException(null,true,Exception(""))
)
    init {
        getAllQuestions()
    }
    private fun getAllQuestions() {
        viewModelScope.launch {
            this@TrivaViewModel.data.value = this@TrivaViewModel.repository.getAllQuestions()
        }
    }
    fun getTotalQuestionCount(): Int? {
        return this.data.value.data?.toMutableList()?.size
    }
}