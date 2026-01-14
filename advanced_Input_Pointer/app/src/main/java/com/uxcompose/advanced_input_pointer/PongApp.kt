package com.uxcompose.advanced_input_pointer

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.times
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

private val GameBounds.maxX: Dp
    get() = this.maxWidth

// --- Game Constants ---
const val PADDLE_WIDTH_DP = 12f
const val PADDLE_HEIGHT_DP = 70f
const val BALL_RADIUS_DP = 10f
const val INITIAL_BALL_SPEED_DP = 4f

// Data class to hold the mutable state of the game
data class GameState(
    val ballX: Dp,
    val ballY: Dp,
    val paddle1Y: Dp,
    val paddle2Y: Dp
)

@Composable
fun PongApp() {
    // 1. Apply the Material Theme
    MaterialTheme(
        colorScheme = androidx.compose.material3.darkColorScheme()
    ) {
        // 2. Set the full-screen Surface with the background color
        Surface(modifier = Modifier.fillMaxSize()) {
            PongGameScreen()
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PongGameScreen() {
    // 1. BoxWithConstraints defines the play area boundary
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Constraints gives us the exact size of the play area in Dp
        val bounds = GameBounds(maxWidth, maxHeight)

        // Initialize state variables
        val gameState = remember {
            mutableStateOf(
                GameState(
                    ballX = bounds.midX,
                    ballY = bounds.midY,
                    paddle1Y = bounds.midY,
                    paddle2Y = bounds.midY
                )
            )
        }
        // Get the current density
        val density = LocalDensity.current
        // State for ball velocity (speed and direction)
        val ballVelocity = remember {
            mutableStateOf(BallVelocity(
                x = INITIAL_BALL_SPEED_DP.dp,
                y = INITIAL_BALL_SPEED_DP.dp
            ))
        }

        // 2. Start the game loop
        GameLoop(gameState, ballVelocity, bounds)

        // 3. Draw the game elements
        Ball(gameState.value.ballX, gameState.value.ballY)

        Paddle(
            x = bounds.maxX - PADDLE_WIDTH_DP.dp / 2, // Near right edge
            y = gameState.value.paddle2Y,
            onDrag = { deltaY ->
                // AI paddle logic is simple for this demo, usually it's set in GameLoop
                // For this interactive demo, we'll let the user control both for fun

                // FIX: Apply the same logic as the first paddle
                val deltaInDp = with(density) { deltaY.toDp() }

                gameState.value = gameState.value.copy(
                    paddle2Y = (gameState.value.paddle2Y - deltaInDp).coerceIn( // Use the converted Dp value
                        bounds.minPaddleY,
                        bounds.maxPaddleY
                    )
                )
            }
        )

        // Display Score (simple placeholder)
        Text(
            text = "P1: 0 | P2: 0",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )
    }
}

// Helper class for screen bounds and clamping values
data class GameBounds(val maxWidth: Dp, val maxHeight: Dp) {
    val midX = maxWidth / 2
    val midY = maxHeight / 2

    // Bounds for the ball's center
    val minBallX = BALL_RADIUS_DP.dp
    val maxBallX = maxWidth - BALL_RADIUS_DP.dp
    val minBallY = BALL_RADIUS_DP.dp
    val maxBallY = maxHeight - BALL_RADIUS_DP.dp

    // Bounds for the paddle's center (prevents paddle from going half-off)
    val minPaddleY = PADDLE_HEIGHT_DP.dp / 2
    val maxPaddleY = maxHeight - PADDLE_HEIGHT_DP.dp / 2
}

data class BallVelocity(val x: Dp, val y: Dp)

// --- Game Logic (The loop and bounding enforcement) ---
@Composable
fun GameLoop(
    gameState: MutableState<GameState>,
    ballVelocity: MutableState<BallVelocity>,
    bounds: GameBounds
) {
    // This coroutine runs the animation and game logic continuously
    LaunchedEffect(bounds) {
        // Run at a fixed rate (e.g., 60 FPS)
        while (isActive) {
            delay(16) // 16ms delay = ~60 FPS

            val currentState = gameState.value
            var newBallX = currentState.ballX + ballVelocity.value.x
            var newBallY = currentState.ballY + ballVelocity.value.y
            var newVelocityX = ballVelocity.value.x
            var newVelocityY = ballVelocity.value.y

            // --- 3. Bounding Logic (Top/Bottom Walls) ---
            if (newBallY < bounds.minBallY || newBallY > bounds.maxBallY) {
                // Ball hit top or bottom boundary, reverse vertical velocity
                newVelocityY *= -1
                // Coerce the position back into bounds immediately to prevent tunneling
                newBallY = newBallY.coerceIn(bounds.minBallY, bounds.maxBallY)
            }

            // --- 4. Paddle Collision Logic (Left/Right Sides) ---
            // Left Paddle (Player 1)
            val paddle1X = PADDLE_WIDTH_DP.dp / 2
            val paddle1Top = currentState.paddle1Y - PADDLE_HEIGHT_DP.dp / 2
            val paddle1Bottom = currentState.paddle1Y + PADDLE_HEIGHT_DP.dp / 2

            if (currentState.ballX <= bounds.minBallX && newBallX < currentState.ballX) {
                // Ball is near the left edge, check for collision
                if (currentState.ballY >= paddle1Top && currentState.ballY <= paddle1Bottom) {
                    // Hit! Reverse horizontal velocity
                    newVelocityX *= -1
                } else {
                    // Miss! Goal scored (Reset Ball)
                    newBallX = bounds.midX
                    newBallY = bounds.midY
                    newVelocityX = INITIAL_BALL_SPEED_DP.dp
                    newVelocityY = INITIAL_BALL_SPEED_DP.dp
                    // Score logic would go here
                }
            }

            // Right Paddle (Player 2)
            val paddle2X = bounds.maxX - PADDLE_WIDTH_DP.dp / 2
            val paddle2Top = currentState.paddle2Y - PADDLE_HEIGHT_DP.dp / 2
            val paddle2Bottom = currentState.paddle2Y + PADDLE_HEIGHT_DP.dp / 2

            if (currentState.ballX >= bounds.maxBallX && newBallX > currentState.ballX) {
                // Ball is near the right edge, check for collision
                if (currentState.ballY >= paddle2Top && currentState.ballY <= paddle2Bottom) {
                    // Hit! Reverse horizontal velocity
                    newVelocityX *= -1
                } else {
                    // Miss! Goal scored (Reset Ball)
                    newBallX = bounds.midX
                    newBallY = bounds.midY
                    newVelocityX = -INITIAL_BALL_SPEED_DP.dp // Send ball to the other side
                    newVelocityY = -INITIAL_BALL_SPEED_DP.dp
                    // Score logic would go here
                }
            }

            // --- 5. Apply the new state ---
            ballVelocity.value = BallVelocity(newVelocityX, newVelocityY)
            gameState.value = currentState.copy(
                ballX = newBallX.coerceIn(bounds.minBallX, bounds.maxBallX),
                ballY = newBallY.coerceIn(bounds.minBallY, bounds.maxBallY)
            )
        }
    }
}

// --- Composable Elements ---

@Composable
fun Ball(x: Dp, y: Dp) {
    Canvas(
        modifier = Modifier
            .offset(x, y)
            .size(BALL_RADIUS_DP * 2.dp)
    ) {
        drawCircle(
            color = Color.White,
            radius = BALL_RADIUS_DP.dp.toPx(),
            center = Offset(BALL_RADIUS_DP.dp.toPx(), BALL_RADIUS_DP.dp.toPx())
        )
    }
}

@Composable
fun Paddle(x: Dp, y: Dp, onDrag: (Float) -> Unit) {
    Box(
        modifier = Modifier
            .offset(x = x - PADDLE_WIDTH_DP.dp / 2, y = y - PADDLE_HEIGHT_DP.dp / 2)
            .size(PADDLE_WIDTH_DP.dp, PADDLE_HEIGHT_DP.dp)
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    // delta is in pixels, we pass it up to the game logic
                    onDrag(delta)
                }
            )
            .background(Color.White)
    )
}
