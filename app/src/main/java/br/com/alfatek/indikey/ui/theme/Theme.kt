package br.com.alfatek.indikey.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

//val Purple80 = Color(0xFFD0BCFF)
//val PurpleGrey80 = Color(0xFFCCC2DC)
//val Pink80 = Color(0xFFEFB8C8)

//val Purple40 = Color(0xFF6650a4)
//val PurpleGrey40 = Color(0xFF625b71)
//val Pink40 = Color(0xFF7D5260)

// Dark Theme Colors
val DarkPrimary = Color(0xFFBB86FC) // A vibrant purple for primary elements
val DarkOnPrimary = Color(0xFF000000) // Black text on primary elements
val DarkPrimaryContainer = Color(0xFF3700B3) // A darker purple for containers
val DarkOnPrimaryContainer = Color(0xFFFFFFFF) // White text on primary containers
val DarkSecondary = Color(0xFF03DAC6) // A teal for secondary elements
val DarkOnSecondary = Color(0xFF000000) // Black text on secondary elements
val DarkSecondaryContainer = Color(0xFF018786) // A darker teal for containers
val DarkOnSecondaryContainer = Color(0xFFFFFFFF) // White text on secondary containers
val DarkTertiary = Color(0xFFCF6679) // A pink for tertiary elements
val DarkOnTertiary = Color(0xFF000000) // Black text on tertiary elements
val DarkTertiaryContainer = Color(0xFF7A0036) // A darker pink for containers
val DarkOnTertiaryContainer = Color(0xFFFFFFFF) // White text on tertiary containers
val DarkBackground = Color(0xFF121212) // A dark gray for the background
val DarkOnBackground = Color(0xFFFFFFFF) // White text on the background
val DarkSurface = Color(0xFF1E1E1E) // A slightly lighter dark gray for surfaces
val DarkOnSurface = Color(0xFFFFFFFF) // White text on surfaces
val DarkError = Color(0xFFCF6679) // A bright red for errors
val DarkOnError = Color(0xFF000000) // Black text on error elements
val DarkErrorContainer = Color(0xFFB00020) // A darker red for error containers
val DarkOnErrorContainer = Color(0xFFFFFFFF) // White text on error containers
val DarkOutline = Color(0xFF999999) // A light gray for outlines

val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,
    outline = DarkOutline
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

)

@Composable
fun IndiKeyTheme(
    darkTheme: Boolean = false,//isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}