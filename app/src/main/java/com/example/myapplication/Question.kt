package com.example.myapplication
// Question(Question, Options, Correct answer)
// isAnsweredCorrectly changes to true via checkAnswer()
class Question(val text: String, val options: List<String>, val correctAnswer: Int) {
    var isAnsweredCorrectly = false
}