package com.example.app_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.app_kotlin.trivia.Question
import com.example.app_kotlin.trivia.QuizViewModel
import com.example.app_kotlin.ui.theme.AppkotlinTheme

class TriviaAppActivity : ComponentActivity() {

    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppkotlinTheme {
                val uiState by quizViewModel.uiState.collectAsState()
                TriviaScreen(
                    uiState = uiState,
                    onSelectOption = { quizViewModel.onSelectOption(it) },
                    onConfirm = { quizViewModel.onConfirm() },
                    onPlayAgain = { quizViewModel.onPlayAgain() },
                    onBack = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TriviaScreen(
    uiState: com.example.app_kotlin.trivia.QuizUiState,
    onSelectOption: (Int) -> Unit,
    onConfirm: () -> Unit,
    onPlayAgain: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Trivia App",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E88E5)
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (uiState.isFinished) {
                FinishedScreen(
                    score = uiState.score,
                    total = uiState.questions.size,
                    onPlayAgain = onPlayAgain
                )
            } else {
                QuestionScreen(
                    question = uiState.currentQuestion,
                    currentIndex = uiState.currentIndex,
                    total = uiState.questions.size,
                    selectedIndex = uiState.selectedIndex,
                    onSelectOption = onSelectOption,
                    onConfirm = onConfirm
                )
            }
        }
    }
}

@Composable
fun QuestionScreen(
    question: Question,
    currentIndex: Int,
    total: Int,
    selectedIndex: Int?,
    onSelectOption: (Int) -> Unit,
    onConfirm: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Pregunta ${currentIndex + 1} de $total",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = question.title,
            style = MaterialTheme.typography.headlineSmall
        )

        question.options.forEachIndexed { index, option ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedIndex == index,
                    onClick = { onSelectOption(index) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedIndex != null
        ) {
            Text(text = "Confirmar")
        }
    }
}

@Composable
fun FinishedScreen(
    score: Int,
    total: Int,
    onPlayAgain: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Puntuación final: $score de $total")
        Button(onClick = onPlayAgain) {
            Text(text = "Jugar de nuevo")
        }
    }
}
