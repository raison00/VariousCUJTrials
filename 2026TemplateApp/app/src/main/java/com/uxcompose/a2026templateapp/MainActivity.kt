package com.uxcompose.a2026templateapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.uxcompose.a2026templateapp.ui.theme._2026TemplateAppTheme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uxcompose.a2026templateapp.ui.theme.Typography

data class MenuItem(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val color: Color
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var darkTheme by remember { mutableStateOf(false) }

            _2026TemplateAppTheme(darkTheme = darkTheme) {
                TemplateApp(
                    darkTheme = darkTheme,
                    onThemeToggle = { darkTheme = !darkTheme }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateApp(darkTheme: Boolean, onThemeToggle: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Material3 Template") },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = if (darkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = "Theme icon",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Switch(
                            checked = darkTheme,
                            onCheckedChange = { onThemeToggle() }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Section1()
            Section2()
            Section3()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Section1() {
    var showContextMenu by remember { mutableStateOf(false) }
    var contextMenuOffset by remember { mutableStateOf(DpOffset.Zero) }
    var selectedItem by remember { mutableStateOf<MenuItem?>(null) }
    val density = LocalDensity.current
    val haptics = LocalHapticFeedback.current
    var contentSize by remember { mutableStateOf(IntSize.Zero) }

    val menuItems = remember {
        listOf(
            MenuItem("1", "Copy", Icons.Default.ContentCopy, Color.Blue),
            MenuItem("2", "Paste", Icons.Default.ContentPaste, Color.Green),
            MenuItem("3", "Cut", Icons.Default.ContentCut, Color.Red)
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Section 1: Welcome",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "This is a Material3 template with modern components and theme support.",
                style = MaterialTheme.typography.bodyMedium
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { /* Action */ }, modifier = Modifier.weight(1f)) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Start")
                }
                OutlinedButton(onClick = { /* Action */ }, modifier = Modifier.weight(1f)) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null)
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Learn")
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .onSizeChanged { contentSize = it },
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xCC1E293B)
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0x090088EA),
                                        Color(0x03002eF6)
                                    )
                                )
                            )
                            .padding(all = 8.dp)
                            .padding(top = 48.dp)
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
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFE9D5FF)
                                )
                                Text(
                                    text = "Android 16 • Jetpack Compose 1.10.0 • Material3 for Compose 1.4.0",
                                    fontSize = 16.sp,
                                    color = Color(0xFFE9D5FF)
                                )
                            }
                        }
                    }

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
                                    onClick = {
                                        showContextMenu = false
                                        println("onClick")
                                    },
                                    onLongClick = {

                                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                        showContextMenu = true
                                        contextMenuOffset = DpOffset(
                                            x = with(density) { (contentSize.width / 2).toFloat().toDp() },
                                            y = with(density) { (contentSize.height / 2).toFloat().toDp() }
                                        )
                                        println("onLongClick")
                                    }
                                )
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onPress = {
                                            awaitPointerEventScope {
                                                val event = awaitPointerEvent()
                                                if (event.buttons.isSecondaryPressed) {
                                                    showContextMenu = true
                                                    contextMenuOffset = DpOffset(
                                                        x = with(density) { event.changes.first().position.x.toDp() },
                                                        y = with(density) { event.changes.first().position.y.toDp() }
                                                    )
                                                }
                                            }
                                        }
                                    )
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
                                    text = "Long-press (touch) or right-click (mouse) or a D-pad or long-press or 2-finger click on a trackpad  to access the same contextual menu.",
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

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                ) {
                                    Chip(
                                        text = "Touch",
                                        color = Color(0xFF9999EA)
                                    )
                                    Chip(
                                        text = "Mouse",
                                        color = Color(0xFF3B82F6)
                                    )
                                }
                                Spacer(modifier = Modifier.height(14.dp))

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                                    modifier = Modifier.padding(horizontal = 4.dp)

                                )
                                {
                                    Chip(
                                        text = "Track",
                                        color = Color(0xFF9cc9EA)
                                    )
                                    Chip(
                                        text = "D-Pad",
                                        color = Color.LightGray
                                    )
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                            DropdownMenu(
                                expanded = showContextMenu,
                                onDismissRequest = { showContextMenu = false },
                                offset = contextMenuOffset,
                                modifier = Modifier
                                    .width(280.dp)
                                    .background(
                                        color = Color(0x03002eF6),
                                        shape = RoundedCornerShape(4.dp)
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
}

@Composable
fun Chip(text: String, color: Color) {
    Surface(
        color = color,
        shape = CircleShape
    ) {
        Text(
            text = text,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun Section2() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    tint = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "Section 2: Features",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Text(
                text = "Explore various Material3 components and interactions.",
                style = MaterialTheme.typography.bodyMedium
            )

            var checkedState by remember { mutableStateOf(false) }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = checkedState,
                    onCheckedChange = { checkedState = it }
                )
                Text("Enable notifications")
            }

            FilledTonalButton(
                onClick = { /* Action */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Configure Settings")
            }
        }
    }
}

@Composable
fun Section3() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star",
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = "Section 3: Actions",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }

            Text(
                text = "Quick actions and common tasks for your workflow. Currently not configured to work, for view only.",
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ElevatedButton(
                    onClick = { /* Action */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }

                ElevatedButton(
                    onClick = { /* Action */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }

                ElevatedButton(
                    onClick = { /* Action */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            FloatingActionButton(
                onClick = { /* Action */ },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    }
}
