package merail.edge2edge.samples.insets

import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import merail.edge2edge.samples.R

/**
 * Whether the current content is a full screen and should apply system bars paddings.
 * It is provided by the container of the screen, not by the screen itself
 */
val LocalApplySystemInsets = compositionLocalOf { false }

/**
 * Insets read from the root of the window. They are provided only on Android 10
 * and lower, where Compose WindowInsets may not receive any values
 */
val LocalWindowInsetsCompat = compositionLocalOf<WindowInsetsCompat?> { null }

/**
 * Applies system bars paddings, but only if the content is a full screen.
 * Must be placed after Modifier.background() (so that the background
 * goes under the system bars) and before inner paddings and scroll
 */
@Composable
fun Modifier.systemBarsScreenPadding() = if (LocalApplySystemInsets.current) {
    statusBarsPaddingCompat().navigationBarsPaddingCompat()
} else {
    this
}

/**
 * Applies the status bar padding, but only if the content is a full screen
 */
@Composable
fun Modifier.statusBarsScreenPadding() = if (LocalApplySystemInsets.current) {
    statusBarsPaddingCompat()
} else {
    this
}

/**
 * Applies the navigation bar (or keyboard) padding, but only if the content is a full screen
 */
@Composable
fun Modifier.navigationBarsScreenPadding() = if (LocalApplySystemInsets.current) {
    navigationBarsPaddingCompat()
} else {
    this
}

/**
 * Applies the status bar padding with support of Android 10 and lower
 */
@Composable
fun Modifier.statusBarsPaddingCompat(): Modifier {
    val insetsCompat = LocalWindowInsetsCompat.current ?: return statusBarsPadding()

    return padding(
        top = with(LocalDensity.current) {
            insetsCompat.statusBarHeight.toDp()
        },
    )
}

/**
 * Applies the navigation bar padding (or the keyboard padding, if it is open)
 * with support of Android 10 and lower
 */
@Composable
fun Modifier.navigationBarsPaddingCompat(): Modifier {
    val density = LocalDensity.current
    val insetsCompat = LocalWindowInsetsCompat.current
    val imeTopPadding = with(density) {
        dimensionResource(R.dimen.ime_top_padding).roundToPx()
    }

    val bottomPadding = if (insetsCompat == null) {
        bottomSystemPadding(
            navigationBarHeight = WindowInsets.navigationBars.getBottom(density),
            imeHeight = WindowInsets.ime.getBottom(density),
            imeTopPadding = imeTopPadding,
        )
    } else {
        insetsCompat.bottomSystemPadding(imeTopPadding)
    }

    return padding(
        bottom = with(density) {
            bottomPadding.toDp()
        },
    )
}

/**
 * On Android 10 and lower reads the insets from the root of the window
 * after every layout pass and provides them through [LocalWindowInsetsCompat]
 */
@Composable
fun SystemInsetsWrapper(
    view: View,
    content: @Composable () -> Unit,
) {
    val insetsState = remember {
        mutableStateOf<WindowInsetsCompat?>(null)
    }

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
        DisposableEffect(view) {
            val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
                ViewCompat.getRootWindowInsets(view)?.let { insets ->
                    insetsState.value = insets
                }
            }
            val viewTreeObserver = view.viewTreeObserver
            viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)

            ViewCompat.requestApplyInsets(view)

            onDispose {
                if (viewTreeObserver.isAlive) {
                    viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
                }
            }
        }
    }

    CompositionLocalProvider(
        value = LocalWindowInsetsCompat provides insetsState.value,
    ) {
        content()
    }
}
