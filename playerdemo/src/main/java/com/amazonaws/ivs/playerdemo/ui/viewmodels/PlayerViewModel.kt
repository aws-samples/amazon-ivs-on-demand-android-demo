package com.amazonaws.ivs.playerdemo.ui.viewmodels

import android.content.Context
import android.net.Uri
import android.view.Surface
import androidx.lifecycle.ViewModel
import com.amazonaws.ivs.player.MediaPlayer
import com.amazonaws.ivs.player.Player
import com.amazonaws.ivs.playerdemo.common.setListener
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class PlayerViewModel : ViewModel() {

    private var player: MediaPlayer? = null
    private var playerListener: Player.Listener? = null

    private val _onError = Channel<String>()
    private val _onLoading = MutableStateFlow(false)
    private val _onSizeChanged = MutableSharedFlow<Pair<Int, Int>>()

    val onError = _onError.receiveAsFlow()
    val onLoading = _onLoading.asStateFlow()
    val onSizeChanged = _onSizeChanged.asSharedFlow()

    fun playMedia(context: Context, surface: Surface, url: String) {
        player = MediaPlayer(context)

        player?.setListener(
            onVideoSizeChanged = { width, height ->
                _onSizeChanged.tryEmit(Pair(width, height))
            },
            onStateChanged = { state ->
                when (state) {
                    Player.State.BUFFERING -> _onLoading.update { true }
                    Player.State.PLAYING -> _onLoading.update { false }
                    else -> {}
                }
            },
            onError = { error ->
                _onError.trySend(error.errorMessage)
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
