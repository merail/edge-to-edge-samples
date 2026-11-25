package merail.edge2edge.samples.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import merail.edge2edge.samples.R
import merail.edge2edge.samples.insets.LocalApplySystemInsets
import merail.edge2edge.samples.insets.systemBarsScreenPadding
import merail.edge2edge.samples.navigation.ComposeScreen
import merail.edge2edge.samples.navigation.Navigator

/**
 * The screen applies system insets itself, and the form embedded into it
 * (the same one as in [ComposeFormScreen]) must not apply them a second time
 */
object EmbeddedContentScreen : ComposeScreen() {

    @Composable
    override fun Content(navigator: Navigator) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.card_background))
                .systemBarsScreenPadding()
                .padding(16.dp),
        ) {
            Text(
                text = "Embedded content",
                style = MaterialTheme.typography.headlineSmall,
            )

            Text(
                text = "The form below is the same one as in the \"Compose screen\" sample. " +
                    "Here it is a part of another screen, so it does not apply system insets.",
                modifier = Modifier
                    .padding(
                        vertical = 12.dp,
                    ),
            )

            val shape = RoundedCornerShape(16.dp)
            CompositionLocalProvider(
                value = LocalApplySystemInsets provides false,
            ) {
                FormContent(
                    title = "Embedded form",
                    onDone = navigator::pop,
                    modifier = Modifier
                        .weight(1f)
                        .clip(shape)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                            ),
                            shape = shape,
                        ),
                )
            }
        }
    }
}
