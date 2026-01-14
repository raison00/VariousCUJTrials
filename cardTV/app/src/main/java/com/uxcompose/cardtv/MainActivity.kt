package com.uxcompose.cardtv

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.color
import androidx.tv.material3.Card
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text

private object PlayingCardDefaults {
    val CardWidth = 80.dp
    val CardHeight = 112.dp
    val CornerPadding = 4.dp
    val CenterSymbolSize = 32.sp
}

private fun Modifier.bottomEndRotated(scope: BoxScope, padding: Dp): Modifier = with(scope) {
this
    .align(Alignment.BottomEnd)
    .padding(padding)
    .graphicsLayer { rotationZ = 180f }

}
@OptIn(ExperimentalTvMaterial3ai::class)
@Composable
fun PlayingCardView(
    card: androidx.tv.material3.Card,
    isFaceUp: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.size(
            width = PlayingCardDefaults.CardWidth,
            height = PlayingCardDefaults.CardHeight
        ),
        shape = androidx.compose.material3.CardDefaults.shape(shape = MaterialTheme.shapes.small),
        colors = androidx.compose.material3.CardDefaults.colors(
            containerColor = if (isFaceUp) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary
        ),
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (isFaceUp) {
                val (suit, rank) = card // Destructuring for readability

                // Top-left corner
                if (isFaceUp) {
                    // ... Top-left and Center content

                    // Bottom-right corner (rotated)
                    // The call is now on the BoxScope, not Modifier
                    Box(modifier = bottomEndRotatedModifier(PlayingCardDefaults.CornerPadding)) {
                        CornerContent(suit = suit, rank = rank)
                    }

                // Center symbol
                Text(
                    text = suit.symbol,
                    color = suit.color,
                    fontSize = PlayingCardDefaults.CenterSymbolSize,
                    modifier = Modifier.align(Alignment.Center)
                )

                // Bottom-right corner (rotated)
                Box(modifier = Modifier.bottomEndRotated(PlayingCardDefaults.CornerPadding)) {
                    CornerContent(suit = suit, rank = rank) // Assumes CornerContent is updated
                }
            }
            // Card back is handled by the Card's containerColor
        }
    }
}


@Composable
fun CornerContent(card: Card) {
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