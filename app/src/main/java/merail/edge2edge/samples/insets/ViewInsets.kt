package merail.edge2edge.samples.insets

import android.content.res.Resources
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import merail.edge2edge.samples.R

/**
 * Sets paddings equal to the system bars: the status bar on top
 * and the navigation bar (or the keyboard, if it is open) at the bottom.
 * The paddings from the layout are kept and added to the system ones
 */
fun View.applySystemBarsPadding(
    innerPaddingTop: Int = paddingTop,
    innerPaddingBottom: Int = paddingBottom,
) {
    val imeTopPadding = resources.getDimensionPixelSize(R.dimen.ime_top_padding)

    doOnApplySystemInsets { insets ->
        updatePadding(
            top = innerPaddingTop + insets.statusBarHeight,
            bottom = innerPaddingBottom + insets.bottomSystemPadding(imeTopPadding),
        )
    }
}

/**
 * Calls [block] every time the system insets of the window change.
 *
 * On Android 11+ the insets come through OnApplyWindowInsetsListener.
 * On Android 10 and lower they may not reach the view at all, so they are
 * read from the root of the window after every layout pass
 */
fun View.doOnApplySystemInsets(block: (insets: WindowInsetsCompat) -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
            block(insets)
            insets
        }

        requestApplyInsetsWhenAttached(
            onDetach = {
                ViewCompat.setOnApplyWindowInsetsListener(this, null)
            },
        )
    } else {
        val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            ViewCompat.getRootWindowInsets(this)?.let(block)
        }

        viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)

        requestApplyInsetsWhenAttached(
            onDetach = {
                viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
            },
        )
    }
}

/**
 * A view added to an already displayed window does not receive insets
 * by itself, so we request them as soon as the view is attached.
 * After the view is detached, the listeners are removed
 */
private fun View.requestApplyInsetsWhenAttached(onDetach: () -> Unit) {
    if (isAttachedToWindow) {
        ViewCompat.requestApplyInsets(this)
    }

    addOnAttachStateChangeListener(
        object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                ViewCompat.requestApplyInsets(v)
            }

            override fun onViewDetachedFromWindow(v: View) {
                onDetach()
                v.removeOnAttachStateChangeListener(this)
            }
        },
    )
}

/**
 * The bottom padding: the navigation bar height, or the keyboard height plus
 * a small gap, if the keyboard is open (the keyboard already covers the navigation bar)
 */
fun WindowInsetsCompat.bottomSystemPadding(imeTopPadding: Int) = bottomSystemPadding(
    navigationBarHeight = navigationBarHeight,
    imeHeight = getInsets(WindowInsetsCompat.Type.ime()).bottom,
    imeTopPadding = imeTopPadding,
)

internal fun bottomSystemPadding(
    navigationBarHeight: Int,
    imeHeight: Int,
    imeTopPadding: Int,
) = if (imeHeight == 0) {
    navigationBarHeight
} else {
    imeHeight + imeTopPadding
}

/**
 * Some devices (for example, Huawei P40 Lite with EMUI 10.1) return zero insets
 * for the system bars. In this case approximate heights are used instead
 */
private const val STATUS_BAR_FALLBACK_HEIGHT_DP = 40
private const val NAVIGATION_BAR_FALLBACK_HEIGHT_DP = 20

val WindowInsetsCompat.statusBarHeight: Int
    get() = getInsets(WindowInsetsCompat.Type.systemBars()).top
        .ifZero { STATUS_BAR_FALLBACK_HEIGHT_DP.dpToPx() }

val WindowInsetsCompat.navigationBarHeight: Int
    get() = getInsets(WindowInsetsCompat.Type.systemBars()).bottom
        .ifZero { NAVIGATION_BAR_FALLBACK_HEIGHT_DP.dpToPx() }

private inline fun Int.ifZero(defaultValue: () -> Int) = if (this == 0) defaultValue() else this

private fun Int.dpToPx() = (this * Resources.getSystem().displayMetrics.density).toInt()
