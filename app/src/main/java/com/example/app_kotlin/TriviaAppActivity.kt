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
import androidx.compose.foundation.layout.height
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
import com.example.app_kotlin.trivia.QuizUiState
import com.example.app_kotlin.trivia.QuizViewModel
import com.example.app_kotlin.ui.theme.AppkotlinTheme

class TriviaAppActivity : ComponentActivity() {

    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppkotlinTheme {
                val state by quizViewModel.uiState.collectAsState()
                TriviaScreen(
                    state = state,
                    viewModel = quizViewModel,
                    onBack = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TriviaScreen(
    state: QuizUiState,
    viewModel: QuizViewModel,
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
            if (state.isFinished) {
                FinishedScreen(
                    score = state.score,
                    total = state.questions.size * 100,
                    onPlayAgain = viewModel::onPlayAgain
                )
            } else {
                QuestionScreen(
                    state = state,
                    onSelectOption = viewModel::onSelectOption,
                    onConfirm = if (state.showFeedback) viewModel::onNextQuestion else viewModel::onConfirm
                )
            }
        }
    }
}

@Composable
fun QuestionScreen(
    state: QuizUiState,
    onSelectOption: (Int) -> Unit,
    onConfirm: () -> Unit,
) {
    val question = state.currentQuestion
    val currentIndex = state.currentIndex
    val selectedIndex = state.selectedIndex

    if (question != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Pregunta ${currentIndex + 1} de ${state.questions.size}",
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
                        onClick = { if (!state.showFeedback) onSelectOption(index) },
                        enabled = !state.showFeedback
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            if (state.showFeedback) {
                Text(
                    text = state.feedback,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (state.feedback.startsWith("✅")) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedIndex != null || state.showFeedback
            ) {
                val buttonText = when {
                    state.showFeedback && state.isLastQuestion -> "Ver resultados"
                    state.showFeedback -> "Siguiente"
                    state.isLastQuestion -> "Ver resultados"
                    else -> "Confirmar"
                }
                Text(text = buttonText)
            }
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
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "¡Quiz Finalizado!",
             style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Tu puntuación final es: $score / $total",
             style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(48.dp))

        Button(onClick = onPlayAgain) {
            Text(text = "Jugar de nuevo")
        }
    }
}
