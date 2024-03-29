package com.bruce32.psnprofileviewer.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bruce32.psnprofileviewer.common.GlideImageLoader
import com.bruce32.psnprofileviewer.common.ImageLoader
import com.bruce32.psnprofileviewer.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch

class ProfileFragment(
    private val viewModelFactorySource: ProfileViewModelFactorySource = ProfileViewModelFactorySourceImpl(),
    private val imageLoader: ImageLoader = GlideImageLoader()
) : Fragment() {

    private val viewModel: ProfileViewModel by viewModels {
        viewModelFactorySource.factory(requireContext())
    }

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
            viewModel.profile.collect {
                it?.let {
                    bind(it)
                }
            }
        }

        return binding.root
    }

    private fun bind(viewModel: ProfileStatsViewModel) {
        imageLoader.load(
            url = viewModel.imageURL,
            view = binding.headingImageView
        )

        binding.headingView.text = viewModel.heading
        binding.subheadingView.text = viewModel.subheading

        val linearLayout = binding.statsLayout
        linearLayout.removeAllViews()

        viewModel.stats.forEach { statViewModel ->
            val view = ProfileStatItemView(requireContext())
            view.viewModel = statViewModel
            linearLayout.addView(view)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
