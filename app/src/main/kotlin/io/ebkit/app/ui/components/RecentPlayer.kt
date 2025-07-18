package io.ebkit.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlutterDash
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ebkit.app.ui.theme.EbKitTheme
import io.ebkit.app.ui.utils.NoOnClick

@Composable
fun RecentPlayer(
    modifier: Modifier = Modifier,
    animateToFlutter: () -> Unit = NoOnClick,
) {
    Column(
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .height(height = 60.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(size = 40.dp))
                .background(Color(color = 0xFF434056))
                .clickable(onClick = animateToFlutter),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.FlutterDash,
                    contentDescription = null,
                    modifier = Modifier.size(size = 30.dp),
                    tint = Color(color = 0xFF8E8E9E)
                )
                Text(
                    text = "暂无内容",
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 10.dp),
                    fontSize = 16.sp,
                    color = Color(color = 0xFF8E8E9E),
                    textAlign = TextAlign.Center,
                )
            }
        }
        Text(
            text = "Flutter",
            fontSize = 15.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun RecentPlayerPreview() {
    EbKitTheme {
        RecentPlayer()
    }
}