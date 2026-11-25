package merail.edge2edge.samples.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Outline
import android.view.Gravity
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import merail.edge2edge.samples.R
import merail.edge2edge.samples.insets.bottomSystemPadding
import merail.edge2edge.samples.insets.doOnApplySystemInsets
import merail.edge2edge.samples.insets.statusBarHeight

/**
 * A bottom sheet over a dimmed screen. It handles system insets itself:
 * the sheet goes under the navigation bar, but its content stays above
 * the bar (or the keyboard), and the sheet never goes under the status bar
 */
@SuppressLint("ViewConstructor")
class BottomSheetContainer(
    context: Context,
    content: View,
    onDismiss: () -> Unit,
) : FrameLayout(context) {

    private val sheet = FrameLayout(context)

    init {
        setBackgroundColor(ContextCompat.getColor(context, R.color.scrim))
        setOnClickListener {
            onDismiss()
        }

        val cornerRadius = resources.getDimension(R.dimen.corner_radius)
        with(sheet) {
            setBackgroundColor(ContextCompat.getColor(context, R.color.screen_background))
            // Only the top corners are rounded: the bottom ones are moved out of the view
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setRoundRect(
                        0,
                        0,
                        view.width,
                        view.height + cornerRadius.toInt(),
                        cornerRadius,
                    )
                }
            }
            clipToOutline = true
            // Touches on the sheet must not dismiss it
            isClickable = true
            addView(content, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
        }
        addView(sheet, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, Gravity.BOTTOM))

        val imeTopPadding = resources.getDimensionPixelSize(R.dimen.ime_top_padding)
        doOnApplySystemInsets { insets ->
            sheet.updatePadding(
                bottom = insets.bottomSystemPadding(imeTopPadding),
            )

            val topMargin = insets.statusBarHeight
            if (sheet.marginTop != topMargin) {
                sheet.updateLayoutParams<LayoutParams> {
                    this.topMargin = topMargin
                }
            }
        }
    }
}
