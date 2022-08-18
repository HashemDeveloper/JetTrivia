package com.android.jettrivia.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.jettrivia.R
import com.android.jettrivia.model.QuestionsItem
import com.android.jettrivia.screens.TrivaViewModel
import com.android.jettrivia.utils.AppColor
import java.time.temporal.TemporalAdjusters.next

@Preview
@Composable
fun Question(viewModel: TrivaViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val isLoading: Boolean? = viewModel.data.value.loading
    val questions: MutableList<QuestionsItem>? = viewModel.data.value.data?.toMutableList()
    val isError: Exception? = viewModel.data.value.e
    val questionIndex = remember {
        mutableStateOf(0)
    }
    isLoading?.let { loading ->
        if (loading) {
            CircularProgressIndicator()
        } else {
            questions?.let { list ->
                val questionsItem = try {
                    list[questionIndex.value]
                } catch (e: Exception) {
                    null
                }
                questionsItem?.let { singleQuestion ->
                    QuestionDisplay(questionsItem = singleQuestion, questionIndex, viewModel) {
                        val increment = it + 1
                        questionIndex.value = increment
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionDisplay(
    questionsItem: QuestionsItem,
    index: MutableState<Int>,
    viewModel: TrivaViewModel,
    onNextClicked: (Int) -> Unit = {}
) {

    val choicesState = remember(questionsItem) {
        questionsItem.choices.toMutableList()
    }
    val answerState = remember(questionsItem) {
        mutableStateOf<Int?>(null)
    }
    val correctAnswerState = remember(questionsItem) {
        mutableStateOf<Boolean?>(null)
    }
    val updateAnswer: (Int) -> Unit = remember(questionsItem) {
        {
            answerState.value = it
            correctAnswerState.value = choicesState[it] == questionsItem.answer
        }
    }
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = AppColor.mDarkPurple
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            if (index.value >= 3) {
                ShowProgress(score = index.value)
            }
            QuestionTracker(counter = index.value)
            DottedLine(pathEffect)
            Column {
                Text(
                    text = questionsItem.question,
                    modifier = Modifier
                        .padding(6.dp)
                        .align(alignment = Alignment.Start)
                        .fillMaxHeight(.30f),
                    fontSize = 17.sp,
                    color = AppColor.mOffWhite,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp
                )
                // choices
                choicesState.forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                            .fillMaxWidth()
                            .height(45.dp)
                            .border(
                                width = 4.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        AppColor.mOffDarkPurple,
                                        AppColor.mOffDarkPurple
                                    )
                                ),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .clip(
                                RoundedCornerShape(
                                    topStartPercent = 50,
                                    topEndPercent = 50,
                                    bottomEndPercent = 50,
                                    bottomStartPercent = 50
                                )
                            )
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (answerState.value == index),
                            onClick = {
                                updateAnswer(index)
                            },
                            modifier = Modifier.padding(start = 16.dp),
                            colors = RadioButtonDefaults.colors(
                                selectedColor =
                                if (correctAnswerState.value == true && index == answerState.value) {
                                    Color.Green.copy(0.2f)
                                } else {
                                    Color.Red.copy(alpha = 0.2f)
                                }
                            )
                        )
                        val annotatedString = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Light, color =
                                    if (correctAnswerState.value == true && index == answerState.value) {
                                        Color.Green
                                    } else if (correctAnswerState.value == false && index == answerState.value) {
                                        Color.Red
                                    } else {
                                        AppColor.mOffWhite
                                    }

                                )
                            ) {
                                append(item)
                            }
                        }
                        Text(text = annotatedString)
                    }
                }
                Button(
                    onClick = { onNextClicked(index.value) },
                    modifier = Modifier
                        .padding(3.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(32.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = AppColor.mLightBlue)
                ) {
                    Text(
                        text = stringResource(id = R.string.next),
                        modifier = Modifier.padding(4.dp),
                        color = AppColor.mOffWhite,
                        fontSize = 17.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun DottedLine(pathEffect: PathEffect) {
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp), onDraw = {
        drawLine(
            color = AppColor.mLightGray,
            start = Offset(0f, 0f),
            end = Offset(size.width, y = 0f),
            pathEffect = pathEffect
        )
    })
}

@Preview
@Composable
fun ShowProgress(score: Int = 12) {
    val progressFactorState = remember(score) {
        mutableStateOf(score * 0.005f)
    }
    val gradient = Brush.linearGradient(listOf(Color(0xFFF95975), Color(0xFFBE6BE5)))
    Row(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .height(45.dp)
            .border(
                width = 4.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        AppColor.mLightPurple,
                        AppColor.mLightPurple
                    )
                ),
                shape = RoundedCornerShape(30.dp),
            )
            .clip(
                RoundedCornerShape(
                    topEndPercent = 50,
                    topStartPercent = 50,
                    bottomStartPercent = 50,
                    bottomEndPercent = 50
                )
            )
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {},
            contentPadding = PaddingValues(1.dp),
            modifier = Modifier
                .fillMaxWidth(progressFactorState.value)
                .background(brush = gradient),
            enabled = false,
            elevation = null,
            colors = buttonColors(Color.Transparent, disabledBackgroundColor = Color.Transparent)
        ) {
            Text(
                text = "${score * 10}",
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(23.dp))
                    .fillMaxHeight(0.87f)
                    .fillMaxWidth()
                    .padding(6.dp),
                color = AppColor.mOffWhite,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun QuestionTracker(counter: Int = 10, outOff: Int = 100) {
    Text(text = buildAnnotatedString {
        withStyle(style = ParagraphStyle(textIndent = TextIndent.None)) {
            withStyle(
                style = SpanStyle(
                    color = AppColor.mLightGray,
                    fontWeight = FontWeight.Bold, fontSize = 27.sp
                )
            ) {
                append("Question $counter/")
                withStyle(
                    style = SpanStyle(
                        color = AppColor.mLightGray,
                        fontWeight = FontWeight.Light, fontSize = 14.sp
                    )
                ) {
                    append("$outOff")
                }
            }
        }
    }, modifier = Modifier.padding(20.dp))
}