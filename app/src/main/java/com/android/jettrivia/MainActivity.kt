package com.android.jettrivia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.jettrivia.screens.TriviaHome
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
        Surface(color = MaterialTheme.colors.background
        ) {
            TriviaHome()
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DefaultPreview() {

}