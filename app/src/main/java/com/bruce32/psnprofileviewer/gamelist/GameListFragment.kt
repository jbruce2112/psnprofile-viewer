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
import com.bruce32.psnprofileviewer.common.ListItemAdapter
import com.bruce32.psnprofileviewer.databinding.FragmentGameListBinding
import kotlinx.coroutines.launch

class GameListFragment(
    private val viewModelFactory: GameListViewModelFactory = GameListViewModelFactory()
): Fragment() {

    private val viewModel: GameListViewModel by viewModels {
        viewModelFactory
    }

    private var _binding: FragmentGameListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding is null"
        }

    private lateinit var adapter: ListItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameListBinding.inflate(inflater, container, false)
        binding.listRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ListItemAdapter(emptyList()) { gameId ->
            findNavController().navigate(
                GameListFragmentDirections.showTrophyList(gameId)
            )
        }
        binding.listRecyclerView.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                observeGameViewModelUpdate()
            }
        }
    }

    private suspend fun observeGameViewModelUpdate() {
        viewModel.items.collect {
            when (it) {
                is GameListUpdate.Empty -> setMessageAndHideRecyclerView(it.message)
                is GameListUpdate.Items -> updateAdapterAndHideMessage(it.viewModels)
            }
        }
    }

    private fun setMessageAndHideRecyclerView(message: String) {
        binding.messageView.text = message
        binding.listRecyclerView.visibility = View.GONE
        binding.messageView.visibility = View.VISIBLE
    }

    private fun updateAdapterAndHideMessage(viewModels: List<GameViewModel>) {
        Log.d("GameList", "game list updated with ${viewModels.size} games")
        adapter.update(viewModels)
        binding.listRecyclerView.visibility = View.VISIBLE
        binding.messageView.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
