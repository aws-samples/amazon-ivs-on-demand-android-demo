package com.amazonaws.ivs.moduleondemand.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.dynamicfeatures.fragment.ui.AbstractProgressFragment
import com.amazonaws.ivs.moduleondemand.R
import com.amazonaws.ivs.moduleondemand.common.showSnackBar
import com.amazonaws.ivs.moduleondemand.databinding.FragmentLoadingBinding
import timber.log.Timber
import kotlin.math.roundToInt

class LoadingFragment : AbstractProgressFragment() {

    private lateinit var binding: FragmentLoadingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoadingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCancelled() {
        binding.root.showSnackBar(getString(R.string.err_something_went_wrong))
    }

    override fun onFailed(errorCode: Int) {
        binding.root.showSnackBar(getString(R.string.err_something_went_wrong))
    }

    override fun onProgress(status: Int, bytesDownloaded: Long, bytesTotal: Long) {
        try {
            val progress = (bytesDownloaded.toFloat() / bytesTotal.toFloat() * 100f).roundToInt()
            Timber.d("Install progress: $progress, ${bytesDownloaded.toFloat() / bytesTotal.toFloat()}, ${bytesDownloaded.toFloat()}, ${bytesTotal.toFloat()}")
            binding.loadPlayerDemoProgress.progress = progress
        } catch (e: Exception) {
            Timber.d("Failed to get progress")
        }
    }
}
