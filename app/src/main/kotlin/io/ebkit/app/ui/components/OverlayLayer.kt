package io.ebkit.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.ebkit.app.IViewFactory
import io.ebkit.app.ui.theme.EbKitTheme
import io.ebkit.app.ui.theme.actionBarExpandedHeight
import io.ebkit.app.ui.theme.capsuleHeight
import io.ebkit.app.ui.theme.capsuleRadius
import io.ebkit.app.ui.theme.capsuleRightPadding
import io.ebkit.app.ui.theme.capsuleWidth
import io.ebkit.app.ui.utils.NoOnClick

@Composable
fun OverlayLayer(
    modifier: Modifier = Modifier,
    factory: IViewFactory? = null,
    content: @Composable BoxScope.() -> Unit = {},
) {
    val inspection: Boolean = LocalInspectionMode.current
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            content = content,
        )
        when {
            inspection -> Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.TopEnd,
            ) {
                Row(
                    modifier = Modifier
                        .padding(
                            top = ((actionBarExpandedHeight - capsuleHeight) / 2),
                            end = capsuleRightPadding,
                        )
                        .systemBarsPadding()
                        .height(height = capsuleHeight)
                        .width(width = capsuleWidth)
                        .clip(shape = RoundedCornerShape(size = capsuleRadius))
                        .background(color = Color(color = 0xff434056)),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .weight(weight = 1f)
                            .fillMaxSize()
                            .clickable(onClick = NoOnClick),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreHoriz,
                            contentDescription = null,
                            tint = Color(color = 0xff8E8E9E),
                        )
                    }
                    VerticalDivider(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .wrapContentWidth()
                            .fillMaxHeight(),
                        color = Color(color = 0xff8E8E9E),
                    )
                    Box(
                        modifier = Modifier
                            .weight(weight = 1f)
                            .fillMaxSize()
                            .clickable(onClick = NoOnClick),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null,
                            tint = Color(color = 0xff8E8E9E),
                        )
                    }
                }
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
private fun OverlayLayerPreview() {
    EbKitTheme {
        OverlayLayer()
    }
}