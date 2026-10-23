package io.github.aedev.flow.player.sabr.integration

import com.google.common.truth.Truth.assertThat
import io.github.aedev.flow.innertube.models.response.PlayerResponse.StreamingData.Format
import org.junit.Test

class SabrDownloadAudioSelectionTest {
    @Test
    fun `shared itag is narrowed to the requested Spanish track`() {
        val english = audio("en.4")
        val spanish = audio("es.3")

        assertThat(SabrUrlResolver.preferredAudioFormats(listOf(english, spanish), "es.3", "es"))
            .containsExactly(spanish)
    }

    @Test
    fun `missing requested language is not replaced by English`() {
        assertThat(SabrUrlResolver.preferredAudioFormats(listOf(audio("en.4")), "es.3", "es")).isEmpty()
    }

    private fun audio(trackId: String) =
        Format(
            itag = 140,
            url = null,
            mimeType = "audio/mp4; codecs=\"mp4a.40.2\"",
            bitrate = 128_000,
            width = null,
            height = null,
            contentLength = null,
            quality = "medium",
            fps = null,
            qualityLabel = null,
            averageBitrate = null,
            audioQuality = null,
            approxDurationMs = null,
            audioSampleRate = null,
            audioChannels = null,
            loudnessDb = null,
            lastModified = null,
            signatureCipher = null,
            audioTrack = Format.AudioTrack(trackId, trackId),
        )
}
