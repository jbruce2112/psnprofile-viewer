package com.bruce32.psnprofileviewer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bruce32.psnprofileviewer.api.PSNProfileService
import com.bruce32.psnprofileviewer.api.PSNProfileServiceImpl
import com.bruce32.psnprofileviewer.databinding.FragmentGameListBinding
import kotlinx.coroutines.launch

class GameListFragment : Fragment() {

    private val service: PSNProfileService = PSNProfileServiceImpl()
    private val adapter = GameListAdapter(emptyList()) { href ->
        println("clicked $href")
        viewLifecycleOwner.lifecycleScope.launch {
            val elems = href.split("/").filter { it.isNotBlank() }
            service.game(gameId = elems[1], userName = elems[2])
        }
    }

    private var _binding: FragmentGameListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding is null"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameListBinding.inflate(inflater, container, false)

        binding.listRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.listRecyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            val profile = service.profile("jbruce2112")
            adapter.update(profile.games)
        }

        return binding.root
    }
}