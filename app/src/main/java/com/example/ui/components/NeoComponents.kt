package com.example.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeoBlack

/**
 * Custom Draw-Behind Neobrutalist hard shadow modifier.
 * It simulates a flat offset solid shadow without any blur.
 */
fun Modifier.neoShadow(
    shadowColor: Color = NeoBlack,
    offsetX: Dp = 6.dp,
    offsetY: Dp = 6.dp,
    cornerRadius: Dp = 24.dp
): Modifier = this.drawBehind {
    drawRoundRect(
        color = shadowColor,
        topLeft = Offset(offsetX.toPx(), offsetY.toPx()),
        size = size,
        cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx())
    )
}

@Composable
fun NeoCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    borderColor: Color = NeoBlack,
    borderWidth: Dp = 4.dp,
    shadowColor: Color = NeoBlack,
    shadowOffset: Dp = 6.dp,
    cornerRadius: Dp = 24.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .padding(bottom = shadowOffset, end = shadowOffset)
            .neoShadow(
                shadowColor = shadowColor,
                offsetX = shadowOffset,
                offsetY = shadowOffset,
                cornerRadius = cornerRadius
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerRadius)
            )
            .border(
                border = BorderStroke(borderWidth, borderColor),
                shape = RoundedCornerShape(cornerRadius)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            content()
        }
    }
}

@Composable
fun NeoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = NeoBlack,
    textColor: Color = Color.White,
    borderColor: Color = NeoBlack,
    borderWidth: Dp = 4.dp,
    cornerRadius: Dp = 16.dp,
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // When pressed, translate down and right, and shrink shadow to simulate mechanical pressing
    val offsetTranslation by animateDpAsState(targetValue = if (isPressed) 4.dp else 0.dp, label = "ButtonPress")
    val shadowOffset = if (isPressed) 2.dp else 6.dp

    Box(
        modifier = modifier
            .padding(bottom = 6.dp, end = 6.dp)
            .offset(x = offsetTranslation, y = offsetTranslation)
            .neoShadow(
                shadowColor = NeoBlack,
                offsetX = shadowOffset,
                offsetY = shadowOffset,
                cornerRadius = cornerRadius
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerRadius)
            )
            .border(
                border = BorderStroke(borderWidth, borderColor),
                shape = RoundedCornerShape(cornerRadius)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null, // Disable default ripple to keep tactile brutalism look
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            content()
        }
    }
}

@Composable
fun NeoBadge(
    text: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    textColor: Color = NeoBlack
) {
    Box(
        modifier = modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(4.dp))
            .border(width = 2.dp, color = NeoBlack, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            color = textColor,
            letterSpacing = 0.5.sp
        )
    }
}
