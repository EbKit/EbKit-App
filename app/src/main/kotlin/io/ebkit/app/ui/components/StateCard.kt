package io.ebkit.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.ebkit.app.ui.theme.EbKitTheme
import io.ebkit.app.ui.utils.NoOnClick

enum class StateCardStyle {
    Normal, NotInstalled,
}

@Composable
fun StateCard(
    modifier: Modifier = Modifier,
    style: StateCardStyle = StateCardStyle.Normal,
    navToInstaller: () -> Unit = NoOnClick,
    showAppsLayer: () -> Unit = NoOnClick,
) {
    Card(
        onClick = {
            navToInstaller()
        },
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 24.dp,
                    horizontal = 24.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.Error,
                contentDescription = null,
            )
            Column(
                modifier = Modifier
                    .weight(weight = 2f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = "EcosedKit未安装",
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(height = 4.dp))
                Text(
                    text = "点此安装",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Button(
                onClick = showAppsLayer,
            ) {
                Text(text = "应用")
            }
        }
    }
}

@Preview
@Composable
private fun StateCardPreview() {
    EbKitTheme {
        StateCard()
    }
}