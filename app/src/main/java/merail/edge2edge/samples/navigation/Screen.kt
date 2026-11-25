package merail.edge2edge.samples.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.ComposeView
import merail.edge2edge.samples.insets.LocalApplySystemInsets
import merail.edge2edge.samples.insets.SystemInsetsWrapper
import merail.edge2edge.samples.insets.applySystemBarsPadding
import merail.edge2edge.samples.ui.theme.EdgeToEdgeSamplesTheme

/**
 * A screen of the app: View or Compose content that [Navigator] places into a container
 */
interface Screen {
    /**
     * @param applySystemInsets whether the screen should apply system bars paddings itself.
     * Only full screens do it: bottom sheets and dialogs handle insets on their own
     */
    fun createView(
        context: Context,
        navigator: Navigator,
        applySystemInsets: Boolean,
    ): View
}

abstract class ViewScreen(
    @param:LayoutRes private val layoutRes: Int,
) : Screen {

    open fun onViewCreated(view: View, navigator: Navigator) = Unit

    // Layout params are set by the container
    @SuppressLint("InflateParams")
    final override fun createView(
        context: Context,
        navigator: Navigator,
        applySystemInsets: Boolean,
    ): View {
        val view = LayoutInflater.from(context).inflate(layoutRes, null)

        if (applySystemInsets) {
            view.applySystemBarsPadding()
        }

        onViewCreated(view, navigator)

        return view
    }
}

abstract class ComposeScreen : Screen {

    @Composable
    abstract fun Content(navigator: Navigator)

    final override fun createView(
        context: Context,
        navigator: Navigator,
        applySystemInsets: Boolean,
    ): View = ComposeView(context).apply {
        // Several ComposeViews live in one window, and each of them
        // needs its own id to save its state
        id = View.generateViewId()

        setContent {
            SystemInsetsWrapper(view = this) {
                CompositionLocalProvider(
                    value = LocalApplySystemInsets provides applySystemInsets,
                ) {
                    EdgeToEdgeSamplesTheme {
                        Content(navigator)
                    }
                }
            }
        }
    }
}
