package com.amazonaws.ivs.playerdemo.ui

import android.os.Bundle
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.amazonaws.ivs.moduleondemand.common.setVisible
import com.amazonaws.ivs.moduleondemand.common.showSnackBar
import com.amazonaws.ivs.playerdemo.common.collect
import com.amazonaws.ivs.playerdemo.common.launchUI
import com.amazonaws.ivs.playerdemo.common.onReady
import com.amazonaws.ivs.playerdemo.common.zoomToFit
import com.amazonaws.ivs.playerdemo.databinding.FragmentPlayerBinding
import com.amazonaws.ivs.playerdemo.ui.viewmodels.PlayerViewModel
import kotlinx.coroutines.delay

private val MEASURE_REPEAT_COUNT = (0..3).count()
private const val MEASURE_REPEAT_DELAY = 200L

class PlayerFragment : Fragment() {

    private lateinit var binding: FragmentPlayerBinding
    private val viewModel by activityViewModels<PlayerViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPlayerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var currentScreenSize = Size(0, 0)
        binding.root.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            val width = binding.mainContent.measuredWidth
            val height = binding.mainContent.measuredHeight
            if (currentScreenSize.width != width || currentScreenSize.height != height) {
                currentScreenSize = Size(width, height)
                remeasureSurface()
            }
        }

        collect(viewModel.onLoading) { loading ->
            binding.playerLoading.setVisible(loading)
        }

        collect(viewModel.onSizeChanged) { size ->
            if (size == null) return@collect
            fitSurface(size)
        }

        collect(viewModel.onError) { error ->
            binding.root.showSnackBar(error)
        }

        binding.playerSurface.onReady { surface ->
            viewModel.playMedia(requireContext(), surface)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
    }

    private fun remeasureSurface() = launchUI {
        repeat(MEASURE_REPEAT_COUNT) {
            binding.root.doOnLayout {
                viewModel.onSizeChanged.value?.let { size ->
                    fitSurface(size)
                }
            }
            delay(MEASURE_REPEAT_DELAY)
        }
    }

    private fun fitSurface(size: Size) {
        val decorView = activity?.window?.decorView ?: return

        binding.playerSurface.onReady {
            binding.playerSurface.zoomToFit(size, decorView)
        }
    }
}
