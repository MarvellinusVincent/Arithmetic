package com.example.arithmetic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.navigation.findNavController
import com.squareup.picasso.Picasso
import kotlin.random.Random
import android.widget.Toast
import android.media.MediaPlayer

/**
 * A simple [Fragment] subclass.
 * Use the [questionsScreen.newInstance] factory method to
 * create an instance of this fragment.
 */
class questionsScreen : Fragment() {
    private var currentQuestion: Int = 1
    private lateinit var firstNum : TextView
    private lateinit var operand : TextView
    private lateinit var secondNum : TextView
    private var numCorrectAnswers: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /**
         * Inflate the layout for this fragment
         */
        val view = inflater.inflate(R.layout.fragment_questions_screen, container, false)

        /**
         * Load an image using Picasso library
         */
        val imageView = view.findViewById<ImageView>(R.id.secondPicture)
        val imageUrl = "https://pi.tedcdn.com/r/talkstar-assets.s3.amazonaws.com/production/playlists/playlist_251/hated_math_1200x627.jpg?c=1050%2C550&w=1050"
        Picasso.get()
            .load(imageUrl)
            .into(imageView)

        val correctSound = MediaPlayer.create(context, R.raw.correct)
        val wrongSound = MediaPlayer.create(context, R.raw.wrong)


        /**
         * Retrieve arguments from the navigation
         */
        val difficulty = questionsScreenArgs.fromBundle(requireArguments()).difficulty
        val operation = questionsScreenArgs.fromBundle(requireArguments()).operation
        val numQuestions = questionsScreenArgs.fromBundle(requireArguments()).numQuestions

        /**
         * Initialize UI elements
         */
        val userAnswer = view.findViewById<EditText>(R.id.UserText)
        firstNum = view.findViewById(R.id.firstNumber)
        operand = view.findViewById(R.id.Operation)
        secondNum = view.findViewById(R.id.secondNumber)

        /**
         * Generate and display the first question
         */
        if (numQuestions > 0) {
            firstNum.text = getRandomNumber(difficulty)
            operand.text = getOperation(operation)
            secondNum.text = getRandomNumber(difficulty)
        }

        /**
         * Define click listener for the "Done" button
         */
        val doneButton = view.findViewById<RadioButton>(R.id.Done)
        doneButton.setOnClickListener {
            /**
             * Check if we should move to the next page and also check if the user inputs anything
             */
            if (currentQuestion < numQuestions && userAnswer.text.toString().trim().isNotEmpty()) {
                val firstNumText = firstNum.text.toString()
                val operandText = operand.text.toString()
                val secondNumText = secondNum.text.toString()
                val correctAns = correctAnswer(firstNumText, operandText, secondNumText)
                val userAnswerText = userAnswer.text.toString().trim().toInt()
                if (correctAns == userAnswerText) {
                    correctSound.start()
                    showToast("Correct. Good work!")
                    numCorrectAnswers ++
                } else {
                    wrongSound.start()
                    showToast("Wrong")
                }

                /**
                 * Generate and display the next question
                 */
                firstNum.text = getRandomNumber(difficulty)
                operand.text = getOperation(operation)
                secondNum.text = getRandomNumber(difficulty)
                currentQuestion ++
            } else if (currentQuestion >= numQuestions && userAnswer.text.toString().trim().isNotEmpty()) {
                val firstNumText = firstNum.text.toString()
                val operandText = operand.text.toString()
                val secondNumText = secondNum.text.toString()
                val correctAns = correctAnswer(firstNumText, operandText, secondNumText)
                val userAnswerText = userAnswer.text.toString().trim().toInt()
                if (correctAns == userAnswerText) {
                    correctSound.start()
                    showToast("Correct. Good work!")
                    numCorrectAnswers ++
                } else {
                    wrongSound.start()
                    showToast("Wrong")
                }

                /**
                 * Navigate to the first screen with the results
                 */
                val action = questionsScreenDirections.actionQuestionsScreenToFirstScreen(numCorrectAnswers.toString(), numQuestions.toString(), operation)
                view.findNavController().navigate(action)
            }
        }
        return view
    }

    /**
     * Function for getting a random number based on the difficulty
     */
    private fun getRandomNumber(difficulty: String): String {
        val randomNumber = when(difficulty) {
            "Easy" -> Random.nextInt(11)
            "Medium" -> Random.nextInt(26)
            "Hard" -> Random.nextInt(51)
            else -> 0
        }
        return randomNumber.toString()
    }

    /**
     * Function for getting the operation and converting it to a text
     */
    private fun getOperation(operation: String): String {
        val operand = when(operation) {
            "Addition" -> "+"
            "Subtraction" -> "-"
            "Multiplication" -> "X"
            "Division" -> "/"
            else -> "+"
        }
        return operand
    }

    /**
     * Function for calculating the correct answer
     */
    private fun correctAnswer(firstNumber: String, operation: String, secondNumber: String): Int {
        val num1 = firstNumber.toInt()
        val num2 = secondNumber.toInt()
        return when (operation) {
            "+" -> num1 + num2
            "-" -> num1 - num2
            "X" -> num1 * num2
            "/" -> num1 / num2
            else -> 0
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}