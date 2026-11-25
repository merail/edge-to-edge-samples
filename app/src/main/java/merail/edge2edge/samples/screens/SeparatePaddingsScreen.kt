package merail.edge2edge.samples.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import merail.edge2edge.samples.R
import merail.edge2edge.samples.insets.navigationBarsScreenPadding
import merail.edge2edge.samples.insets.statusBarsScreenPadding
import merail.edge2edge.samples.navigation.ComposeScreen
import merail.edge2edge.samples.navigation.Navigator

/**
 * The scrollable content is padded from the status bar, and the button
 * below it is padded from the navigation bar (or the keyboard)
 */
object SeparatePaddingsScreen : ComposeScreen() {

    @Composable
    override fun Content(navigator: Navigator) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.screen_background)),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .statusBarsScreenPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
            ) {
                Text(
                    text = "Separate paddings",
                    style = MaterialTheme.typography.headlineSmall,
                )

                repeat(8) { index ->
                    SampleTextField(
                        label = "Field ${index + 1}",
                    )
                }
            }

            Button(
                onClick = navigator::pop,
                modifier = Modifier
                    .navigationBarsScreenPadding()
                    .padding(16.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = "Done",
                )
            }
        }
    }
}
