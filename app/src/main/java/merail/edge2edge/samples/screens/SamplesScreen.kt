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
import merail.edge2edge.samples.insets.systemBarsScreenPadding
import merail.edge2edge.samples.navigation.ComposeScreen
import merail.edge2edge.samples.navigation.Navigator

object SamplesScreen : ComposeScreen() {

    @Composable
    override fun Content(navigator: Navigator) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.screen_background))
                .systemBarsScreenPadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            Text(
                text = "Edge-to-edge samples",
                style = MaterialTheme.typography.headlineSmall,
            )

            SampleButton(
                text = "View screen",
                onClick = {
                    navigator.push(ViewFormScreen)
                },
            )

            SampleButton(
                text = "Compose screen",
                onClick = {
                    navigator.push(ComposeFormScreen)
                },
            )

            SampleButton(
                text = "Separate paddings",
                onClick = {
                    navigator.push(SeparatePaddingsScreen)
                },
            )

            SampleButton(
                text = "Embedded content",
                onClick = {
                    navigator.push(EmbeddedContentScreen)
                },
            )

            SampleButton(
                text = "Bottom sheet",
                onClick = {
                    navigator.showBottomSheet(ComposeFormScreen)
                },
            )

            SampleButton(
                text = "Dialog",
                onClick = {
                    navigator.showDialog(DialogContentScreen)
                },
            )

            SampleButton(
                text = "Notification banner",
                onClick = {
                    navigator.showBanner("The banner is placed right below the status bar")
                },
            )
        }
    }
}

@Composable
private fun SampleButton(
    text: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = text,
        )
    }
}
