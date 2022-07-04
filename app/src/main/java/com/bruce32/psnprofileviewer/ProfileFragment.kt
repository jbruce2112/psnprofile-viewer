package com.bruce32.psnprofileviewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bruce32.psnprofileviewer.api.PSNProfileService
import com.bruce32.psnprofileviewer.api.PSNProfileServiceImpl
import com.bruce32.psnprofileviewer.databinding.FragmentProfileBinding
import com.bruce32.psnprofileviewer.model.Profile
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class ProfileFragment(
    private val service: PSNProfileService = PSNProfileServiceImpl()
) : Fragment() {

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
            val profile = service.profile("jbruce2112")
            bind(profile)
        }

        return binding.root
    }

    private fun bind(profile: Profile) {
        Glide.with(binding.avatarImageView)
            .load(profile.avatarURL.toString())
            .into(binding.avatarImageView)

        binding.userNameView.text = profile.psnId
        binding.trophiesValue.text = "${profile.totalPlatinum} Platinum, ${profile.totalGold} Gold, ${profile.totalSilver} Silver, ${profile.totalBronze} Bronze"
        binding.gamesPlayedValue.text = profile.gamesPlayed.toString()
        binding.percentCompleteValue.text = "${profile.completionPercent}%"
        binding.completedGamesValue.text = profile.completedGames.toString()
        binding.unearnedTrophiesValue.text = profile.unearnedTrophies.toString()
        binding.worldRankValue.text = String.format("%,d", profile.worldRank)
        binding.countryRankValue.text = String.format("%,d", profile.countryRank)
        binding.levelValue.text = profile.level.toString()
    }
}
