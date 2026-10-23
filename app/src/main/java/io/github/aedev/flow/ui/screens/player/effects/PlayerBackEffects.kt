package io.github.aedev.flow.ui.screens.player.effects

import androidx.activity.compose.BackHandler
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.aedev.flow.data.local.PlayerPreferences
import io.github.aedev.flow.player.GlobalPlayerState
import io.github.aedev.flow.ui.components.videoplayer.PlayerDraggableState
import io.github.aedev.flow.ui.components.videoplayer.PlayerSheetValue
import io.github.aedev.flow.ui.screens.player.state.PlayerScreenState
import kotlin.coroutines.cancellation.CancellationException

/**
 * Back leaves fullscreen, then scrubs the expanded sheet down with the finger and collapses it on
 * commit. While the player is touch-locked, back only reveals the lock; it is registered last so it
 * wins over both.
 */
@Composable
internal fun PlayerBackEffects(
    playerSheetState: PlayerDraggableState,
    screenState: PlayerScreenState,
    isInPipMode: Boolean,
) {
    val context = LocalContext.current
    val preferences = remember(context) { PlayerPreferences(context) }
    val autoPipEnabled by preferences.autoPipEnabled.collectAsStateWithLifecycle(initialValue = true)

    BackHandler(enabled = screenState.isFullscreen) {
        screenState.isFullscreen = false
        screenState.isFullscreenPortrait = false
    }

    // Stays enabled for the whole scrub: the sheet's currentValue flips to Collapsed halfway
    // through, and disabling the handler mid-gesture would cancel it as if the user had backed out.
    var backScrubInFlight by remember { mutableStateOf(false) }
    PredictiveBackHandler(
        enabled =
            (playerSheetState.currentValue == PlayerSheetValue.Expanded || backScrubInFlight) &&
                !isInPipMode &&
                !screenState.isFullscreen,
    ) { progress ->
        backScrubInFlight = true
        try {
            if (autoPipEnabled) {
                playerSheetState.beginBackScrub()
                progress.collect { event -> playerSheetState.scrubBack(event.progress) }
                playerSheetState.collapse()
            } else {
                progress.collect { }
                GlobalPlayerState.requestDismiss()
            }
        } catch (e: CancellationException) {
            if (autoPipEnabled) playerSheetState.expand()
            // Returning normally from here, before the flow was collected, makes activity-compose
            // throw "You must collect the progress flow" (#1307).
            throw e
        } finally {
            backScrubInFlight = false
        }
    }

    BackHandler(enabled = screenState.isTouchLocked && !isInPipMode) {
        screenState.revealLockOverlay()
        screenState.onInteraction()
    }
}
