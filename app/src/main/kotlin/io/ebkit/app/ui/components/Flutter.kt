package io.ebkit.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import io.ebkit.app.IViewFactory
import io.ebkit.app.ui.theme.EbKitTheme

@Composable
fun Flutter(
    modifier: Modifier = Modifier,
    factory: IViewFactory? = null,
) {
    val inspection: Boolean = LocalInspectionMode.current
    val count = remember { mutableIntStateOf(value = 0) }
    Surface(
        modifier = modifier.fillMaxSize(),
        shape = MaterialTheme.shapes.medium,
    ) {
        when {
            inspection -> Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = MaterialTheme.shapes.medium),
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            count.intValue++
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                        )
                    }
                },
                containerColor = Color.Transparent,
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues = innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        modifier = Modifier.wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "You have pushed the button this many times:")
                        Text(
                            text = count.intValue.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                        )
                    }
                }
            }

            else -> ViewFactory(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = MaterialTheme.shapes.medium),
                factory = factory,
            ) {
                getFlutterView
            }
        }
    }
}

@Preview
@Composable
private fun FlutterPreview() {
    EbKitTheme {
        Flutter()
    }
}