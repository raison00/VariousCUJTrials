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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import android.media.MediaPlayer
import android.content.Context
//import android.text.style.LineHeightSpan
import androidx.compose.ui.graphics.graphicsLayer
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.tv.material3.darkColorScheme
import androidx.tv.material3.MaterialTheme
import kotlin.random.Random

//var selectedIndex: Int = 0

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme {
                TicTacToeGame()
            }
        }
    }
}
@Composable
fun TicTacToeTheme(content: @Composable () -> Unit) {
    val colorScheme = darkColorScheme(
        primary = Color(0xFF9C27B0),
        secondary = Color(0xFFE91E63),
        background = Color(0xFF121212),
        surface = Color(0xFF121212),
        onPrimary = Color.Cyan,
        onSecondary = Color.Magenta,
        onBackground = Color.Yellow,
        onSurface = Color.White
    )

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

enum class GameMode { MENU, SINGLE_PLAYER, MULTIPLAYER }
enum class Player { X, O, NONE }

data class GameState(
    val board: List<Player> = List(9) { Player.NONE },
    val currentPlayer: Player = Player.X,
    val winner: Player? = null,
    val winningLine: List<Int>? = null,
    val xScore: Int = 0,
    val oScore: Int = 0,
    val draws: Int = 0
)

@Composable
fun TicTacToeGame() {
    var gameMode by remember { mutableStateOf(GameMode.MENU) }
    var gameState by remember { mutableStateOf(GameState()) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (gameMode) {
            GameMode.MENU -> MainMenu(
                onSinglePlayer = { gameMode = GameMode.SINGLE_PLAYER },
                onMultiplayer = { gameMode = GameMode.MULTIPLAYER }
            )
            GameMode.SINGLE_PLAYER, GameMode.MULTIPLAYER -> GameScreen(
                gameState = gameState,
                gameMode = gameMode,
                onMove = { index ->
                    if (gameState.board[index] == Player.NONE && gameState.winner == null) {
                        playSound(context, R.raw.move)
                        //vibrate(context)
                        val newBoard = gameState.board.toMutableList()
                        newBoard[index] = gameState.currentPlayer
                        val (winner, line) = checkWinner(newBoard)

                        gameState = gameState.copy(
                            board = newBoard,
                            currentPlayer = if (gameState.currentPlayer == Player.X) Player.O else Player.X,
                            winner = winner,
                            winningLine = line
                        )

                        if (winner != null) {
                            playSound(context, R.raw.win)
                            gameState = when (winner) {
                                Player.X -> gameState.copy(xScore = gameState.xScore + 1)
                                Player.O -> gameState.copy(oScore = gameState.oScore + 1)
                                else -> gameState.copy(draws = gameState.draws + 1)
                            }
                        }
                    }
                },
                onPlayAgain = {
                    gameState = gameState.copy(
                        board = List(9) { Player.NONE },
                        currentPlayer = Player.X,
                        winner = null,
                        winningLine = null
                    )
                },
                onEndGame = {
                    gameMode = GameMode.MENU
                    gameState = GameState()
                },
                //context = context
            )
        }
    }
}

@Composable
fun MainMenu(onSinglePlayer: () -> Unit, onMultiplayer: () -> Unit) {
    val focusRequesters = remember { List(2) { FocusRequester() } }

    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "TIC TAC TOE",
            fontSize = 80.sp,
            lineHeight = 80.sp,
            fontWeight = FontWeight.Bold,
            //add a different font?
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(64.dp))

        MenuButton(
            text = "Single Player",
            focusRequester = focusRequesters[0],
            onClick = onSinglePlayer,
            onNavigate = { direction ->
                if (direction == 1) focusRequesters[1].requestFocus()
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        MenuButton(
            text = "Multiplayer",
            focusRequester = focusRequesters[1],
            onClick = onMultiplayer,
            onNavigate = { direction ->
                if (direction == -1) focusRequesters[0].requestFocus()
            }
        )
    }
}

@Composable
fun MenuButton(
    text: String,
    focusRequester: FocusRequester,
    onClick: () -> Unit,
    onNavigate: (Int) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isFocused) 1.1f else 1f)

    Button(
        onClick = onClick,
        modifier = Modifier
            .width(100.dp)
            .height(80.dp)
            .padding(all = 16.dp)
            .scale(scale)
            .focusRequester(focusRequester)
            .onFocusChanged {
                isFocused = it.isFocused
            }
            .focusable()
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown) {
                    when (event.key) {
                        Key.DirectionDown -> {
                            onNavigate(1)
                            true
                        }

                        Key.DirectionUp -> {
                            onNavigate(-1)
                            true
                        }

                        Key.Enter, Key.DirectionCenter -> {
                            onClick()
                            true
                        }

                        else -> false
                    }
                } else false
            },
        colors = ButtonDefaults.colors(    containerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(text, fontSize = 32.sp,  fontWeight = FontWeight.Bold)
    }
}



