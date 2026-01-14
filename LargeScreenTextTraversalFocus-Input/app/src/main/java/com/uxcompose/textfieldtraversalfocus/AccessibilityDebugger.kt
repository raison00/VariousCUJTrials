package com.uxcompose.textfieldtraversalfocus

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.min


fun Color.contrastAgainst(background: Color): Float {
    val l1 = max(this.luminance(), background.luminance())
    val l2 = min(this.luminance(), background.luminance())
    return (l1 + 0.05f) / (l2 + 0.05f)
}
// Data class to represent the result of a check
data class ContrastResult(
    val label: String,
    val containerColor: Color,
    val contentColor: Color,
    val ratio: Double
) {
    val isPassAA: Boolean = ratio >= 4.5
    val isPassAAA: Boolean = ratio >= 7.0
}

/**
 * Extension to check contrast between any two colors
 * Usage: colorA.contrastAgainst(colorB)

fun Color.contrastAgainst(background: Color): Float {
    val foregroundLuminance = this.luminance()
    val backgroundLuminance = background.luminance()

    val l1 = max(foregroundLuminance, backgroundLuminance)
    val l2 = min(foregroundLuminance, backgroundLuminance)

    return (l1 + 0.05f) / (l2 + 0.05f)
}
 */
@Composable
fun ThemeAccessibilityDashboard() {
    val scheme = MaterialTheme.colorScheme

    val checkResults = remember(scheme) {
        listOf(
            "Primary" to (scheme.primary to scheme.onPrimary),
            "Tertiary (Success)" to (scheme.tertiary to scheme.onTertiary),
            "Error Container" to (scheme.errorContainer to scheme.onErrorContainer)
        ).map { (label, pair) ->
            ContrastResult(
                label = label,
                containerColor = pair.first,
                contentColor = pair.second,
                // FIX: Use the extension function directly on the color
                ratio = pair.second.contrastAgainst(pair.first).toDouble()
            )
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Theme Accessibility Check",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(checkResults, key = { it.label }) { result ->
            ContrastItemCard(result)
        }
    }
}


@Composable
fun ContrastItemCard(result: ContrastResult) {
    // 2. Use a Surface or Card to better visualize the color pair
    Surface(
        color = result.containerColor,
        contentColor = result.contentColor,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = result.label, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "Ratio: ${"%.2f".format(result.ratio)}:1",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // 3. Clearer visual indicator of pass/fail
            StatusBadge(result)
        }
    }
}

@Composable
fun StatusBadge(result: ContrastResult) {
    val (text, badgeColor) = when {
        result.isPassAAA -> "AAA PASS" to Color(0xFF4CAF50) // Green
        result.isPassAA -> "AA PASS" to Color(0xFF8BC34A)  // Light Green
        else -> "FAIL" to MaterialTheme.colorScheme.error
    }

    Surface(
        color = badgeColor,
        contentColor = Color.White,
        shape = CircleShape
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}
