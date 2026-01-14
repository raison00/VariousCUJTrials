package com.uxcompose.large_screen_optimized

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import kotlinx.coroutines.delay
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.materialIcon


/*
data class MenuItem(
    val id: Int,
    val title: String,
    val icon: ImageVector,
    val backgroundColor: Color
)
*/

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContextMenuTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ContextMenuScreen()
                }
            }
        }
    }
}



// Theme ? update to M3 - issue with m3 api initially
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF9333EA),
    secondary = Color(0xFF3B82F6),
    tertiary = Color(0xFF10B981),
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B)
)


@Composable
fun ContextMenuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

// Data Model
data class MenuItem(
    val id: Int,
    val label: String,
    val icon: ImageVector,
    val color: Color
)

// Main Screen
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContextMenuScreen() {
    var showContextMenu by remember { mutableStateOf(false) }
    var contextMenuOffset by remember { mutableStateOf(DpOffset.Zero) }
    var selectedItem by remember { mutableStateOf<MenuItem?>(null) }
    var contentSize by remember { mutableStateOf(IntSize.Zero) }

    val density = LocalDensity.current

    val menuItems = listOf(
        // CORRECTED: Added Icons.Filled for extended icons, not Icons.Default sysui v compose
        MenuItem(1, "Open Link", Icons.Filled.Link, Color(0xFF2196F3)),
        MenuItem(2, "Share Document", Icons.Filled.Description, Color(0xFF4CAF50)),
        MenuItem(3, "View Image", Icons.Filled.Image, Color(0xFF9C27B0)),
        MenuItem(4, "Play Video", Icons.Default.PlayArrow, Color(0xFFF44336)), // PlayArrow is in Default
        MenuItem(5, "Play Audio", Icons.Filled.MusicNote, Color(0xFFFF9800))
    )

    LaunchedEffect(selectedItem) {
        if (selectedItem != null) {
            delay(2000)
            selectedItem = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF0F172A),
                        Color(0xFF581C87),
                        Color(0xFF0F172A)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .onSizeChanged { contentSize = it },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xCC1E293B)
                )
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF9333EA),
                                    Color(0xFF3B82F6)
                                )
                            )
                        )
                        .padding(24.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.TouchApp,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                        Column {
                            Text(
                                text = "Context Menu Triggered by ----",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Android 16 • Jetpack Compose 1.10.0 • Material3 for Compose 1.4.0",
                                fontSize = 14.sp,
                                color = Color(0xFFE9D5FF)
                            )
                        }
                    }
                }

                // Interactive Content Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 400.dp)
                            .combinedClickable(
                                onClick = { showContextMenu = false },
                                onLongClick = {
                                    showContextMenu = true
                                    contextMenuOffset = DpOffset(
                                        x = with(density) { (contentSize.width / 2).toDp() },
                                        y = with(density) { (contentSize.height / 2).toDp() }
                                    )
                                }
                            )

                            /* POINTER INPUT */
                            .pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val event = awaitPointerEvent()
                                        if (event.type == PointerEventType.Press) {
                                            val position = event.changes.first().position
                                            if (event.buttons.isSecondaryPressed) {
                                                showContextMenu = true
                                                contextMenuOffset = DpOffset(
                                                    x = with(density) { position.x.toDp() },
                                                    y = with(density) { position.y.toDp() }
                                                )
                                            }
                                        }
                                    }
                                }
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0x80334155)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFF9333EA),
                                                Color(0xFF3B82F6)
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.TouchApp,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(80.dp)
                                )
                                Spacer(modifier = Modifier.width(148.dp))

                            }


                            Spacer(modifier = Modifier.height(34.dp))

                            Text(
                                text = "Context Menu Testing Area",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            Text(
                                text = "Long-press (touch) or right-click (mouse) or long-press on a trackpad or a D-pad to access the same contextual menu.",
                                fontSize = 14.sp,
                                color = Color(0xFFCBD5E1),
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Expectations: A contextual menu will appear when these conditions are met:  Long-press on a touchscreen, right-click on a mouse, or click and hold on a trackpad.",
                                fontSize = 14.sp,
                                color = Color(0xFFCBD5E1),
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(20.dp),
                                modifier = Modifier.padding(horizontal = 4.dp)
                            ) {
                                Chip(text = "Touch", color = Color(0xFF9999EA))
                                Chip(text = "Mouse", color = Color(0xFF3B82F6))
                                Chip(text = "Track", color = Color(0xFF9cc9EA))
                                //pointer
                                Chip(text = "D-Pad", color = Color.LightGray)
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(20.dp),
                                modifier = Modifier.padding(horizontal = 4.dp)

                            )
                            {
                                Chip(text = "Track", color = Color(0xFF9cc9EA))
                                //pointer
                                Chip(text = "D-Pad", color = Color.LightGray)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                        // Context Menu Dropdown
                        DropdownMenu(
                            expanded = showContextMenu,
                            onDismissRequest = { showContextMenu = false },
                            offset = contextMenuOffset,
                            modifier = Modifier
                                .width(280.dp)
                                .background(
                                    color = Color.DarkGray,
                                    shape = RoundedCornerShape(16.dp)
                                )
                        ) {
                            menuItems.forEach { item ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(48.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(item.color.copy(alpha = 0.2f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = item.icon,
                                                    contentDescription = null,
                                                    tint = item.color,
                                                    modifier = Modifier.size(40.dp)
                                                )
                                            }
                                            Text(
                                                text = item.label,
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color.White,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Icon(
                                                imageVector = Icons.Default.ChevronRight,
                                                contentDescription = null,
                                                tint = Color(0xFF94A3B8),
                                                modifier = Modifier.size(32.dp)
                                            )
                                        }
                                    },
                                    onClick = {
                                        selectedItem = item
                                        showContextMenu = false
                                    },
                                    modifier = Modifier.height(64.dp)
                                )
                            }
                        }
                    }

                    // Status indicator
                    selectedItem?.let { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .align(Alignment.TopCenter),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0x3310B981)
                            )
                        ) {
                            Text(
                                text = "✓ Selected: ${item.label}",
                                modifier = Modifier.padding(16.dp),
                                color = Color(0xFF6EE7B7)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Chip(text: String, color: Color) {
    Card(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, color),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Text(
            text = text,
            color = color,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
