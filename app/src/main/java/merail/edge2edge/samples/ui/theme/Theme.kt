package merail.edge2edge.samples.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/**
 * Only the light theme: the system bars are set up for light content
 */
@Composable
fun EdgeToEdgeSamplesTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = lightColorScheme(),
        typography = Typography,
        content = content,
    )
}
