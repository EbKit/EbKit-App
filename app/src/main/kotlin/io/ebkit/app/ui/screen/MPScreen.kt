package io.ebkit.app.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import io.ebkit.app.IViewFactory
import io.ebkit.app.ui.components.MPBottomBar
import io.ebkit.app.ui.components.MPTopBar
import io.ebkit.app.ui.components.OverlayLayer
import io.ebkit.app.ui.page.FlutterPage
import io.ebkit.app.ui.page.MPAppsPage
import io.ebkit.app.ui.theme.EbKitTheme
import io.ebkit.app.ui.utils.NoOnClick
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data object AppsPage

@Serializable
data object FlutterPage

private data class MPPage<T>(
    val page: Int,
    val route: T,
)

private val pages = arrayListOf(
    MPPage(
        page = 0,
        route = AppsPage,
    ),
    MPPage(
        page = 1,
        route = FlutterPage,
    ),
)

@Composable
fun MPScreen(
    modifier: Modifier = Modifier,
    factory: IViewFactory? = null,
    popBackStack: () -> Unit = NoOnClick,
    initialPage: Int = getPageWithRoute(
        route = AppsPage,
    ),
) {

    val pageState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { pages.size },
    )
    val coroutineScope = rememberCoroutineScope()
    OverlayLayer(factory = factory) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                MPTopBar(factory = factory)
            },
            bottomBar = {
                MPBottomBar(popBackStack = popBackStack)
            },
            containerColor = Color(color = 0xff1B1B2B),
        ) { innerPadding ->
            HorizontalPager(
                state = pageState,
                modifier = Modifier.padding(
                    paddingValues = innerPadding,
                ),
                userScrollEnabled = false,
            ) { page ->
                when (pages[page].route) {
                    AppsPage -> MPAppsPage(
                        popBackStack = popBackStack,
                        animateToFlutter = {
                            coroutineScope.launch {
                                pageState.animateScrollToPage(
                                    page = getPageWithRoute(
                                        route = FlutterPage,
                                    ),
                                )
                            }
                        },
                    )

                    FlutterPage -> FlutterPage(
                        factory = factory,
                        animateToApps = {
                            coroutineScope.launch {
                                pageState.animateScrollToPage(
                                    page = getPageWithRoute(
                                        route = AppsPage,
                                    ),
                                )
                            }
                        },
                    )

                    else -> Box(
                        modifier = modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "未知页面",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        )
                    }
                }
            }
        }
    }
}

private fun <T> getPageWithRoute(route: T): Int {
    var result = 0
    for (page in pages) {
        if (page.route == route) {
            result = page.page
        }
    }
    return result
}

@Preview
@Composable
private fun MPScreenAppsPagePreview() {
    EbKitTheme {
        MPScreen(
            initialPage = getPageWithRoute(
                route = AppsPage,
            ),
        )
    }
}

@Preview
@Composable
private fun MPScreenFlutterPagePreview() {
    EbKitTheme {
        MPScreen(
            initialPage = getPageWithRoute(
                route = FlutterPage,
            ),
        )
    }
}