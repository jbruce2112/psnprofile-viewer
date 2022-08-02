package com.bruce32.psnprofileviewer.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bruce32.psnprofileviewer.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    private var _binding: FragmentProfileBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding is null"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.profile.collect {
                it?.let {
                    bind(it)
                }
            }
        }

        return binding.root
    }

    private fun bind(viewModel: ProfileStatsViewModel) {
        Glide.with(binding.avatarImageView)
            .load(viewModel.imageURL.toString())
            .into(binding.avatarImageView)

        binding.userNameView.text = viewModel.id
        binding.trophiesValue.text = viewModel.trophies
        binding.gamesPlayedValue.text = viewModel.gamesPlayedValue
        binding.percentCompleteValue.text = viewModel.percentCompleteValue
        binding.completedGamesValue.text = viewModel.completedGamesValue
        binding.unearnedTrophiesValue.text = viewModel.unearnedTrophiesValue
        binding.worldRankValue.text = viewModel.worldRankValue
        binding.countryRankValue.text = viewModel.countryRankValue
        binding.levelValue.text = viewModel.levelValue
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
