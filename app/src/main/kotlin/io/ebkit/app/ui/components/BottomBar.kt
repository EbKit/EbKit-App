package io.ebkit.app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.ebkit.app.R
import io.ebkit.app.ui.theme.EbKitTheme

@Composable
fun MPBottomBar(
    modifier: Modifier = Modifier,
    popBackStack: () -> Unit,
) {
    BottomAppBar(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxWidth()
            .clip(
                shape = RoundedCornerShape(
                    topStart = 10.dp,
                    topEnd = 10.dp,
                ),
            ),
        actions = {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White,
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                    Text(text = "Back")
                },
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                    )
                },
                onClick = popBackStack,
                modifier = Modifier.wrapContentSize(),
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            )
        },
        containerColor = Color(color = 0xff787493),
    )
}

@Preview
@Composable
fun MPBottomBarPreview() {
    EbKitTheme {
        MPBottomBar(
            popBackStack = {},
        )
    }
}
