package com.example.app_kotlin.trivia

import android.content.IntentSender
import androidx.lifecycle.ViewModel




data class Question(
    val id: Int,
    val title: String, // Pregunta
    val options: List<String>, // [Respuesta 1, Respuesta 2...]
    val correctAnswer: Int // 2
)

data class QuizUiState(
    val questions: List<Question> = emptyList(), // Banco de preguntas
    val currentIndex: Int = 0, // Pregunta actual
    val selectedIndex: Int? = null, // Respuesta seleccionada, (null si no hay selección)
    val score: Int = 0, // Puntaje
    val isFinished: Boolean = false // Estado del juego pantalla final


)




