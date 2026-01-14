package com.uxcompose.cardtv

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text

@OptIn(ExperimentalTvMaterial3ai::class)
@Composable
fun PlayingCardView(
    card: Card,
    onClick: () -> Unit,
    modifier: Modifier = Modifier, isFaceUp: Boolean = card.isFaceUp,
) {
    Card(
        onClick = onClick,
        modifier = modifier.size(width = 80.dp, height = 112.dp),
        shape = androidx.compose.material3.CardDefaults.shape(shape = MaterialTheme.shapes.small),
        colors = androidx.compose.material3.CardDefaults.colors(
            containerColor = if (isFaceUp) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary
        ),
        // Omitted other Card properties like scale, border, etc. for clarity unless needed.
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (isFaceUp) {
                // Top-left corner
                Box(modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(4.dp)) {
                    CornerContent(card = card)
                }

                // Center symbol
                Text(
                    text = card.suit.symbol,
                    color = card.suit.color,
                    fontSize = 32.sp,
                    modifier = Modifier.align(Alignment.Center)
                )

                // Bottom-right corner (rotated)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                        .graphicsLayer { rotationZ = 180f }
                ) {
                    CornerContent(card = card)
                }
            }
            // The card back is now handled entirely by the Card's containerColor.
        }
    }
}

@Composable
private fun CornerContent(card: Card) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = card.rank.symbol,
            color = card.suit.color,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = card.suit.symbol,
            color = card.suit.color,
            style = MaterialTheme.typography.bodySmall
        )
    }
}