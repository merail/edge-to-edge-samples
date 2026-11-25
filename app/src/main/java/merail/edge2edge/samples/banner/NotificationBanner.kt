package merail.edge2edge.samples.banner

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import merail.edge2edge.samples.R
import merail.edge2edge.samples.insets.doOnApplySystemInsets
import merail.edge2edge.samples.insets.statusBarHeight

private const val BANNER_DURATION_MS = 3000L

/**
 * A banner over all screens, placed right below the status bar
 */
@SuppressLint("ViewConstructor")
class NotificationBanner(
    context: Context,
    text: String,
) : TextView(context) {

    private val margin = resources.getDimensionPixelSize(R.dimen.banner_margin)

    init {
        this.text = text
        setTextColor(Color.WHITE)
        val padding = resources.getDimensionPixelSize(R.dimen.screen_padding)
        setPadding(padding, padding, padding, padding)
        background = GradientDrawable().apply {
            setColor(ContextCompat.getColor(context, R.color.banner_background))
            cornerRadius = resources.getDimension(R.dimen.corner_radius)
        }
        // Accessibility services announce the banner when it appears
        accessibilityLiveRegion = View.ACCESSIBILITY_LIVE_REGION_POLITE
    }

    fun show(parent: FrameLayout) {
        parent.addView(
            this,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.TOP,
            ).apply {
                setMargins(margin, margin, margin, 0)
            },
        )

        doOnApplySystemInsets { insets ->
            val topMargin = margin + insets.statusBarHeight
            if (marginTop != topMargin) {
                updateLayoutParams<FrameLayout.LayoutParams> {
                    this.topMargin = topMargin
                }
            }
        }

        alpha = 0f
        animate().alpha(1f)
        postDelayed(
            {
                animate()
                    .alpha(0f)
                    .withEndAction {
                        parent.removeView(this)
                    }
            },
            BANNER_DURATION_MS,
        )
    }
}
