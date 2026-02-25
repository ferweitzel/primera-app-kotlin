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
        if (current.isFinished) return
        _uiState.value = current.copy(selectedIndex = index)
    }

    fun onConfirm() {
        val current = _uiState.value
        if (current.selectedIndex == null) return

        val isCorrect = current.currentQuestion.correctIndex == current.selectedIndex
        val newScore = if (isCorrect) current.score + 1 else current.score

        if (current.currentIndex < current.questions.size - 1) {
            _uiState.value = current.copy(
                score = newScore,
                currentIndex = current.currentIndex + 1,
                selectedIndex = null
            )
        } else {
            _uiState.value = current.copy(
                score = newScore,
                isFinished = true,
                selectedIndex = null
            )
        }
    }

    fun onPlayAgain() {
        _uiState.value = QuizUiState(questions = seedQuestions())
    }

    private fun seedQuestions(): List<Question> {
        return listOf(
            Question(
                id = 1,
                title = "¿Qué palabra clave se usa para declarar una variable unmutable en Kotlin?",
                options = listOf("var", "val", "let", "const"),
                correctIndex = 1
            ),
            Question(
                id = 2,
                title = "En Jetpack Compose,¿qué anotación marca una función como UI?",
                options = listOf("@UI", "@widget", "@composable", "@Compose"),
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
                title = "¿Qué instrucción que permite restaurar estado tras recreación de Activity es?",
                options = listOf("intentData", "savedInstanceState", "activityState", "bundleConfig"),
                correctIndex = 1
            )
        )
    }
}
