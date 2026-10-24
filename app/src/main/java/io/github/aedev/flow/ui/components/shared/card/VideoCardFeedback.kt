package io.github.aedev.flow.ui.components.shared.card

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.aedev.flow.R

@Composable
internal fun VideoCardFeedback(
    state: VideoCardState,
    actions: VideoCardActions,
    modifier: Modifier = Modifier,
) {
    val haptics = LocalHapticFeedback.current
    val watched = state.isWatched
    OutlinedToggleButton(
        checked = watched,
        onCheckedChange = {
            if (!watched) {
                haptics.performHapticFeedback(HapticFeedbackType.ToggleOn)
                actions.onWatched(state.video)
            }
        },
        contentPadding = ButtonDefaults.ExtraSmallContentPadding,
        modifier = modifier.heightIn(min = ButtonDefaults.ExtraSmallContainerHeight).widthIn(min = 48.dp),
    ) {
        Icon(
            imageVector = if (watched) Icons.Filled.Visibility else Icons.Outlined.Visibility,
            contentDescription = null,
            modifier = Modifier.size(ButtonDefaults.ExtraSmallIconSize),
        )
        Spacer(Modifier.width(ButtonDefaults.ExtraSmallIconSpacing))
        Text(
            text = stringResource(if (watched) R.string.quick_action_watched else R.string.mark_as_watched),
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
