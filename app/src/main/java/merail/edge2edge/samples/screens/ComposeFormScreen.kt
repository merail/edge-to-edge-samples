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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import merail.edge2edge.samples.R
import merail.edge2edge.samples.insets.systemBarsScreenPadding
import merail.edge2edge.samples.navigation.ComposeScreen
import merail.edge2edge.samples.navigation.Navigator

object ComposeFormScreen : ComposeScreen() {

    @Composable
    override fun Content(navigator: Navigator) {
        FormContent(
            title = "Compose screen",
            onDone = navigator::pop,
        )
    }
}

/**
 * A form that does not know where it is shown: as a full screen,
 * in a bottom sheet or inside another screen
 */
@Composable
fun FormContent(
    title: String,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.screen_background))
            // After the background, so that it goes under the system bars,
            // and before the scroll and inner paddings
            .systemBarsScreenPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
        )

        repeat(8) { index ->
            SampleTextField(
                label = "Field ${index + 1}",
            )
        }

        Button(
            onClick = onDone,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(
                text = "Done",
            )
        }
    }
}

@Composable
fun SampleTextField(
    label: String,
    modifier: Modifier = Modifier,
) {
    var text by rememberSaveable {
        mutableStateOf("")
    }

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
        },
        label = {
            Text(
                text = label,
            )
        },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth(),
    )
}