@Composable
fun GameScreen(
    gameState: GameState,
    gameMode: GameMode,
    onMove: (Int) -> Unit,
    onPlayAgain: () -> Unit,
    onEndGame: () -> Unit,
    //
) {
    val focusRequesters = remember { List(9) { FocusRequester() } }
    //val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        focusRequesters[4].requestFocus()
    }

    // AI Move for Single Player
    LaunchedEffect(gameState.currentPlayer, gameState.winner) {
        if (gameMode == GameMode.SINGLE_PLAYER &&
            gameState.currentPlayer == Player.O &&
            gameState.winner == null) {
            delay(500)
            val aiMove = getAIMove(gameState.board)
            if (aiMove != -1) {
                onMove(aiMove)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Score Board
        ScoreBoard(gameState)

        // Game Board
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (gameState.winner == null) {
                Text(
                    text = "${if (gameState.currentPlayer == Player.X) "Purple X" else "Pink O"}'s Turn",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (gameState.currentPlayer == Player.X)
                        Color(0xFF9C27B0) else Color(0xFFE91E63)
                )
                Spacer(modifier = Modifier.height(24.dp) .padding(start = 16.dp))
            }

            Board(
                gameState = gameState,
                focusRequesters = focusRequesters,
                onMove = onMove
            )
        }

        // Game Over Screen
        // Game Over Screen
        if (gameState.winner != null) {
            GameOverDialog(
                winner = gameState.winner,
                onPlayAgain = onPlayAgain,
                onEndGame = onEndGame
            )

        }
    }
}

@Composable
fun ScoreBoard(gameState: GameState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ScoreCard("Purple X", gameState.xScore, Color(0xFF9C27B0))
        ScoreCard("Draws", gameState.draws, Color.Gray)
        ScoreCard("Pink O", gameState.oScore, Color(0xFFE91E63))
    }
}

@Composable
fun ScoreCard(label: String, score: Int, color: Color) {
    Card(
        modifier = Modifier.width(200.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, fontSize = 20.sp, color = color, fontWeight = FontWeight.Bold)
            Text(score.toString(), fontSize = 32.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun Board(
    gameState: GameState,
    focusRequesters: List<FocusRequester>,
    onMove: (Int) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (row in 0..2) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (col in 0..2) {
                    val index = row * 3 + col
                    Cell(
                        player = gameState.board[index],
                        focusRequester = focusRequesters[index],
                        isWinningCell = gameState.winningLine?.contains(index) == true,
                        onClick = { onMove(index) },
                        onNavigate = { direction ->
                            when (direction) {
                                Key.DirectionUp -> if (row > 0) focusRequesters[index - 3].requestFocus()
                                Key.DirectionDown -> if (row < 2) focusRequesters[index + 3].requestFocus()
                                Key.DirectionLeft -> if (col > 0) focusRequesters[index - 1].requestFocus()
                                Key.DirectionRight -> if (col < 2) focusRequesters[index + 1].requestFocus()
                            }
                        }
                    )
                }
            }
        }
    }
}



@Composable
fun Cell(
    player: Player,
    focusRequester: FocusRequester,
    isWinningCell: Boolean,
    onClick: () -> Unit,
    onNavigate: (Key) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isFocused) 1.05f else 1f)
    val animationType = remember { Random.nextInt(3) }

    val rotation by animateFloatAsState(
        targetValue = if (isWinningCell && animationType == 0) 360f else 0f,
        animationSpec = tween(1000)
    )

    val pulseScale by animateFloatAsState(
        targetValue = if (isWinningCell && animationType == 1) 1.2f else 1f,
        animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse)
    )

    val alpha by animateFloatAsState(
        targetValue = if (isWinningCell && animationType == 2) 0.3f else 1f,
        animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse)
    )

    Box(
        modifier = Modifier
            .size(140.dp)
            .scale(scale * if (isWinningCell) pulseScale else 1f)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = if (isFocused) 4.dp else 2.dp,
                color = if (isFocused) Color.White else Color.Gray,
                shape = RoundedCornerShape(12.dp)
            )
            .focusRequester(focusRequester)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown) {
                    when (event.key) {
                        Key.DirectionUp, Key.DirectionDown,
                        Key.DirectionLeft, Key.DirectionRight -> {
                            onNavigate(event.key)
                            true
                        }

                        Key.Enter, Key.DirectionCenter -> {
                            onClick()
                            true
                        }

                        else -> false
                    }
                } else false
            }
            .graphicsLayer {
                rotationZ = rotation
                this.alpha = alpha
            },
        contentAlignment = Alignment.Center
    ) {
        when (player) {
            Player.X -> Text(
                "X",
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF9C27B0)
            )
            Player.O -> Text(
                "O",
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE91E63)
            )
            Player.NONE -> {}
        }
    }
}

