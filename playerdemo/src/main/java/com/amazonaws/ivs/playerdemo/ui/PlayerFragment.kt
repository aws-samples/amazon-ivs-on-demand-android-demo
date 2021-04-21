package com.amazonaws.ivs.playerdemo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.amazonaws.ivs.moduleondemand.App
import com.amazonaws.ivs.moduleondemand.common.setVisible
import com.amazonaws.ivs.moduleondemand.common.showSnackBar
import com.amazonaws.ivs.playerdemo.BuildConfig
import com.amazonaws.ivs.playerdemo.common.lazyViewModel
import com.amazonaws.ivs.playerdemo.common.zoomToFit
import com.amazonaws.ivs.playerdemo.databinding.FragmentPlayerBinding
import com.amazonaws.ivs.playerdemo.ui.viewmodels.PlayerViewModel

class PlayerFragment : Fragment() {

    private lateinit var binding: FragmentPlayerBinding
    private val viewModel: PlayerViewModel by lazyViewModel(
        { requireActivity().application as App },
        { PlayerViewModel() }
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPlayerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onLoading.observeConsumable(viewLifecycleOwner) { loading ->
            binding.playerLoading.setVisible(loading)
        }

        viewModel.onSizeChanged.observeConsumable(viewLifecycleOwner) { size ->
            activity?.windowManager?.let { windowManager ->
                binding.playerSurface.zoomToFit(windowManager, size.first, size.second)
            }
        }

        viewModel.onError.observeConsumable(viewLifecycleOwner) { error ->
            binding.root.showSnackBar(error)
        }

        viewModel.playMedia(requireContext(), binding.playerSurface.holder.surface, BuildConfig.DEMO_VIDEO)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
    }
}
