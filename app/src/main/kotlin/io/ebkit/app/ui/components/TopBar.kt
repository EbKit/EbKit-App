package io.ebkit.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ebkit.app.IViewFactory

import io.ebkit.app.ui.theme.EbKitTheme
import io.ebkit.app.ui.utils.rememberCapsulePadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MPTopBar(
    modifier: Modifier = Modifier,
    factory: IViewFactory? = null,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        CenterAlignedTopAppBar(
            modifier = Modifier.fillMaxWidth(),
            title = {
                Text(
                    text = "应用",
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
            },
            navigationIcon = {
                Box(
                    modifier = Modifier
                        .padding(start = 12.dp) // TopAppBar 自带 4dp 左边距
                        .width(width = 87.dp)
                        .height(height = 32.dp)
                        .clip(shape = RoundedCornerShape(size = 20.dp))
                        .background(Color(color = 0xff434056))
                        .clickable(onClick = {}),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        modifier = Modifier.wrapContentSize(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                            modifier = Modifier.size(size = 20.dp),
                            tint = Color(color = 0xff8E8E9E)
                        )
                        Text(
                            text = "搜索",
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(start = 6.dp),
                            fontSize = 13.sp,
                            color = Color(color = 0xff8E8E9E),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            },
            actions = {
                Spacer(
                    modifier = Modifier.padding(
                        paddingValues = rememberCapsulePadding(
                            factory = factory,
                            excess = 4.dp, // TopAppBar 自带 4dp 右边距
                        ),
                    ),
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Transparent,
            ),
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 0.5.dp,
            color = Color(color = 0x22000000)
        )
    }
}

@Preview
@Composable
fun MPTopBarPreview() {
    EbKitTheme {
        MPTopBar()
    }
}
