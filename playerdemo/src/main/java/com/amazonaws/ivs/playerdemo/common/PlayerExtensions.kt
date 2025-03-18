package com.amazonaws.ivs.playerdemo.common

import android.graphics.SurfaceTexture
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.doOnLayout
import com.amazonaws.ivs.player.Cue
import com.amazonaws.ivs.player.Player
import com.amazonaws.ivs.player.PlayerException
import com.amazonaws.ivs.player.Quality
import java.nio.ByteBuffer

inline fun Player.setListener(
    crossinline onAnalyticsEvent: (key: String, value: String) -> Unit = { _,_ -> },
    crossinline onRebuffering: () -> Unit = {},
    crossinline onSeekCompleted: (value: Long) -> Unit = { _ -> },
    crossinline onQualityChanged: (quality: Quality) -> Unit = { _ -> },
    crossinline onVideoSizeChanged: (width: Int, height: Int) -> Unit = { _,_ -> },
    crossinline onCue: (cue: Cue) -> Unit = { _ -> },
    crossinline onDurationChanged: (duration: Long) -> Unit = { _ -> },
    crossinline onStateChanged: (state: Player.State) -> Unit = { _ -> },
    crossinline onError: (exception: PlayerException) -> Unit = { _ -> },
    crossinline onMetadata: (data: String, buffer: ByteBuffer) -> Unit = { _, _ -> }
): Player.Listener {
    val listener = playerListener(
        onAnalyticsEvent, onRebuffering, onSeekCompleted, onQualityChanged, onVideoSizeChanged,
        onCue, onDurationChanged, onStateChanged, onError, onMetadata
    )

    addListener(listener)
    return listener
}

inline fun playerListener(
    crossinline onAnalyticsEvent: (key: String, value: String) -> Unit = { _,_ -> },
    crossinline onRebuffering: () -> Unit = {},
    crossinline onSeekCompleted: (value: Long) -> Unit = { _ -> },
    crossinline onQualityChanged: (quality: Quality) -> Unit = { _ -> },
    crossinline onVideoSizeChanged: (width: Int, height: Int) -> Unit = { _,_ -> },
    crossinline onCue: (cue: Cue) -> Unit = { _ -> },
    crossinline onDurationChanged: (duration: Long) -> Unit = { _ -> },
    crossinline onStateChanged: (state: Player.State) -> Unit = { _ -> },
    crossinline onError: (exception: PlayerException) -> Unit = { _ -> },
    crossinline onMetadata: (data: String, buffer: ByteBuffer) -> Unit = { _, _ -> }
): Player.Listener = object : Player.Listener() {
    // Indicates that a video analytics tracking event occurred.
    override fun onAnalyticsEvent(key: String, value: String) = onAnalyticsEvent(key, value)
    // Indicates that the player is buffering from a previous PLAYING state.
    override fun onRebuffering() = onRebuffering()
    // Indicates that the player has seeked to a given position as requested from seekTo(long).
    override fun onSeekCompleted(value: Long) = onSeekCompleted(value)
    // Indicates that the playing quality changed either from a user action or from an internal adaptive quality switch.
    override fun onQualityChanged(quality: Quality) = onQualityChanged(quality)
    // Indicates that the video dimensions changed.
    override fun onVideoSizeChanged(width: Int, height: Int) = onVideoSizeChanged(width, height)
    // Indicates that a timed cue was received.
    override fun onCue(cue: Cue) = onCue(cue)
    // Indicates that source duration changed
    override fun onDurationChanged(duration: Long) = onDurationChanged(duration)
    // Indicates that the player state changed.
    override fun onStateChanged(state: Player.State) = onStateChanged(state)
    // Indicates that an error occurred.
    override fun onError(exception: PlayerException) = onError(exception)
    // Indicates that a metadata event occurred.
    override fun onMetadata(data: String, buffer: ByteBuffer) = onMetadata(data, buffer)
}

fun TextureView.onReady(onReady: (surface: Surface) -> Unit) {
    if (surfaceTexture != null) {
        onReady(Surface(surfaceTexture))
        return
    }
    surfaceTextureListener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            surfaceTextureListener = null
            onReady(Surface(surfaceTexture))
        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
            /* Ignored */
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture) = false

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
            /* Ignored */
        }
    }
}

fun View.zoomToFit(videoSize: Size, decorView: View) {
    decorView.doOnLayout { useToScale ->
        val viewWidth = useToScale.measuredWidth
        val viewHeight = useToScale.measuredHeight
        val size = calculateSurfaceSize(viewWidth, viewHeight, videoSize)
        layoutParams = FrameLayout.LayoutParams(size.width, size.height)
    }
}

private fun calculateSurfaceSize(surfaceWidth: Int, surfaceHeight: Int, videoSize: Size): Size {
    val ratioHeight = videoSize.height.toFloat() / videoSize.width.toFloat()
    val ratioWidth = videoSize.width.toFloat() / videoSize.height.toFloat()
    val isPortrait = videoSize.width < videoSize.height
    val calculatedHeight = if (isPortrait) (surfaceWidth / ratioWidth).toInt() else (surfaceWidth * ratioHeight).toInt()
    val calculatedWidth = if (isPortrait) (surfaceHeight / ratioHeight).toInt() else (surfaceHeight * ratioWidth).toInt()
    return if (calculatedWidth >= surfaceWidth) {
        Size(calculatedWidth, surfaceHeight)
    } else {
        Size(surfaceWidth, calculatedHeight)
    }
}
