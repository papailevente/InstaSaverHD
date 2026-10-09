package com.example.instasaverhd.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Instagram Brand Gradient Colors
val InstaPurple = Color(0xFF833AB4)
val InstaPink = Color(0xFFE1306C)
val InstaRed = Color(0xFFFD1D1D)
val InstaOrange = Color(0xFFF56040)
val InstaYellow = Color(0xFFFCB045)

// App Dark Theme Base Colors
val DarkBackground = Color(0xFF0F172A)
val DarkSurface = Color(0xFF1E293B)
val DarkSurfaceGlass = Color(0xB31E293B) // 70% opacity for glassmorphism
val DarkSurfaceVariant = Color(0xFF334155)
val DarkBorder = Color(0x33FFFFFF)

// Gradient Brushes
val InstagramGradient = Brush.horizontalGradient(
    colors = listOf(InstaPurple, InstaPink, InstaRed, InstaOrange, InstaYellow)
)

val InstagramGradientVertical = Brush.verticalGradient(
    colors = listOf(InstaPurple, InstaPink, InstaRed, InstaYellow)
)

val GlassCardGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0x26FFFFFF),
        Color(0x0AFFFFFF)
    )
)
