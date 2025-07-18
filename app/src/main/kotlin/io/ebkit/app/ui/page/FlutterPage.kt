package io.ebkit.app.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.ebkit.app.IViewFactory
import io.ebkit.app.R
import io.ebkit.app.ui.components.ActionBar
import io.ebkit.app.ui.components.Flutter
import io.ebkit.app.ui.theme.EbKitTheme
import io.ebkit.app.ui.utils.NoOnClick

@Composable
fun FlutterPage(
    modifier: Modifier = Modifier,
    factory: IViewFactory? = null,
    animateToApps: () -> Unit = NoOnClick,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        ActionBar(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 8.dp,
            ),
            factory = factory,
            title = {
                Text(
                    text = stringResource(
                        id = R.string.app_name,
                    ),
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = animateToApps,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                    )
                }
            },
        )
        Flutter(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 16.dp,
            ),
            factory = factory,
        )
    }
}

@Preview
@Composable
private fun FlutterPagePreview() {
    EbKitTheme {
        FlutterPage()
    }
}