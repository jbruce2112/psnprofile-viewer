package com.bruce32.psnprofileviewer.trophylist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bruce32.psnprofileviewer.common.ListItemAdapter
import com.bruce32.psnprofileviewer.databinding.FragmentTrophyListBinding
import kotlinx.coroutines.launch

class TrophyListFragment : Fragment() {

    private val args: TrophyListFragmentArgs by navArgs()
    private val viewModel: TrophyListViewModel by viewModels {
        TrophyListViewModelFactory(args.gameId)
    }

    private var _binding: FragmentTrophyListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding is null"
        }

    private val adapter = ListItemAdapter(emptyList())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTrophyListBinding.inflate(inflater, container, false)

        binding.listRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.listRecyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.trophies.collect {
                Log.d("TrophyList", "${args.gameId} updated with ${it.size} trophies")
                adapter.update(it)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
