package com.bruce32.psnprofileviewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bruce32.psnprofileviewer.application.ProfileRepository
import com.bruce32.psnprofileviewer.database.ProfilePersistence
import com.bruce32.psnprofileviewer.databinding.FragmentSwitchAccountBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SwitchAccountFragment(
    private val persistence: ProfilePersistence = ProfilePersistence.get(),
    private val repository: ProfileRepository = ProfileRepository()
) : Fragment() {

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
                updateTextHintWithCurrentUser()
                binding.setIdButton.setOnClickListener {
                    updateCurrentUserAndGoBack()
                }
            }
        }
    }

    private suspend fun updateTextHintWithCurrentUser() {
        val currentUser = persistence.getCurrentUser().first()
        binding.setPsnIdText.hint = currentUser?.psnId  ?: "PSN ID"
    }

    private fun updateCurrentUserAndGoBack() {
        viewLifecycleOwner.lifecycleScope.launch {
            val newId = binding.setPsnIdText.text.toString()
            persistence.setCurrentUser(newId)
            repository.refreshProfileAndGames()
            activity?.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
