package io.ebkit.app.ui.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ebkit.app.ui.components.AppsGrid
import io.ebkit.app.ui.components.MPPlayer
import io.ebkit.app.ui.components.miniProgramList
import io.ebkit.app.ui.theme.EbKitTheme
import io.ebkit.app.ui.utils.NoOnClick

@Composable
fun MPAppsPage(
    modifier: Modifier = Modifier,
    popBackStack: () -> Unit = NoOnClick,
    animateToFlutter: () -> Unit = NoOnClick,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState),
    ) {
        Box(
            modifier = Modifier.padding(
                start = 30.dp,
                bottom = 15.dp,
                top = 30.dp,
            ),
        ) {
            Text(
                text = "音乐和视频",
                fontSize = 14.sp,
                color = Color(
                    color = 0xFF8E8E9E,
                ),
            )
        }
        MPPlayer(
            popBackStack = popBackStack,
            animateToFlutter = animateToFlutter,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    start = 30.dp,
                    bottom = 15.dp,
                    top = 30.dp,
                    end = 30.dp,
                ),
        ) {
            Row {
                Box(
                    modifier = Modifier.weight(weight = 1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "最近使用小程序",
                        fontSize = 14.sp,
                        color = Color(color = 0xff8E8E9E),
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .wrapContentHeight(align = Alignment.CenterVertically),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "更多",
                            fontSize = 14.sp,
                            color = Color(color = 0xff8E8E9E),
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            modifier = Modifier.size(size = 16.dp),
                            tint = Color(color = 0xff8E8E9E),
                        )
                    }
                }
            }
        }
        AppsGrid(list = miniProgramList)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    start = 30.dp,
                    bottom = 15.dp,
                    top = 30.dp,
                ),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = "我的小程序",
                fontSize = 14.sp,
                color = Color(color = 0xff8E8E9E),
            )
        }
        AppsGrid(
            modifier = Modifier.padding(bottom = 16.dp),
            list = miniProgramList,
        )
    }
}

@Preview
@Composable
private fun AppsPagePreview() {
    EbKitTheme {
        MPAppsPage()
    }
}
