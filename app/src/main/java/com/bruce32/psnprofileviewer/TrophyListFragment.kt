package com.bruce32.psnprofileviewer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bruce32.psnprofileviewer.databinding.FragmentTrophyListBinding
import kotlinx.coroutines.launch

class TrophyListFragment(
    private val repository: ProfileRepository = ProfileRepository()
) : Fragment() {

    private val adapter = TrophyListAdapter(emptyList())

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
            repository.refreshTrophies(args.gameId, args.userName)
            repository.getTrophies(args.gameId).collect {
                Log.d("TrophyListUpdate", "${args.gameId} updated with ${it.size} trophies")
                adapter.update(it)
            }
        }

        return binding.root
    }
}
