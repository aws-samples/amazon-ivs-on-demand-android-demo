package com.amazonaws.ivs.playerdemo.ui.viewmodels

import android.content.Context
import android.net.Uri
import android.view.Surface
import androidx.lifecycle.ViewModel
import com.amazonaws.ivs.player.MediaPlayer
import com.amazonaws.ivs.player.Player
import com.amazonaws.ivs.playerdemo.common.ConsumableSharedFlow
import com.amazonaws.ivs.playerdemo.common.setListener

class PlayerViewModel : ViewModel() {

    private var player: MediaPlayer? = null
    private var playerListener: Player.Listener? = null

    val onError = ConsumableSharedFlow<String>()
    val onSizeChanged = ConsumableSharedFlow<Pair<Int, Int>>()
    val onLoading = ConsumableSharedFlow<Boolean>()

    fun playMedia(context: Context, surface: Surface, url: String) {
        player = MediaPlayer(context)

        player?.setListener(
            onVideoSizeChanged = { width, height ->
                onSizeChanged.tryEmit(Pair(width, height))
            },
            onStateChanged = { state ->
                when (state) {
                    Player.State.BUFFERING -> onLoading.tryEmit(true)
                    Player.State.PLAYING -> onLoading.tryEmit(false)
                    else -> {}
                }
            },
            onError = { error ->
                onError.tryEmit(error.errorMessage)
            }
        )

        player?.setSurface(surface)
        player?.load(Uri.parse(url))
        player?.play()
    }

    fun release() {
        playerListener?.let { player?.removeListener(it) }
        player?.release()
        player = null
    }
}
