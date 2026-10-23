package io.github.aedev.flow.ui.screens.subscriptions

import com.google.common.truth.Truth.assertThat
import io.github.aedev.flow.data.model.Video
import org.junit.Test

class SubscriptionPlayerMetadataTest {
    @Test
    fun `player metadata does not replace the feed title with a translated title`() {
        val feedVideo =
            Video(
                id = "video",
                title = "Original title",
                channelName = "Channel",
                channelId = "channel",
                thumbnailUrl = "",
                duration = 0,
                viewCount = 0,
                uploadDate = "",
            )
        val playerVideo = feedVideo.copy(title = "Translated title", duration = 240, viewCount = 100)

        val result = feedVideo.withPlayerMetadata(playerVideo)

        assertThat(result.title).isEqualTo("Original title")
        assertThat(result.duration).isEqualTo(240)
        assertThat(result.viewCount).isEqualTo(100)
    }
}
