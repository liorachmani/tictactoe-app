package com.example.tictactoe

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    enum class Turn {
        CROSS,
        CIRCLE
    }

    companion object {
        const val CROSS = "X"
        const val CIRCLE = "O"
    }

    private var currentTurn = Turn.CROSS
    private lateinit var board: Array<Array<Button>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        initBoard()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initBoard() {
        board = arrayOf(
            arrayOf(
                findViewById(R.id.topLeft),
                findViewById(R.id.topCenter),
                findViewById(R.id.topRight)
            ),
            arrayOf(
                findViewById(R.id.middleLeft),
                findViewById(R.id.middleCenter),
                findViewById(R.id.middleRight)
            ),
            arrayOf(
                findViewById(R.id.bottomLeft),
                findViewById(R.id.bottomCenter),
                findViewById(R.id.bottomRight)
            )
        )

        for (row in board.indices) {
            for (col in board[row].indices) {
                board[row][col].setOnClickListener {
                    onTileClicked(board[row][col])
                }
            }
        }
    }

    private fun onTileClicked(button: Button) {
        addToBoard(button)
        when {
            checkWinner(CROSS) -> declareResult("$CROSS won!")
            checkWinner(CIRCLE) -> declareResult("$CIRCLE won!")
        }
    }

    private fun declareResult(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Game Over")
            .setMessage(message)
            .setPositiveButton("Play again") { _, _ -> resetBoard() }
            .show()
    }

    private fun checkWinner(player: String): Boolean {
        val size = board.size

        // Check rows
        for (i in 0 until size) {
            if (board[i].all { it.text == player }) {
                return true
            }
        }

        // Check columns
        for (i in 0 until size) {
            if (board.all { it[i].text == player }) {
                return true
            }
        }

        // Check main diagonal
        if ((0 until size).all { board[it][it].text == player }) {
            return true
        }

        // Check anti-diagonal
        if ((0 until size).all { board[it][size - 1 - it].text == player }) {
            return true
        }

        return false
    }

    private fun addToBoard(button: Button) {
        if (button.text.isNotEmpty()) {
            return
        }

        button.text = when (currentTurn) {
            Turn.CROSS -> {
                currentTurn = Turn.CIRCLE
                CROSS
            }
            Turn.CIRCLE -> {
                currentTurn = Turn.CROSS
                CIRCLE
            }
        }
    }
}