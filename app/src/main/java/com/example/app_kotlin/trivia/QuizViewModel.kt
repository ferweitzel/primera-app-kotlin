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
        val newLives = if (isCorrect) current.lives else current.lives - 1
        
        val feedbackText = if (isCorrect) {
            "✅ Correcto"
        } else {
            "❌ Incorrecto. Respuesta correcta: ${question.options[question.correctIndex]}"
        }

        _uiState.value = current.copy(
            score = newScore,
            lives = newLives,
            showFeedback = true,
            feedback = feedbackText,
            isFinished = newLives <= 0
        )
    }

    fun onNextQuestion() {
        val current = _uiState.value
        if (current.isFinished) return

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
            Question(1, "¿Qué palabra clave se usa para declarar una variable inmutable?", listOf("var", "val", "let", "const"), 1),
            Question(2, "¿Qué anotación marca una función como UI en Compose?", listOf("@UI", "@Widget", "@Composable", "@Compose"), 2),
            Question(3, "¿Qué componente se usa para listas eficientes?", listOf("Column", "RecyclerView", "Stack", "LazyColumn"), 3),
            Question(4, "¿Cómo se declara una variable que puede ser nula?", listOf("String", "String!", "String?", "String*"), 2),
            Question(5, "¿Qué función se usa para imprimir en consola?", listOf("print()", "println()", "log()", "write()"), 1),
            Question(6, "¿Cuál es el operador para llamadas seguras (null-safety)?", listOf("!!", "?.", "?:", "as?"), 1),
            Question(7, "¿Cómo se define una función de extensión?", listOf("fun String.ext()", "extend fun String", "fun ext(String)", "String::fun"), 0),
            Question(8, "¿Qué palabra clave se usa para clases de datos?", listOf("pojo", "model", "data class", "struct"), 2),
            Question(9, "¿Cómo se representa un rango del 1 al 10?", listOf("1..10", "1 to 10", "1-10", "range(1,10)"), 0),
            Question(10, "¿Qué es un objeto 'Companion'?", listOf("Un constructor", "Un objeto estático", "Una interfaz", "Un Singleton"), 1),
            Question(11, "¿Qué palabra clave se usa para herencia?", listOf("extends", "implements", ":", "inherit"), 2),
            Question(12, "¿Cómo se llama el constructor principal?", listOf("init", "primary", "constructor", "main"), 2),
            Question(13, "¿Qué operador se usa para comparar igualdad estructural?", listOf("=", "==", "===", "equals"), 1),
            Question(14, "¿Cuál es el tipo de retorno por defecto (void)?", listOf("Void", "Nothing", "Unit", "Null"), 2),
            Question(15, "¿Qué palabra clave se usa para constantes en tiempo de compilación?", listOf("val", "static", "const val", "final"), 2)
        )
    }
}
