package com.bruce32.psnprofileviewer.switchaccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bruce32.psnprofileviewer.databinding.FragmentSwitchAccountBinding
import kotlinx.coroutines.launch

class SwitchAccountFragment(
    private val viewModelFactory: SwitchAccountViewModelFactory = SwitchAccountViewModelFactory()
) : Fragment() {

    private val viewModel: SwitchAccountViewModel by viewModels {
        viewModelFactory
    }

    private var _binding: FragmentSwitchAccountBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding is null"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSwitchAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                setUpClickListener()
                observeUserFieldHintTextChanges()
            }
        }
    }

    private fun setUpClickListener() {
        binding.setIdButton.setOnClickListener {
            updateCurrentUserAndGoBack()
        }
    }

    private suspend fun observeUserFieldHintTextChanges() {
        viewModel.userFieldHint.collect {
            binding.setPsnIdText.hint = it?.getString(requireContext())
        }
    }

    private fun updateCurrentUserAndGoBack() {
        viewLifecycleOwner.lifecycleScope.launch {
            val newId = binding.setPsnIdText.text.toString()
            viewModel.setCurrentUser(newId)
            activity?.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
