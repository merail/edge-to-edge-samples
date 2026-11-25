package merail.edge2edge.samples.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import merail.edge2edge.samples.navigation.ComposeScreen
import merail.edge2edge.samples.navigation.Navigator

object DialogContentScreen : ComposeScreen() {

    @Composable
    override fun Content(navigator: Navigator) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(24.dp),
        ) {
            Text(
                text = "Dialog",
                style = MaterialTheme.typography.headlineSmall,
            )

            Text(
                text = "The dialog container keeps the card away from the system bars and the keyboard.",
            )

            SampleTextField(
                label = "Name",
            )

            Button(
                onClick = navigator::pop,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text(
                    text = "OK",
                )
            }
        }
    }
}
