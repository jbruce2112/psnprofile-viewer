package com.bruce32.psnprofileviewer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bruce32.psnprofileviewer.databinding.FragmentGameListBinding
import com.bruce32.psnprofileviewer.model.Game
import kotlinx.coroutines.launch

class GameListFragment(
    private val repository: ProfileRepository = ProfileRepository()
) : Fragment() {

    private var _binding: FragmentGameListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding is null"
        }

    private val adapter
        get() = binding.root.adapter as GameListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameListBinding.inflate(inflater, container, false)

        binding.listRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.listRecyclerView.adapter = GameListAdapter(emptyList()) { }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                refreshGameListAndObserve("jbruce2112")
            }
        }
    }

    private suspend fun refreshGameListAndObserve(psnId: String) {
        // TODO: why does order matter here. collect doesn't return?
        repository.refreshProfileAndGames(psnId)
        repository.games.collect {
            Log.d("GameListFragment", "got ${it.size} games from collect")
            reconfigureListAdapter(it, psnId)
        }
    }

    private fun reconfigureListAdapter(games: List<Game>, psnId: String) {
        binding.listRecyclerView.adapter = GameListAdapter(games) { id ->
            findNavController().navigate(
                GameListFragmentDirections.showTrophyList(
                    id,
                    psnId
                )
            )
        }
    }
}
