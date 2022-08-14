package com.android.jettrivia

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.jettrivia.model.QuestionsItem
import com.android.jettrivia.screens.TrivaViewModel
import com.android.jettrivia.ui.theme.JetTriviaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TriviaAppEntryPoint()
        }
    }
    companion object {
        val TAG = MainActivity::class.java.canonicalName
    }
}
@Composable
private fun TriviaAppEntryPoint() {
    JetTriviaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            TriviaHome()
        }
    }
}
@Composable
private fun TriviaHome(questionViewModel: TrivaViewModel = viewModel()) {
    val isLoading: Boolean? = questionViewModel.data.value.loading
    val questions: MutableList<QuestionsItem>? = questionViewModel.data.value.data?.toMutableList()
    val isError: Exception? = questionViewModel.data.value.e

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

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DefaultPreview() {

}