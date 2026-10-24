package io.github.aedev.flow.ui.components.shared.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.aedev.flow.R

@Composable
internal fun VideoCardInlineActions(
    state: VideoCardState,
    actions: VideoCardActions,
    showMenu: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        IconButton(onClick = { actions.onDownload(state.video.copy(title = state.title)) }) {
            Icon(
                imageVector = Icons.Outlined.Download,
                contentDescription = stringResource(R.string.download),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        val isWatched = state.isWatched
        IconToggleButton(
            checked = isWatched,
            onCheckedChange = { checked ->
                if (checked) actions.onWatched(state.video) else actions.onUnwatched(state.video)
            },
        ) {
            Icon(
                imageVector = if (isWatched) Icons.Filled.Visibility else Icons.Outlined.Visibility,
                contentDescription = stringResource(if (isWatched) R.string.mark_as_unwatched else R.string.mark_as_watched),
                tint = if (isWatched) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        if (showMenu) {
            IconButton(onClick = { state.sheets.showQuickActions = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.more_options),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
