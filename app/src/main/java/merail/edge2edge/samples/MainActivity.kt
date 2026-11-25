package merail.edge2edge.samples

import android.graphics.Color
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import merail.edge2edge.samples.navigation.Navigator
import merail.edge2edge.samples.screens.SamplesScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            // The system bars are transparent: the content itself is drawn under them
            statusBarStyle = SystemBarStyle.light(
                scrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT,
            ),
            navigationBarStyle = SystemBarStyle.light(
                scrim = Color.TRANSPARENT,
                // Before Android 8 the navigation bar cannot have dark icons,
                // so it is drawn with this solid color instead of a transparent one
                darkScrim = ContextCompat.getColor(this, R.color.screen_background),
            ),
        )

        val container = FrameLayout(this)
        setContentView(container)

        Navigator(
            activity = this,
            container = container,
        ).push(SamplesScreen)
    }
}
