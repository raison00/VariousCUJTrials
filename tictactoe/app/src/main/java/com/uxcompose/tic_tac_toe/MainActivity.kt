package com.uxcompose.tic_tac_toe // Change this to your package name

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import androidx.tv.material3.darkColorScheme

// Represents the state of each cell
enum class Player { X, O, EMPTY }

@OptIn(ExperimentalTvMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Apply the TV Material Theme (defaults to dark)
            MaterialTheme(colorScheme = darkColorScheme()) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TicTacToeGame()
                }
            }
        }
    }
}
data class TicTacToeUiState(
    val board: List<Player> = List(9) { Player.EMPTY },
    val currentPlayer: Player = Player.X,
    val winner: Player? = null,
    val isGameOver: Boolean = false
)


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TicTacToeGame() {
    // Single source of truth for the game state
    var uiState by remember { mutableStateOf(TicTacToeUiState()) }

    // Derived status text from the single uiState
    val statusText = remember(uiState.winner, uiState.isGameOver, uiState.currentPlayer) {
      when {
        uiState.winner != null -> "Player ${uiState.winner} Wins!"
        uiState.isGameOver -> "It's a Draw!"
        else -> "Player ${uiState.currentPlayer}'s Turn"
      }
    }

    // Focus Requester to set initial focus
    val focusRequester = remember { FocusRequester() }
    val resetFocusRequester = remember { FocusRequester() }

    // Function to handle a cell click
    fun onCellClick(index: Int) {
        // Ignore clicks if the cell is not empty or the game is over
        if (uiState.board[index] != Player.EMPTY || uiState.isGameOver) return

        // Update board
        val newBoard = uiState.board.toMutableList()
        newBoard[index] = uiState.currentPlayer

        // Check for winner
        val winningLines = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // Rows
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // Columns
            listOf(0, 4, 8), listOf(2, 4, 6) // Diagonals
        )

        val winningLine = winningLines.firstOrNull { (a, b, c) ->
            newBoard[a] != Player.EMPTY && newBoard[a] == newBoard[b] && newBoard[a] == newBoard[c]
        }

        if (winningLine != null) { // We have a winner
            val winner = newBoard[winningLine.first()]
            uiState = uiState.copy(
                board = newBoard,
                winner = winner,
                isGameOver = true
            )
            resetFocusRequester.requestFocus()
        } else if (!newBoard.contains(Player.EMPTY)) { // It's a draw
            uiState = uiState.copy(
                board = newBoard,
                isGameOver = true
            )
            resetFocusRequester.requestFocus()
        } else { // Game continues
            uiState = uiState.copy(
                board = newBoard,
                currentPlayer = if (uiState.currentPlayer == Player.X) Player.O else Player.X
            )
        }
    }

    // Function to reset the game to its initial state
    fun resetGame() {
        uiState = TicTacToeUiState() // Reset to the default state
        focusRequester.requestFocus() // Set focus back to the top-left cell
    }


    // Set initial focus to the top-left cell when the game starts
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    // Main layout: Center everything
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Display Game Status
            Text(
                text = statusText,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Game Board
            GameBoard(
                board = uiState.board,
                onCellClick = ::onCellClick, // Use function reference
                enabled = !uiState.isGameOver,
                initialFocusRequester = focusRequester // Pass the requester for the first cell
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Reset Button
            Button(
                onClick = ::resetGame, // Use function reference
                modifier = Modifier.focusRequester(resetFocusRequester) // Assign focus requester
            ) {
                Text("Reset Game")
            }
        }
    }
}


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun GameBoard(
    board: List<Player>,
    onCellClick: (Int) -> Unit,
    enabled: Boolean,
    initialFocusRequester: FocusRequester // Requester for the top-left cell (index 0)
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Create 3 rows
        (0..2).forEach { rowIndex ->
            Row(horizontalArrangement = Arrangement.Center) {
                // Create 3 cells per row
                (0..2).forEach { colIndex ->
                    val index = rowIndex * 3 + colIndex
                    GameCell(
                        player = board[index],
                        onClick = { onCellClick(index) },
                        enabled = enabled && board[index] == Player.EMPTY,
                        // Apply the initialFocusRequester only to the first cell (index 0)
                        modifier = if (index == 0) Modifier.focusRequester(initialFocusRequester) else Modifier
                    )
                    // Add spacing between cells horizontally
                    if (colIndex < 2) Spacer(modifier = Modifier.width(8.dp))
                }
            }
            // Add spacing between rows vertically
            if (rowIndex < 2) Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun GameCell(
    player: Player,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor = if (isFocused) MaterialTheme.colorScheme.primary else Color.Gray

    Button(
        onClick = onClick,
        modifier = modifier
            .size(100.dp) // Fixed size for each cell
            .aspectRatio(1f) // Ensure it's square
            .focusable(enabled = enabled, interactionSource = interactionSource), // Make it focusable
        enabled = enabled,
        shape = ButtonDefaults.shape(shape = MaterialTheme.shapes.medium),
        border = ButtonDefaults.border(
            border = Border(BorderStroke(2.dp, Color.Gray)), // Default border
            focusedBorder = Border(BorderStroke(4.dp, borderColor)) // Focused border
        ),
        colors = ButtonDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant, // Slightly different bg
            contentColor = when (player) {
                Player.X -> Color.Cyan // Color for X
                Player.O -> Color.Yellow // Color for O
                Player.EMPTY -> MaterialTheme.colorScheme.onSurfaceVariant
            },
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), // Dim if disabled
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        ),
        contentPadding = ButtonDefaults.ContentPadding // Use default padding
    ) {
        if (player != Player.EMPTY) {
            Text(
                text = player.name,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}