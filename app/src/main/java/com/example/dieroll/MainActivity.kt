package com.example.dieroll

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ViewFlipper
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates

private const val NUMBER_OF_DICE_SIDES = 6
private const val KEY_DISPLAY_CHILD_INDEX = "KEY_DISPLAY_CHILD_INDEX"
private const val KEY_NUMBER_OF_FLIPS_LEFT = "KEY_NUMBER_OF_FLIPS_LEFT"

class MainActivity : AppCompatActivity() {
    private var displayedChildIndex: Int by Delegates.notNull()
    private var numberOfFlipsLeft: Int by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        displayedChildIndex = savedInstanceState?.getInt(KEY_DISPLAY_CHILD_INDEX, 0) ?: 0
        numberOfFlipsLeft = savedInstanceState?.getInt(KEY_NUMBER_OF_FLIPS_LEFT, 0) ?: 0

        val viewFlipper = findViewById<ViewFlipper>(R.id.viewFlipper).apply {
            displayedChild = displayedChildIndex
            addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                if (displayedChildIndex != displayedChild) {
                    displayedChildIndex = displayedChild
                    numberOfFlipsLeft--
                    if (numberOfFlipsLeft == 0) {
                        viewFlipper.stopFlipping()
                        buttonRoll.isEnabled = true
                    }
                }
            }
        }

        if (numberOfFlipsLeft != 0) {
            buttonRoll.isEnabled = false
            viewFlipper.startFlipping()
        }

        buttonRoll.setOnClickListener {
            buttonRoll.isEnabled = false
            numberOfFlipsLeft = determineNumberOfFlips()
            viewFlipper.startFlipping()
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState!!.run {
            putInt(KEY_DISPLAY_CHILD_INDEX, displayedChildIndex)
            putInt(KEY_NUMBER_OF_FLIPS_LEFT, numberOfFlipsLeft)
        }
        super.onSaveInstanceState(savedInstanceState)
    }

    private fun determineNumberOfFlips(): Int =
        NUMBER_OF_DICE_SIDES + (1..NUMBER_OF_DICE_SIDES).random()
}
