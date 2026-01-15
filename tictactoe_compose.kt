package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TicTacToeApp()
                }
            }
        }
    }
}

@Composable
fun TicTacToeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF00BCD4),
            background = Color(0xFF121212),
            surface = Color(0xFF1E1E1E),
            onSurface = Color(0xFFE0E0E0)
        ),
        content = content
    )
}

sealed class Screen {
    object Home : Screen()
    data class Game(val isSinglePlayer: Boolean) : Screen()
}

@Composable
fun TicTacToeApp() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var xWins by remember { mutableIntStateOf(0) }
    var oWins by remember { mutableIntStateOf(0) }
    var draws by remember { mutableIntStateOf(0) }

    when (val screen = currentScreen) {
        is Screen.Home -> {
            HomeScreen(
                onSinglePlayer = { currentScreen = Screen.Game(true) },
                onMultiplayer = { currentScreen = Screen.Game(false) }
            )
        }
        is Screen.Game -> {
            GameScreen(
                isSinglePlayer = screen.isSinglePlayer,
                xWins = xWins,
                oWins = oWins,
                draws = draws,
                onGameEnd = { winner ->
                    when (winner) {
                        "X" -> xWins++
                        "O" -> oWins++
                        "Draw" -> draws++
                    }
                },
                onEndGame = {
                    currentScreen = Screen.Home
                    xWins = 0
                    oWins = 0
                    draws = 0
                }
            )
        }
    }
}

@Composable
fun HomeScreen(onSinglePlayer: () -> Unit, onMultiplayer: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(48.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "TIC TAC TOE",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00BCD4)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            FocusableButton(
                text = "Single Player",
                onClick = onSinglePlayer
            )
            
            FocusableButton(
                text = "Multiplayer",
                onClick = onMultiplayer
            )
        }
    }
}

@Composable
fun FocusableButton(text: String, onClick: () -> Unit) {
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(280.dp)
            .height(64.dp)
            .focusRequester(focusRequester)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .then(
                if (isFocused) Modifier.border(3.dp, Color(0xFF00BCD4), RoundedCornerShape(8.dp))
                else Modifier
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun GameScreen(
    isSinglePlayer: Boolean,
    xWins: Int,
    oWins: Int,
    draws: Int,
    onGameEnd: (String) -> Unit,
    onEndGame: () -> Unit
) {
    var board by remember { mutableStateOf(List(9) { "" }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var winner by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var winningLine by remember { mutableStateOf<List<Int>?>(null) }
    
    LaunchedEffect(currentPlayer, board, winner) {
        if (isSinglePlayer && currentPlayer == "O" && winner == null) {
            delay(500)
            val move = getAIMove(board)
            if (move != -1) {
                board = board.toMutableList().also { it[move] = "O" }
                val result = checkWinner(board)
                if (result != null) {
                    winner = result.first
                    winningLine = result.second
                    showDialog = true
                    onGameEnd(result.first)
                } else {
                    currentPlayer = "X"
                }
            }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(48.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Scoreboard
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ScoreText("X: $xWins")
                ScoreText("Draws: $draws")
                ScoreText("O: $oWins")
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Turn indicator
            Text(
                text = if (winner == null) "Turn: $currentPlayer" else "",
                fontSize = 24.sp,
                color = Color(0xFFE0E0E0),
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Game board
            Column(
                modifier = Modifier.aspectRatio(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (row in 0..2) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (col in 0..2) {
                            val index = row * 3 + col
                            GameCell(
                                value = board[index],
                                isWinning = winningLine?.contains(index) == true,
                                onClick = {
                                    if (board[index].isEmpty() && winner == null) {
                                        board = board.toMutableList().also { it[index] = currentPlayer }
                                        val result = checkWinner(board)
                                        if (result != null) {
                                            winner = result.first
                                            winningLine = result.second
                                            showDialog = true
                                            onGameEnd(result.first)
                                        } else {
                                            currentPlayer = if (currentPlayer == "X") "O" else "X"
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
    
    if (showDialog) {
        GameEndDialog(
            winner = winner,
            onPlayAgain = {
                board = List(9) { "" }
                currentPlayer = "X"
                winner = null
                winningLine = null
                showDialog = false
            },
            onEndGame = {
                showDialog = false
                onEndGame()
            }
        )
    }
}

@Composable
fun ScoreText(text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        color = Color(0xFFE0E0E0),
        fontWeight = FontWeight.Light,
        modifier = Modifier.padding(horizontal = 4.dp)
    )
}

@Composable
fun RowScope.GameCell(value: String, isWinning: Boolean, onClick: () -> Unit) {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isWinning) 1.1f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )
    
    Box(
        modifier = Modifier
            .weight(1f)
            .aspectRatio(1f)
            .scale(scale)
            .background(
                color = if (isWinning) Color(0xFF00BCD4).copy(alpha = 0.3f)
                else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .then(
                if (isFocused) Modifier.border(3.dp, Color(0xFF00BCD4), RoundedCornerShape(8.dp))
                else Modifier
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = if (value == "X") Color(0xFF00BCD4) else Color(0xFFFF4081),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun GameEndDialog(winner: String?, onPlayAgain: () -> Unit, onEndGame: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(
                text = when (winner) {
                    "Draw" -> "It's a Draw!"
                    else -> "Player $winner Wins!"
                },
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (winner != "Draw") {
                    Text(
                        text = "🎉",
                        fontSize = 72.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        confirmButton = {
            FocusableButton(
                text = "Play Again",
                onClick = onPlayAgain
            )
        },
        dismissButton = {
            FocusableButton(
                text = "End Game",
                onClick = onEndGame
            )
        }
    )
}

fun checkWinner(board: List<String>): Pair<String, List<Int>>? {
    val winPatterns = listOf(
        listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
        listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
        listOf(0, 4, 8), listOf(2, 4, 6)
    )
    
    for (pattern in winPatterns) {
        val (a, b, c) = pattern
        if (board[a].isNotEmpty() && board[a] == board[b] && board[a] == board[c]) {
            return Pair(board[a], pattern)
        }
    }
    
    if (board.all { it.isNotEmpty() }) {
        return Pair("Draw", emptyList())
    }
    
    return null
}

fun getAIMove(board: List<String>): Int {
    // Check for winning move
    for (i in board.indices) {
        if (board[i].isEmpty()) {
            val testBoard = board.toMutableList().also { it[i] = "O" }
            if (checkWinner(testBoard)?.first == "O") return i
        }
    }
    
    // Block player's winning move
    for (i in board.indices) {
        if (board[i].isEmpty()) {
            val testBoard = board.toMutableList().also { it[i] = "X" }
            if (checkWinner(testBoard)?.first == "X") return i
        }
    }
    
    // Take center if available
    if (board[4].isEmpty()) return 4
    
    // Take corners
    val corners = listOf(0, 2, 6, 8)
    val availableCorners = corners.filter { board[it].isEmpty() }
    if (availableCorners.isNotEmpty()) return availableCorners.random()
    
    // Take any available space
    return board.indices.firstOrNull { board[it].isEmpty() } ?: -1
}