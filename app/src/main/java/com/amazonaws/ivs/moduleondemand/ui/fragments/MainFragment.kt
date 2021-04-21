package com.amazonaws.ivs.moduleondemand.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.amazonaws.ivs.moduleondemand.R
import com.amazonaws.ivs.moduleondemand.databinding.FragmentMainBinding
import timber.log.Timber

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loadPlayerDemoButton.setOnClickListener {
            Timber.d("Launching player demo")
            activity?.findNavController(R.id.navigation_host)?.navigate(R.id.player_fragment)
        }
    }
}
