package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import android.widget.TextView
import android.widget.RadioGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.Toast
import android.content.Intent
import android.graphics.Color




class MainActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameEditText = findViewById(R.id.nameEditText)
        val startButton = findViewById<Button>(R.id.startButton)

        // When start button is pressed call StartQuiz()
        startButton.setOnClickListener {
            startQuiz()
        }
    }

    // Move to
    private fun startQuiz() {
        val name = nameEditText.text.toString().trim()

        // Check if name is empty
        if (name.isEmpty()) {
            nameEditText.error = "Please enter your name"
            nameEditText.requestFocus()
            return
        }

        // Start the quiz with the entered name
        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtra("name", name)
        startActivity(intent)
    }
}

