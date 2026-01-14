package com.uxcompose.large_screen_optimized

import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A data class representing a single item in our custom context menu.
 *
 * @param label The text to display for the menu item.
 * @param icon The vector icon to display.
 * @param color The tint color for the icon.
 */
data class ContextMenuItem(
    val label: String,
    val icon: ImageVector,
    val color: Color = Color.Unspecified // Default to unspecified to use local tint
)

/**
 * A Large-Screen Optimized Context Menu wrapper.
 * Handles Touch vs. Mouse input, Occlusion Offset, Haptics, and Keyboard Navigation.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AdaptiveContextMenu(
    menuItems: List<ContextMenuItem>,
    onItemSelected: (ContextMenuItem) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var menuOffset by remember { mutableStateOf(DpOffset.Zero) }
    var isTouchInput by remember { mutableStateOf(false) }

    val density = LocalDensity.current
    val haptic = LocalHapticFeedback.current
    val focusRequester = remember { FocusRequester() }

    // 1. Entrance Animation Logic
    val scale by animateFloatAsState(
        targetValue = if (showMenu) 1f else 0.5f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "Scale"
    )
    val alpha by animateFloatAsState(
        targetValue = if (showMenu) 1f else 0f,
        animationSpec = tween(150),
        label = "Alpha"
    )

    Box(modifier = modifier) {
        // 2. Trigger Area with Input Detection
        content(
            Modifier
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            val type = event.changes.first().type

                            if (event.type == PointerEventType.Press) {
                                isTouchInput = type == PointerType.Touch || type == PointerType.Stylus

                                if (event.buttons.isSecondaryPressed) {
                                    val pos = event.changes.first().position
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                                    // Offset Y if touch to avoid hiding under thumb
                                    val yOffset = if (isTouchInput) pos.y - 120f else pos.y

                                    menuOffset = DpOffset(
                                        x = with(density) { pos.x.toDp() },
                                        y = with(density) { yOffset.toDp() }
                                    )
                                    showMenu = true
                                }
                            }
                        }
                    }
                }
                .combinedClickable(
                    onClick = { showMenu = false },
                    onLongClick = {
                        // Standard long-press fallback for touch
                        if (!showMenu) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            // Default to center if pointerInput didn't catch specific pos
                            showMenu = true
                        }
                    }
                )
        )

        // 3. Animated & Focusable Dropdown
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
            offset = menuOffset,
            modifier = Modifier
                .width(280.dp)
                .focusRequester(focusRequester)
                .onKeyEvent {
                    if (it.key == Key.Escape) {
                        showMenu = false; true
                    } else false
                }
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                    transformOrigin = TransformOrigin(0f, 0f)
                }
        ) {
            menuItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.label, fontSize = 16.sp) },
                    leadingIcon = { Icon(item.icon, contentDescription = null, tint = item.color) },
                    onClick = {
                        onItemSelected(item)
                        showMenu = false
                    },
                    modifier = Modifier.height(56.dp)
                )
            }
        }
    }

    LaunchedEffect(showMenu) {
        if (showMenu) focusRequester.requestFocus()
    }
}
