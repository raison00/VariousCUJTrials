package com.uxcompose.pong_input_test_12

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.roundToInt

// --- 1. Main Entry Point & Orientation Locking ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // FOLDABLE NOTE: SENSOR_LANDSCAPE allows the user to flip the phone 180 degrees
        // (holding it by the left or right hand) while keeping the game landscape.
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    PongGameScreen()
                }
            }
        }
    }
}

// --- 2. Game State Management ---
enum class GameState {
    Playing, GameOver
}

// --- 3. Main Game Composable ---
@Composable
fun PongGameScreen() {
    // --- Configuration & State ---
    var boardSize by remember { mutableStateOf(IntSize.Zero) }
    var gameState by remember { mutableStateOf(GameState.Playing) }
    var score by remember { mutableIntStateOf(0) }

    // Paddle Config (The Player)
    var paddleY by remember { mutableFloatStateOf(0f) }
    val paddleHeight = 100.dp
    val paddleWidth = 20.dp
    val paddleX = 50f // Distance from the left edge

    // Density conversions for efficient math loop
    val density = LocalDensity.current
    val paddleHeightPx = with(density) { paddleHeight.toPx() }
    val paddleWidthPx = with(density) { paddleWidth.toPx() }

    // Ball Config
    val ballSize = 25.dp
    val ballSizePx = with(density) { ballSize.toPx() }

    // Ball State (Position & Velocity)
    // FOLDABLE NOTE: Initialize in the middle to prevent instant wall hits on weird aspect ratios
    var ballPos by remember { mutableStateOf(Offset(300f, 300f)) }
    var ballVelocity by remember { mutableStateOf(Offset(-15f, 15f)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            // FOLDABLE NOTE: onSizeChanged handles the dynamic resizing if the user
            // unfolds the device or enters split-screen mode.
            .onSizeChanged { boardSize = it }

            // --- 4. Input Handling (Paddle Movement) ---
            .pointerInput(gameState) {
                if (gameState == GameState.Playing) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            // Get absolute Y position of the touch/mouse
                            val yPos = event.changes.first().position.y

                            // Center the paddle on the finger
                            val targetY = yPos - (paddleHeightPx / 2)

                            // Calculate bounds
                            val maxY = (boardSize.height - paddleHeightPx).coerceAtLeast(0f)

                            // CLAMP: Efficiently restrict paddle to screen
                            paddleY = targetY.coerceIn(0f, maxY)
                        }
                    }
                }
            }
    ) {
        // --- 5. UI: Score Display ---
        // FOLDABLE NOTE: Placed Top-Center. On a foldable with a horizontal crease,
        // you might want to move this to Top-Left to avoid the physical hinge.
        Text(
            text = "$score",
            color = Color.White.copy(alpha = 0.2f),
            fontSize = 100.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )

        // --- 6. The Game Loop ---
        LaunchedEffect(gameState, boardSize) {
            // Only run if playing and we know the screen size
            if (gameState == GameState.Playing && boardSize != IntSize.Zero) {
                while (true) {
                    // A. Calculate potential new position
                    val newX = ballPos.x + ballVelocity.x
                    val newY = ballPos.y + ballVelocity.y

                    // B. Define Hitboxes (AABB Collision)
                    val ballRect = Rect(newX, newY, newX + ballSizePx, newY + ballSizePx)
                    val paddleRect = Rect(paddleX, paddleY, paddleX + paddleWidthPx, paddleY + paddleHeightPx)

                    // C. Collision Logic (Paddle)
                    if (ballRect.overlaps(paddleRect)) {
                        // Only bounce if moving LEFT (towards paddle) to prevent "stuck" balls
                        if (ballVelocity.x < 0) {
                            // Reverse X, and speed up slightly (10%)
                            ballVelocity = ballVelocity.copy(x = abs(ballVelocity.x) * 1.1f)
                            score++
                        }
                    }

                    // D. Wall Logic (Boundaries)
                    val maxBallX = (boardSize.width - ballSizePx).coerceAtLeast(0f)
                    val maxBallY = (boardSize.height - ballSizePx).coerceAtLeast(0f)

                    // Floor / Ceiling Bounce
                    if (newY <= 0f || newY >= maxBallY) {
                        ballVelocity = ballVelocity.copy(y = -ballVelocity.y)
                    }

                    // Right Wall Bounce
                    if (newX >= maxBallX) {
                        ballVelocity = ballVelocity.copy(x = -ballVelocity.x)
                    }

                    // E. Game Over Condition (Left Wall)
                    if (newX <= 0f) {
                        gameState = GameState.GameOver
                    }

                    // F. Apply Movement (Clamped to screen)
                    ballPos = Offset(
                        newX.coerceIn(0f, maxBallX),
                        newY.coerceIn(0f, maxBallY)
                    )

                    // G. Frame Rate (~60 FPS)
                    delay(16L)
                }
            }
        }

        // --- 7. Render Game Objects ---
        if (gameState == GameState.Playing) {
            // Paddle
            Box(
                modifier = Modifier
                    .offset { IntOffset(paddleX.roundToInt(), paddleY.roundToInt()) }
                    .size(paddleWidth, paddleHeight)
                    .background(Color.Cyan)
            )

            // Ball
            Box(
                modifier = Modifier
                    .offset { IntOffset(ballPos.x.roundToInt(), ballPos.y.roundToInt()) }
                    .size(ballSize)
                    .background(Color.Yellow, CircleShape)
            )
        }

        // --- 8. Game Over Overlay ---
        if (gameState == GameState.GameOver) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f))
                    // Capture clicks so they don't pass through to game
                    .clickable(enabled = false) {},
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "GAME OVER",
                        color = Color.Red,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Final Score: $score",
                        color = Color.White,
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = {
                            // Reset Logic
                            score = 0
                            // Reset ball to center
                            ballPos = Offset(boardSize.width / 2f, boardSize.height / 2f)
                            // Reset speed
                            ballVelocity = Offset(-15f, 15f)
                            gameState = GameState.Playing
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan)
                    ) {
                        Text("PLAY AGAIN", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}