package merail.edge2edge.samples.navigation

import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import merail.edge2edge.samples.R
import merail.edge2edge.samples.banner.NotificationBanner

/**
 * The simplest navigation: all screens, bottom sheets and dialogs are views
 * placed one above another in a single container
 */
class Navigator(
    private val activity: ComponentActivity,
    private val container: FrameLayout,
) {
    private val backStack = mutableListOf<View>()

    private val onBackPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() = pop()
    }

    init {
        activity.onBackPressedDispatcher.addCallback(activity, onBackPressedCallback)
    }

    /**
     * Opens a full screen. Only such screens apply system insets themselves
     */
    fun push(screen: Screen) {
        val view = screen.createView(
            context = activity,
            navigator = this,
            applySystemInsets = true,
        )
        // Touches must not reach the screen below
        view.isClickable = true

        add(view)
    }

    /**
     * Opens a bottom sheet. It handles system insets itself, so the screen inside it must not
     */
    fun showBottomSheet(screen: Screen) = add(
        BottomSheetContainer(
            context = activity,
            content = screen.createView(
                context = activity,
                navigator = this,
                applySystemInsets = false,
            ),
            onDismiss = ::pop,
        ),
    )

    /**
     * Opens a dialog. It handles system insets itself, so the screen inside it must not
     */
    fun showDialog(screen: Screen) = add(
        DialogContainer(
            context = activity,
            content = screen.createView(
                context = activity,
                navigator = this,
                applySystemInsets = false,
            ),
            onDismiss = ::pop,
        ),
    )

    fun showBanner(text: String) = NotificationBanner(
        context = activity,
        text = text,
    ).show(container)

    fun pop() {
        if (backStack.size > 1) {
            container.removeView(backStack.removeAt(backStack.lastIndex))
            onBackStackChanged()
        }
    }

    private fun add(view: View) {
        container.addView(
            view,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )
        backStack += view
        onBackStackChanged()
    }

    private fun onBackStackChanged() {
        onBackPressedCallback.isEnabled = backStack.size > 1

        // Accessibility services must see only the top screen
        backStack.forEachIndexed { index, view ->
            view.importantForAccessibility = if (index == backStack.lastIndex) {
                View.IMPORTANT_FOR_ACCESSIBILITY_AUTO
            } else {
                View.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS
            }
        }

        updateNavigationBarColor()
    }

    /**
     * Before Android 8 the navigation bar is drawn with a solid color over the content,
     * so the dim of a dialog does not reach it. In this case the bar is dimmed manually
     */
    @Suppress("DEPRECATION")
    private fun updateNavigationBarColor() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            val backgroundColor = ContextCompat.getColor(activity, R.color.screen_background)

            activity.window.navigationBarColor = if (backStack.last() is DialogContainer) {
                ColorUtils.compositeColors(
                    ContextCompat.getColor(activity, R.color.scrim),
                    backgroundColor,
                )
            } else {
                backgroundColor
            }
        }
    }
}
