// MainActivity.kt
package com.uxcompose.tic_tac_toe_test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
//import androidx.compose.ui.input.key.*
//import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
//import android.media.MediaPlayer
//import android.content.Context
//import android.text.style.LineHeightSpan
//import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.material3.AlertDialog
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Text
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
//import androidx.compose.ui.graphics.Color
//import androidx.tv.material3.ButtonShape
import androidx.tv.material3.darkColorScheme
import androidx.tv.material3.MaterialTheme
import com.uxcompose.tic_tac_toe_test.ui.theme.BoardSurface
import com.uxcompose.tic_tac_toe_test.ui.theme.LightDark
import com.uxcompose.tic_tac_toe_test.ui.theme.Pink80

//import com.uxcompose.tic_tac_toe_test.ui.theme.Purple40

//import kotlin.random.Random

//var selectedIndex: Int = 0

//val color: Unit = Unit

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
//ISSUES: game winning announcement has the buttons not centered properly (it looks like the left padding is not applied to the text, but the button itself)
//ISSUES: Focus traversal indicator is on keyboard only - maybe update this so the selected box is indicated when selected?
//playing with colors and themes, needs to be updated to use the theme; currently in use for quick testing
//TO DO: optimize and create a theme for the app that is complete, currently using arbitrary colors as building
@Composable
fun TicTacToeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = BoardSurface,
            background = Color(0xFF121212),
            surface = LightDark,
            onSurface = Color(0xb4a7d633)
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "INPUT & FOCUS TEST",
                fontSize = 44.sp,
                lineHeight = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00BCD4)
            )

            Text (
                text = "This is a trial first android app developed for UX testing and to gain understanding of how compostables work within the current UI framework",
                fontSize = 24.sp,
                lineHeight = 28.sp,
                color = Color(0xFCFFCF80)
            )

            Text(
                text = "Using Tic Tac Toe for focus traversal testing across devices.",
                fontSize = 30.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF444CC4)
            )
            Spacer(modifier = Modifier.height(24.dp))
//want these to be centered, it's not obvious how to do that
//want to make these clickable by mouse and by touch and by d-pad
            FocusableButton(
                text = "Single Player",
                onClick = onSinglePlayer
            )

            FocusableButton(
                text = "Multiplayer",
                onClick = onMultiplayer
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text (
                text = "The goal is to capture x,y,z. More to come. Needs more focus and input integration",
                fontSize = 24.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFfCfCD4)
            )


        }
    }
}

//make updates to better illustrate focus and input via mouse, keyboard, d-pad, etc.
@Composable
fun FocusableButton(text: String, onClick: () -> Unit) {
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    Button(
        onClick = onClick,

        modifier = Modifier
            .width(250.dp)
            .height(80.dp)
            //.padding(start = 16.dp)
            .focusRequester(focusRequester)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .then(
                if (isFocused) Modifier.border(6.dp, Color(0xdd1eeC94), RoundedCornerShape(48.dp))
                else Modifier
            ),
        colors = ButtonDefaults.colors(
            containerColor = Color(color=0xFF112222),
            contentColor = Color.DarkGray,

        ),
       // shapes = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues( 20.dp
        )
    ) {
        Text(
            text = text,
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 16.dp),
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
            .padding(40.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Number of Ties
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                //color = Color(0xFF00BCD4)
            )

            {

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Scoreboard:",
                    fontSize = 56.sp,
                    modifier = Modifier.padding(PaddingValues(top = 12.dp)),
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFfffCD4)
                )
            }
         


            // Scoreboard
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                ScoreText("X: $xWins")
                ScoreText("O: $oWins")
            }
            Spacer(modifier = Modifier.height(16.dp))
            ScoreText("Draws: $draws")

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "The point of this exercise is to review how the input and focus traversal works across devices and what gaps may be improved, if discovered. This app is expected to work on most android devices and to test the input and focus traversal UI.",
                fontSize = 16.sp,
                modifier = Modifier.padding(PaddingValues(top = 2.dp)),
                lineHeight = 28.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFFfffFF4)
            )
            Spacer(modifier = Modifier.height(24.dp))
            // Turn indicator
            Text(
                text = if (winner == null) "Turn: $currentPlayer" else "",
                fontSize = 64.sp,
                color = Color(0xcc77c0c0),
                fontWeight = FontWeight.Medium
            )
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
            Spacer(modifier = Modifier.height(34.dp))



}

@Composable
fun ScoreText(text: String) {
    Text(
        text = text,
        fontSize = 32.sp,
        color = Color.LightGray,
        fontWeight = FontWeight.Light,
        modifier = Modifier.padding(horizontal = 8.dp)
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
                color = if (isWinning) Color(0xFF44BCD4).copy(alpha = 0.95f)
                else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(24.dp)
            )
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            //this should be a color variable, not hardcoded here- update all uses of this focus indicator color
            .then(
                if (isFocused) Modifier.border(6.dp, Color(0xdd1eeC94), RoundedCornerShape(24.dp))
                else Modifier
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = if (value == "X") Color(0xfff11c90) else Color(0xdc8effa4),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun GameEndDialog(winner: String?, onPlayAgain: () -> Unit, onEndGame: () -> Unit) {
    AlertDialog(
        containerColor = BoardSurface,
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
                color = Pink80,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (winner != "Draw") {
                    Text(
                        text = "🎉 🎉 🎉",
                        fontSize = 72.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        confirmButton = {

            FocusableButton(
                text= "Play Again",
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
