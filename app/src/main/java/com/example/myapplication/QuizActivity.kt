package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {

    private lateinit var name: String
    private var currentQuestion = 0
    private lateinit var questions: List<Question>

    private lateinit var nameTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var answerRadioGroup: RadioGroup
    private lateinit var submitButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        // If after validating via startQuiz() the name is null, use Anonymous
        name = intent.getStringExtra("name") ?: "Anonymous"


        nameTextView = findViewById(R.id.nameTextView)
        questionTextView = findViewById(R.id.questionTextView)
        answerRadioGroup = findViewById(R.id.answerRadioGroup)
        submitButton = findViewById(R.id.submitButton)
        progressBar = findViewById(R.id.progressBar)

        // Initialize the questions list
        // Question(Question, Options, Correct answer)
        // use .shuffle method to call questions in random order
        questions = listOf(
            Question(
                "What is the capital of France?",
                listOf("Paris", "London", "New York"),
                0
            ),
            Question(
                "What is the largest country in the world by land area?",
                listOf("Russia", "China", "USA"),
                0
            ),
            Question(
                "What is the highest mountain in the world?",
                listOf("Mount Everest", "K2", "Makalu"),
                0
            )
        ).shuffled()

        // Show the first question
        showQuestion(currentQuestion)

        submitButton.setOnClickListener {
            checkAnswer()
        }
    }

    private fun showQuestion(index: Int) {
        val question = questions[index]
        questionTextView.text = question.text
        answerRadioGroup.removeAllViews()
        for ((i, option) in question.options.withIndex()) {
            val radioButton = RadioButton(this)
            radioButton.text = option
            radioButton.id = i
            answerRadioGroup.addView(radioButton)
        }
        progressBar.progress = (index + 1) * 100 / questions.size
    }

    private fun checkAnswer() {
        val selectedOption = answerRadioGroup.checkedRadioButtonId
        // Validate if an answer has been selected
        if (selectedOption == -1) {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
            return
        }

        val question = questions[currentQuestion]
        // Check if answer is correct
        val isCorrect = selectedOption == question.correctAnswer

        // Change highlighting to indicate if true or false
        answerRadioGroup.getChildAt(question.correctAnswer).setBackgroundColor(Color.GREEN)
        if (!isCorrect) {
            answerRadioGroup.getChildAt(selectedOption).setBackgroundColor(Color.RED)
        }
        question.isAnsweredCorrectly = isCorrect
        if (isCorrect) {
            progressBar.progress = (currentQuestion + 1) * 100 / questions.size
        }

        // disable submit button temporarily so user is unable to spam
        submitButton.isEnabled = false

        // Validate if more questions exist, other wise show final result
        Handler(Looper.getMainLooper()).postDelayed({
            currentQuestion++
            if (currentQuestion < questions.size) {
                showQuestion(currentQuestion)
                answerRadioGroup.clearCheck()
                submitButton.isEnabled = true
            } else {
                showFinalScore()
            }
        }, 1000)
    }

    private fun showFinalScore() {
        // call activity_final_score layout
        setContentView(R.layout.activity_final_score)

        val scoreTextView = findViewById<TextView>(R.id.scoreTextView)
        val takeNewQuizButton = findViewById<Button>(R.id.takeNewQuizButton)
        val finishButton = findViewById<Button>(R.id.finishButton)

        // count if AnsweredCorrectly is true
        val numCorrect = questions.count { it.isAnsweredCorrectly }
        val score = numCorrect * 100 / questions.size

        if (score == 0){
            scoreTextView.text = "$name! You scored $score% on the quiz. You might need to do some research"
        } else {
            scoreTextView.text = "Congratulations, $name! You scored $score% on the quiz."
        }

        // Take new Quiz restarts QuizActivity saving name
        takeNewQuizButton.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("name", name)
            startActivity(intent)
            finish()
        }

        // Finish - clean up and return to MainActivity
        finishButton.setOnClickListener {
            finish()
        }
    }
}