@Composable
fun GameOverDialog(
    winner: Player,
    onPlayAgain: () -> Unit,
    onEndGame: () -> Unit
) {
    val focusRequesters = remember { List(2) { FocusRequester() } }

    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(24.dp)
    ) {
        Text(
            text = when (winner) {
                Player.X -> "Purple X Wins!"
                Player.O -> "Pink O Wins!"
                Player.NONE -> "It's a Draw!"
            },
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = when (winner) {
                Player.X -> Color(0xFF9C27B0)
                Player.O -> Color(0xFFE91E63)
                Player.NONE -> Color.Gray
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            GameOverButton(
                text = "Play Again",
                focusRequester = focusRequesters[0],
                onClick = onPlayAgain,
                onNavigate = { if (it == 1) focusRequesters[1].requestFocus() }
            )

            GameOverButton(
                text = "End Game",
                focusRequester = focusRequesters[1],
                onClick = onEndGame,
                onNavigate = { if (it == -1) focusRequesters[0].requestFocus() }
            )
        }
    }
}

@Composable
fun GameOverButton(
    text: String,
    focusRequester: FocusRequester,
    onClick: () -> Unit,
    onNavigate: (Int) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isFocused) 1.1f else 1f)

    Button(
        onClick = onClick,
        modifier = Modifier
            .width(240.dp)
            .height(80.dp)
            .scale(scale)
            .focusRequester(focusRequester)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown) {
                    when (event.key) {
                        Key.DirectionLeft -> {
                            onNavigate(-1)
                            true
                        }

                        Key.DirectionRight -> {
                            onNavigate(1)
                            true
                        }

                        Key.Enter, Key.DirectionCenter -> {
                            onClick()
                            true
                        }

                        else -> false
                    }
                } else false
            },
        colors = ButtonDefaults.colors(
            containerColor = if (isFocused) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Text(text, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}

fun checkWinner(board: List<Player>): Pair<Player?, List<Int>?> {
    val lines = listOf(
        listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
        listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
        listOf(0, 4, 8), listOf(2, 4, 6)
    )

    for (line in lines) {
        val (a, b, c) = line
        if (board[a] != Player.NONE && board[a] == board[b] && board[a] == board[c]) {
            return Pair(board[a], line)
        }
    }

    return if (board.none { it == Player.NONE }) {
        Pair(Player.NONE, null)
    } else {
        Pair(null, null)
    }
}

fun getAIMove(board: List<Player>): Int {
    // Try to win
    for (i in board.indices) {
        if (board[i] == Player.NONE) {
            val testBoard = board.toMutableList()
            testBoard[i] = Player.O
            if (checkWinner(testBoard).first == Player.O) return i
        }
    }

    // Block player from winning
    for (i in board.indices) {
        if (board[i] == Player.NONE) {
            val testBoard = board.toMutableList()
            testBoard[i] = Player.X
            if (checkWinner(testBoard).first == Player.X) return i
        }
    }

    // Take center
    if (board[4] == Player.NONE) return 4

    // Take corner
    val corners = listOf(0, 2, 6, 8)
    val availableCorners = corners.filter { board[it] == Player.NONE }
    if (availableCorners.isNotEmpty()) return availableCorners.random()

    // Take any available
    return board.indices.filter { board[it] == Player.NONE }.randomOrNull() ?: -1
}

fun playSound(context: Context, soundRes: Int) {
    try {
        val mediaPlayer = MediaPlayer.create(context, soundRes)
        mediaPlayer?.apply {
            setOnCompletionListener { release() }
            start()
        }
    } catch (_: Exception) {
        // Sound not available
    }
}
