package io.ebkit.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import io.ebkit.app.IViewFactory
import io.ebkit.app.ui.theme.EbKitTheme

@Composable
fun OverlayLayer(
    modifier: Modifier = Modifier,
    factory: IViewFactory? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            content = content,
        )
        when {
            LocalInspectionMode.current -> Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {

            }

            else -> ViewFactory(
                modifier = Modifier.fillMaxSize(),
                factory = factory,
            ) {
                getOverlayView
            }
        }
    }
}

@Preview
@Composable
fun OverlayLayerPreview() {
    EbKitTheme {
        OverlayLayer {
            Text(text = "OverlayLayer")
        }
    }
}