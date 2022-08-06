package com.bruce32.psnprofileviewer.trophylist

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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bruce32.psnprofileviewer.common.ListItemAdapterSource
import com.bruce32.psnprofileviewer.common.ListItemAdapterSourceImpl
import com.bruce32.psnprofileviewer.databinding.FragmentTrophyListBinding
import kotlinx.coroutines.launch

class TrophyListFragment(
    private val viewModelFactorySource: TrophyListViewModelFactorySource = TrophyListViewModelFactorySourceImpl(),
    adapterSource: ListItemAdapterSource = ListItemAdapterSourceImpl()
) : Fragment() {

    private val args: TrophyListFragmentArgs by navArgs()
    private val viewModel: TrophyListViewModel by viewModels {
        viewModelFactorySource.factory(args.gameId)
    }

    private var _binding: FragmentTrophyListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding is null"
        }

    private val adapter = adapterSource.adapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTrophyListBinding.inflate(inflater, container, false)

        binding.listRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.listRecyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                observeViewModelUpdates()
            }
        }
        return binding.root
    }

    private suspend fun observeViewModelUpdates() {
        viewModel.trophies.collect {
            when (it) {
                is TrophyListUpdate.Loading -> showProgressBarAndHideRecyclerView()
                is TrophyListUpdate.Items -> updateAdapterAndHideProgressBar(it.viewModels)
            }
        }
    }

    private fun showProgressBarAndHideRecyclerView() {
        binding.progressBar.visibility = View.VISIBLE
        binding.listRecyclerView.visibility = View.GONE
    }

    private fun updateAdapterAndHideProgressBar(viewModels: List<TrophyViewModel>) {
        Log.d("TrophyList", "${args.gameId} updated with ${viewModels.size} trophies")
        adapter.submitList(viewModels)
        binding.listRecyclerView.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
