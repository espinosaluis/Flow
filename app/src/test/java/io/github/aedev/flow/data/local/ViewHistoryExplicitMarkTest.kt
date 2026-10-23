package io.github.aedev.flow.data.local

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.github.aedev.flow.data.subscriptions.SubscriptionWatchedVideos
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.ConscryptMode

@RunWith(AndroidJUnit4::class)
@Config(sdk = [34], application = Application::class)
@ConscryptMode(ConscryptMode.Mode.OFF)
class ViewHistoryExplicitMarkTest {
    @Test
    fun `manual mark hides a subscription video while playback history is paused`() =
        runBlocking {
            val context = ApplicationProvider.getApplicationContext<Application>()
            val preferences = PlayerPreferences(context)
            val history = ViewHistory.getInstance(context)
            val videoId = "paused-mark-${System.nanoTime()}"
            preferences.setWatchHistoryPaused(true)
            preferences.setHideWatchedVideosFromSubscriptions(true)

            try {
                history.savePlaybackPosition(videoId, 30_000L, 30_000L)
                assertThat(history.getWatchProgress(videoId)).isNull()

                history.savePlaybackPosition(
                    videoId = videoId,
                    position = 30_000L,
                    duration = 30_000L,
                    explicitlyMarkedWatched = true,
                )

                val watched = SubscriptionWatchedVideos(history, preferences, AppDatabase.getDatabase(context))
                assertThat(watched.ids.first()).contains(videoId)
            } finally {
                history.clearVideoHistory(videoId)
                preferences.setHideWatchedVideosFromSubscriptions(false)
                preferences.setWatchHistoryPaused(false)
            }
        }
}
