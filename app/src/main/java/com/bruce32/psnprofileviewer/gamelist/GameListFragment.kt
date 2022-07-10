package com.bruce32.psnprofileviewer.gamelist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bruce32.psnprofileviewer.databinding.FragmentGameListBinding
import com.bruce32.psnprofileviewer.model.Game
import kotlinx.coroutines.launch

class GameListFragment() : Fragment() {

    private val viewModel: GameListViewModel by viewModels()

    private var _binding: FragmentGameListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding is null"
        }

    private lateinit var adapter: GameListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameListBinding.inflate(inflater, container, false)
        binding.listRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = GameListAdapter(emptyList()) { gameId ->
            findNavController().navigate(
                GameListFragmentDirections.showTrophyList(gameId)
            )
        }
        binding.listRecyclerView.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("GameList", "onViewCreated")

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                refreshGameListAndObserve()
            }
        }
    }

    private suspend fun refreshGameListAndObserve() {
        viewModel.items.collect {
            Log.d("GameList", "game list updated with ${it.size} games")
            updateAdapter(it)
        }
    }

    private fun updateAdapter(viewModels: List<GameViewModel>) {
        adapter.update(viewModels)
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
