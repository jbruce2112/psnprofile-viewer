package com.bruce32.psnprofileviewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bruce32.psnprofileviewer.api.PSNProfileService
import com.bruce32.psnprofileviewer.api.PSNProfileServiceImpl
import com.bruce32.psnprofileviewer.databinding.FragmentGameListBinding
import kotlinx.coroutines.launch

class GameListFragment : Fragment() {

    private val service: PSNProfileService = PSNProfileServiceImpl()

    private var _binding: FragmentGameListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding is null"
        }

    private val adapter
        get() = binding.root.adapter as? GameListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameListBinding.inflate(inflater, container, false)

        binding.listRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.listRecyclerView.adapter = GameListAdapter(emptyList()) { }

        viewLifecycleOwner.lifecycleScope.launch {
            val profile = service.profile("jbruce2112")
            adapter?.update(profile.games)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                binding.listRecyclerView.adapter = GameListAdapter(emptyList()) { href ->
                    val components = href.split("/").filter { it.isNotBlank() }
                    findNavController().navigate(
                        GameListFragmentDirections.showTrophyList(
                            components[1],
                            components[2]
                        )
                    )
                }
            }
        }
    }
}
