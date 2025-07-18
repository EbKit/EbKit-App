package io.ebkit.app.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.ebkit.app.IViewFactory
import io.ebkit.app.R
import io.ebkit.app.ui.theme.EbKitTheme
import io.ebkit.app.ui.utils.NoOnClick

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionBar(
    modifier: Modifier = Modifier,
    factory: IViewFactory? = null,
    title: @Composable () -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    val inspection: Boolean = LocalInspectionMode.current
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        TopAppBar(
            title = {
                when {
                    factory == null || inspection -> title()

                    else -> ViewFactory(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        factory = factory,
                    ) {
                        getToolbarView
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            navigationIcon = navigationIcon,
            actions = actions,
            windowInsets = WindowInsets(
                left = 0.dp,
                top = 0.dp,
                right = 0.dp,
                bottom = 0.dp,
            ),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
            ),
        )
    }
}

@Preview
@Composable
private fun ActionBarPreview() {
    EbKitTheme {
        ActionBar(
            title = {
                Text(text = stringResource(id = R.string.app_name))
            },
            navigationIcon = {
                IconButton(onClick = NoOnClick) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = null,
                    )
                }
            },
            actions = {
                IconButton(onClick = NoOnClick) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = null,
                    )
                }
            },
        )
    }
}