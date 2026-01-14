package com.uxcompose.tic_tac_toe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// Data class to hold all game state
data class GameState(
    val board: List<Player> = List(9) { Player.EMPTY },
    val currentPlayer: Player = Player.X,
    val winner: Player? = null,
    val isDraw: Boolean = false
) {
    val isGameOver: Boolean
        get() = winner != null || isDraw

    val statusText: String
        get() = when {
            winner != null -> "Player ${winner.name} Wins!"
            isDraw -> "It's a Draw!"
            else -> "Player ${currentPlayer.name}'s Turn"
        }
}

class TicTacToeViewModel : ViewModel() {
    var gameState by mutableStateOf(GameState())
        private set

    fun onCellClick(index: Int) {
        // Prevent action if the cell is taken or the game is over
        if (gameState.board[index] != Player.EMPTY || gameState.isGameOver) {
            return
        }

        val newBoard = gameState.board.toMutableList()
        newBoard[index] = gameState.currentPlayer
        gameState = gameState.copy(board = newBoard)

        checkGameStatus()
    }

    private fun checkGameStatus() {
        val winner = calculateWinner(gameState.board)
        val isBoardFull = gameState.board.none { it == Player.EMPTY }

        if (winner != null) {
            gameState = gameState.copy(winner = winner)
        } else if (isBoardFull) {
            gameState = gameState.copy(isDraw = true)
        } else {
            // Switch player only if the game is still ongoing
            val nextPlayer = if (gameState.currentPlayer == Player.X) Player.O else Player.X
            gameState = gameState.copy(currentPlayer = nextPlayer)
        }
    }

    fun resetGame() {
        gameState = GameState()
    }

    private fun calculateWinner(board: List<Player>): Player? {
        val winningLines = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // Rows
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // Columns
            listOf(0, 4, 8), listOf(2, 4, 6)             // Diagonals
        )

        for (line in winningLines) {
            val (a, b, c) = line
            if (board[a] != Player.EMPTY && board[a] == board[b] && board[a] == board[c]) {
                return board[a] // Return the winning player
            }
        }
        return null // No winner
    }
}