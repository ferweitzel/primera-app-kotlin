package com.example.app_kotlin.trivia

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuizViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        QuizUiState(
            questions = seedQuestions()
        )
    )

    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    fun onSelectOption(index: Int) {
        val current = _uiState.value
        if (current.isFinished || current.showFeedback) return
        _uiState.value = current.copy(selectedIndex = index)
    }

    fun onConfirm() {
        val current = _uiState.value
        val selected = current.selectedIndex ?: return
        val question = current.currentQuestion ?: return

        val isCorrect = selected == question.correctIndex
        val newScore = if (isCorrect) current.score + 100 else current.score
        
        val feedbackText = if (isCorrect) {
            "✅ Correcto"
        } else {
            "❌ Incorrecto. Respuesta correcta: ${question.options[question.correctIndex]}"
        }

        _uiState.value = current.copy(
            score = newScore,
            showFeedback = true,
            feedback = feedbackText
        )
    }

    fun onNextQuestion() {
        val current = _uiState.value
        val nextIndex = current.currentIndex + 1
        val isFinished = nextIndex >= current.questions.size

        _uiState.value = current.copy(
            currentIndex = if (isFinished) current.currentIndex else nextIndex,
            selectedIndex = null,
            isFinished = isFinished,
            showFeedback = false,
            feedback = ""
        )
    }

    fun onPlayAgain() {
        _uiState.value = QuizUiState(questions = seedQuestions())
    }

    private fun seedQuestions(): List<Question> {
        return listOf(
            Question(
                id = 1,
                title = "¿Qué palabra clave se usa para declarar una variable inmutable en Kotlin?",
                options = listOf("var", "val", "let", "const"),
                correctIndex = 1
            ),
            Question(
                id = 2,
                title = "En Jetpack Compose, ¿qué anotación marca una función como UI?",
                options = listOf("@UI", "@widget", "@Composable", "@Compose"),
                correctIndex = 2
            ),
            Question(
                id = 3,
                title = "¿Qué componente se usa para listas eficientes y scrolleables?",
                options = listOf("Column", "RecyclerView", "Stack", "LazyColumn"),
                correctIndex = 3
            ),
            Question(
                id = 4,
                title = "¿Qué instrucción permite restaurar el estado tras la recreación de una Activity?",
                options = listOf("intentData", "rememberSaveable", "activityState", "bundleConfig"),
                correctIndex = 1
            ),
            Question(
                id = 5,
                title = "¿Cómo se define una función en Kotlin?",
                options = listOf("func", "define", "fun", "function"),
                correctIndex = 2
            ),
            Question(
                id = 6,
                title = "¿Cuál es el punto de entrada principal en una aplicación Android?",
                options = listOf("onCreate", "onStart", "main", "init"),
                correctIndex = 0
            )
        )
    }
}
