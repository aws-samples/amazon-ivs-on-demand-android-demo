package com.amazonaws.ivs.playerdemo.ui.viewmodels

import android.content.Context
import android.net.Uri
import android.view.Surface
import androidx.lifecycle.ViewModel
import com.amazonaws.ivs.player.MediaPlayer
import com.amazonaws.ivs.player.Player
import com.amazonaws.ivs.playerdemo.common.ConsumableLiveData
import com.amazonaws.ivs.playerdemo.common.setListener

class PlayerViewModel : ViewModel() {

    private var player: MediaPlayer? = null
    private var playerListener: Player.Listener? = null

    val onError = ConsumableLiveData<String>()
    val onSizeChanged = ConsumableLiveData<Pair<Int, Int>>()
    val onLoading = ConsumableLiveData<Boolean>()

    fun playMedia(context: Context, surface: Surface, url: String) {
        player = MediaPlayer(context)

        player?.setListener(
            onVideoSizeChanged = { width, height ->
                onSizeChanged.postConsumable(Pair(width, height))
            },
            onStateChanged = { state ->
                when (state) {
                    Player.State.BUFFERING -> onLoading.postConsumable(true)
                    Player.State.PLAYING -> onLoading.postConsumable(false)
                    else -> {}
                }
            },
            onError = { error ->
                onError.postConsumable(error.errorMessage)
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
