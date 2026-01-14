package com.uxcompose.ponggame

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
//import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.isActive
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF1E1E1E)
                ) {
                    PongGame()
                }
            }
        }
    }
}

data class Ball(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val radius: Float = 12f
)

data class Paddle(
    var x: Float,
    var y: Float,
    val width: Float = 100f,
    val height: Float = 20f
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PongGame() {
    var playAreaSize by remember { mutableStateOf(IntSize.Zero) }
    var ball by remember { mutableStateOf(Ball(0f, 0f, 5f, 5f)) }
    var playerPaddle by remember { mutableStateOf(Paddle(0f, 0f)) }
    var aiPaddle by remember { mutableStateOf(Paddle(0f, 0f)) }
    var score by remember { mutableStateOf(0 to 0) }

    // Input state
    var dpadLeftPressed by remember { mutableStateOf(false) }
    var dpadRightPressed by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    // Initialize positions when play area size is known
    LaunchedEffect(playAreaSize) {
        if (playAreaSize.width > 0 && playAreaSize.height > 0) {
            ball = Ball(
                x = playAreaSize.width / 2f,
                y = playAreaSize.height / 2f,
                vx = 5f,
                vy = 5f
            )
            playerPaddle = Paddle(
                x = playAreaSize.width / 2f - 50f,
                y = playAreaSize.height - 40f
            )
            aiPaddle = Paddle(
                x = playAreaSize.width / 2f - 50f,
                y = 20f
            )
        }
    }

    // Request focus for D-pad input
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    // Game loop
    LaunchedEffect(playAreaSize) {
        if (playAreaSize.width == 0 || playAreaSize.height == 0) return@LaunchedEffect

        while (isActive) {
            withFrameMillis {
                // Handle D-pad movement
                if (dpadLeftPressed) {
                    playerPaddle.x = (playerPaddle.x - 8f).coerceAtLeast(0f)
                }
                if (dpadRightPressed) {
                    playerPaddle.x = (playerPaddle.x + 8f).coerceAtMost(playAreaSize.width - playerPaddle.width)
                }

                // Update ball position
                ball.x += ball.vx
                ball.y += ball.vy

                // Boundary collision - left and right walls
                if (ball.x - ball.radius <= 0 || ball.x + ball.radius >= playAreaSize.width) {
                    ball.vx = -ball.vx
                    ball.x = ball.x.coerceIn(ball.radius, playAreaSize.width - ball.radius)
                }

                // Top boundary - AI misses
                if (ball.y - ball.radius <= 0) {
                    score = score.copy(first = score.first + 1)
                    ball = Ball(
                        x = playAreaSize.width / 2f,
                        y = playAreaSize.height / 2f,
                        vx = 5f,
                        vy = 5f
                    )
                }

                // Bottom boundary - Player misses
                if (ball.y + ball.radius >= playAreaSize.height) {
                    score = score.copy(second = score.second + 1)
                    ball = Ball(
                        x = playAreaSize.width / 2f,
                        y = playAreaSize.height / 2f,
                        vx = 5f,
                        vy = -5f
                    )
                }

                // Paddle collision - Player
                if (ball.y + ball.radius >= playerPaddle.y &&
                    ball.y - ball.radius <= playerPaddle.y + playerPaddle.height &&
                    ball.x >= playerPaddle.x &&
                    ball.x <= playerPaddle.x + playerPaddle.width
                ) {
                    ball.vy = -abs(ball.vy)
                    val hitPos = (ball.x - playerPaddle.x) / playerPaddle.width
                    ball.vx = (hitPos - 0.5f) * 10f
                }

                // Paddle collision - AI
                if (ball.y - ball.radius <= aiPaddle.y + aiPaddle.height &&
                    ball.y + ball.radius >= aiPaddle.y &&
                    ball.x >= aiPaddle.x &&
                    ball.x <= aiPaddle.x + aiPaddle.width
                ) {
                    ball.vy = abs(ball.vy)
                    val hitPos = (ball.x - aiPaddle.x) / aiPaddle.width
                    ball.vx = (hitPos - 0.5f) * 10f
                }

                // AI movement
                val aiCenter = aiPaddle.x + aiPaddle.width / 2f
                if (ball.x < aiCenter - 5f) {
                    aiPaddle.x = (aiPaddle.x - 4f).coerceAtLeast(0f)
                } else if (ball.x > aiCenter + 5f) {
                    aiPaddle.x = (aiPaddle.x + 4f).coerceAtMost(playAreaSize.width - aiPaddle.width)
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { size ->
                    playAreaSize = size
                }
                .focusRequester(focusRequester)
                .focusable()
                // D-pad / Keyboard input
                .onKeyEvent { keyEvent ->
                    when (keyEvent.nativeKeyEvent.keyCode) {
                        KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_A -> {
                            dpadLeftPressed = keyEvent.nativeKeyEvent.action == KeyEvent.ACTION_DOWN
                            true
                        }
                        KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_D -> {
                            dpadRightPressed = keyEvent.nativeKeyEvent.action == KeyEvent.ACTION_DOWN
                            true
                        }
                        else -> false
                    }
                }
                // Touch/Stylus drag input
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        playerPaddle.x = (playerPaddle.x + dragAmount.x)
                            .coerceIn(0f, playAreaSize.width - playerPaddle.width)
                    }
                }
                // Mouse movement and hover input
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            when (event.type) {
                                PointerEventType.Move, PointerEventType.Enter -> {
                                    val position = event.changes.first().position
                                    playerPaddle.x = (position.x - playerPaddle.width / 2f)
                                        .coerceIn(0f, playAreaSize.width - playerPaddle.width)
                                }
                            }
                        }
                    }
                }
        ) {
            // Draw center line
            drawLine(
                color = Color.White.copy(alpha = 0.3f),
                start = Offset(0f, size.height / 2f),
                end = Offset(size.width, size.height / 2f),
                strokeWidth = 2f
            )

            // Draw ball
            drawCircle(
                color = Color.White,
                radius = ball.radius,
                center = Offset(ball.x, ball.y)
            )

            // Draw player paddle
            drawRect(
                color = Color.Green,
                topLeft = Offset(playerPaddle.x, playerPaddle.y),
                size = Size(playerPaddle.width, playerPaddle.height)
            )

            // Draw AI paddle
            drawRect(
                color = Color.Red,
                topLeft = Offset(aiPaddle.x, aiPaddle.y),
                size = Size(aiPaddle.width, aiPaddle.height)
            )
        }

        // Score display
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "AI: ${score.second}",
                color = Color.Red,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "You: ${score.first}",
                color = Color.Green,
                fontSize = 24.sp
            )
        }

        // Control instructions
        Text(
            text = "Touch/Stylus: Drag | Mouse: Move | D-pad/Keyboard: ← → or A D",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}