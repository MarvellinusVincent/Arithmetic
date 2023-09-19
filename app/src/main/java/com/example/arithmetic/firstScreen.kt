package com.example.arithmetic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.squareup.picasso.Picasso

/**
 * A simple [Fragment] subclass.
 * Use the [firstScreen.newInstance] factory method to
 * create an instance of this fragment.
 */
class firstScreen : Fragment() {
    private lateinit var difficultyRadioGroup: RadioGroup
    private lateinit var operationRadioGroup: RadioGroup
    private lateinit var minusRadioButton: Button
    private lateinit var plusRadioButton: Button
    private lateinit var numQuestionsTextView: TextView
    private var numQuestions = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        /**
         * Inflate the layout for this fragment
         */
        val view = inflater.inflate(R.layout.fragment_first_screen, container, false)

        /**
         * Initialize UI elements
         */
        difficultyRadioGroup = view.findViewById(R.id.Difficulty)
        operationRadioGroup = view.findViewById(R.id.Operation)
        minusRadioButton = view.findViewById(R.id.Minus)
        plusRadioButton = view.findViewById(R.id.Plus)
        numQuestionsTextView = view.findViewById(R.id.textViewCount)

        /**
         * Set default radio button selections
         */
        operationRadioGroup.check(R.id.Addition)
        difficultyRadioGroup.check(R.id.Easy)

        /**
         * Load an image using Picasso library
         */
        val imageView = view.findViewById<ImageView>(R.id.firstPicture)
        val imageUrl = "https://pi.tedcdn.com/r/talkstar-assets.s3.amazonaws.com/production/playlists/playlist_251/hated_math_1200x627.jpg?c=1050%2C550&w=1050"
        Picasso.get()
            .load(imageUrl)
            .into(imageView)

        /**
         * Load correctNumberOfAnswers
         */
        val numCorrectAnswers = try{
            firstScreenArgs.fromBundle(requireArguments()).numCorrectAnswers
        } catch (e: Exception) {
            ""
        }
        val numQuestionsPrevious = try{
            firstScreenArgs.fromBundle(requireArguments()).numQuestionsPrevious
        } catch (e: Exception) {
            ""
        }
        val operandPrevious = try{
            firstScreenArgs.fromBundle(requireArguments()).previousOperation
        } catch (e: Exception) {
            ""
        }
        if (numCorrectAnswers != "" && numQuestionsPrevious != "" && operandPrevious != "") {
            val percentage = numCorrectAnswers.toDouble() / numQuestionsPrevious.toDouble() * 100.0
            val showUser = view.findViewById<TextView>(R.id.totalCorrectTextView)
            if (percentage < 80) {
                showUser.text =
                    "You got $numCorrectAnswers out of $numQuestionsPrevious in $operandPrevious. You need to practice more!"
                showUser.setTextColor(resources.getColor(android.R.color.holo_red_light))
            } else {
                showUser.text =
                    "You got $numCorrectAnswers out of $numQuestionsPrevious in $operandPrevious. Good work!"
                showUser.setTextColor(ContextCompat.getColor(requireContext(), R.color.grayShade))
            }
        }

        /**
         * Define click listeners for buttons
         */
        val startButton = view.findViewById<RadioButton>(R.id.Start)
        minusRadioButton.setOnClickListener {
            if (numQuestions > 0) {
                numQuestions--
                numQuestionsTextView.text = "$numQuestions"
            }
        }
        plusRadioButton.setOnClickListener {
            numQuestions++
            numQuestionsTextView.text = "$numQuestions"
        }

        startButton.setOnClickListener {
            /**
             * Get selected options
             */
            val selectedDifficulty = difficultyRadioGroup.findViewById<RadioButton>(difficultyRadioGroup.checkedRadioButtonId).text.toString()
            val selectedOperation = operationRadioGroup.findViewById<RadioButton>(operationRadioGroup.checkedRadioButtonId).text.toString()
            val numQuestionsChosen = numQuestionsTextView.text.toString().toInt()

            if (numQuestionsChosen > 0) {
                /**
                 * Check if a valid number of questions is chosen
                 * Navigate to the QuestionsScreen fragment with selected options
                 */
                val action = firstScreenDirections.actionFirstScreenToQuestionsScreen(
                    selectedDifficulty,
                    selectedOperation,
                    numQuestionsChosen
                )
                view.findNavController().navigate(action)
            }
        }
        return view
    }
}