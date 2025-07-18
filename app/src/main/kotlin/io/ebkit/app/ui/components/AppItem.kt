package io.ebkit.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ebkit.app.R
import io.ebkit.app.ui.theme.EbKitTheme

enum class AppItemStyle {
    Image, Icon,
}

@Composable
fun AppItem(
    modifier: Modifier = Modifier,
    onLaunch: () -> Unit = {},
    style: AppItemStyle,
    appIcon: Painter,
    appName: String,
) {
    Column(
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(size = 60.dp)
                .clip(shape = RoundedCornerShape(size = 35.dp))
                .background(color = Color(color = 0xff434056))
                .clickable(onClick = onLaunch),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = appIcon,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = when (style) {
                    AppItemStyle.Image -> Modifier
                        .fillMaxSize()
                        .clip(
                            shape = RoundedCornerShape(
                                size = 35.dp,
                            ),
                        )

                    AppItemStyle.Icon -> Modifier.size(
                        size = 30.dp,
                    )
                }
            )
        }
        Text(
            text = appName,
            fontSize = 15.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun AppItemPreview() {
    EbKitTheme {
        AppItem(
            style = AppItemStyle.Icon,
            appIcon = painterResource(id = R.drawable.baseline_preview_24),
            appName = "Preview",
        )
    }
}