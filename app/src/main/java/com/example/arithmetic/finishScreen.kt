package com.example.arithmetic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.navigation.findNavController

/**
 * A simple [Fragment] subclass.
 * Use the [finishScreen.newInstance] factory method to
 * create an instance of this fragment.
 */
class finishScreen : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /**
         * Inflate the layout for this fragment
         */
        val view = inflater.inflate(R.layout.fragment_finish_screen, container, false)

        /**
         * Retrieve values from the arguments using the generated class finishScreenArgs
         */
        val numQuestions = finishScreenArgs.fromBundle(requireArguments()).numQuestions
        val numCorrectAnswers = finishScreenArgs.fromBundle(requireArguments()).numCorrectAnswers

        /**
         * Find the TextView to display the result
         */
        val showCorrectAnswer = view.findViewById<TextView>(R.id.showUser)
        showCorrectAnswer.text ="$numCorrectAnswers of $numQuestions"

        /**
         * Find the RadioButton for the "Do It Again" button and set its click listener
         * Navigate to the firstScreen using the NavController
         */
        val doItAgainButton = view.findViewById<RadioButton>(R.id.doItAgain)
        doItAgainButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_finishScreen_to_firstScreen)
        }
        return view
    }
}