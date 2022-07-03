package com.bruce32.psnprofileviewer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bruce32.psnprofileviewer.api.CompleteGame
import com.bruce32.psnprofileviewer.api.PSNProfileService
import com.bruce32.psnprofileviewer.api.PSNProfileServiceImpl
import com.bruce32.psnprofileviewer.databinding.FragmentGameListBinding
import com.bruce32.psnprofileviewer.databinding.FragmentTrophyListBinding
import kotlinx.coroutines.launch


class TrophyListFragment : Fragment() {

    private val service: PSNProfileService = PSNProfileServiceImpl()
    private val adapter = TrophyListAdapter(CompleteGame(trophies = emptyList()))

    private val args: TrophyListFragmentArgs by navArgs()

    private var _binding: FragmentTrophyListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding is null"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTrophyListBinding.inflate(inflater, container, false)

        binding.listRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.listRecyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            val game = service.game(args.gameId, args.userName)
            adapter.update(game)
        }

        return binding.root
    }
}
