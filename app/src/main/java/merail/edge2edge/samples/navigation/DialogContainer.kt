package merail.edge2edge.samples.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import merail.edge2edge.samples.R
import merail.edge2edge.samples.insets.applySystemBarsPadding

/**
 * A dialog over a dimmed screen. It handles system insets itself:
 * the dim goes under the system bars, but the card stays between them
 * and above the keyboard
 */
@SuppressLint("ViewConstructor")
class DialogContainer(
    context: Context,
    content: View,
    onDismiss: () -> Unit,
) : FrameLayout(context) {

    init {
        setBackgroundColor(ContextCompat.getColor(context, R.color.scrim))
        setOnClickListener {
            onDismiss()
        }

        val card = FrameLayout(context).apply {
            background = GradientDrawable().apply {
                setColor(ContextCompat.getColor(context, R.color.card_background))
                cornerRadius = resources.getDimension(R.dimen.corner_radius)
            }
            clipToOutline = true
            // Touches on the card must not dismiss the dialog
            isClickable = true
            addView(content, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
        }
        addView(
            card,
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER).apply {
                val margin = resources.getDimensionPixelSize(R.dimen.dialog_margin)
                marginStart = margin
                marginEnd = margin
            },
        )

        applySystemBarsPadding()
    }
}
