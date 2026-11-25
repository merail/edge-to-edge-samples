package merail.edge2edge.samples.screens

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import merail.edge2edge.samples.R
import merail.edge2edge.samples.navigation.Navigator
import merail.edge2edge.samples.navigation.ViewScreen

object ViewFormScreen : ViewScreen(R.layout.screen_view_form) {

    override fun onViewCreated(view: View, navigator: Navigator) {
        val fields = view.findViewById<LinearLayout>(R.id.fields)
        repeat(8) { index ->
            fields.addView(
                EditText(view.context).apply {
                    hint = view.context.getString(R.string.field_hint, index + 1)
                    isSingleLine = true
                },
            )
        }

        view.findViewById<Button>(R.id.done).setOnClickListener {
            navigator.pop()
        }
    }
}
