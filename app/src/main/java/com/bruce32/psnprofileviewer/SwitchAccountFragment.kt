package com.bruce32.psnprofileviewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bruce32.psnprofileviewer.application.ProfileRepository
import com.bruce32.psnprofileviewer.database.ProfilePersistence
import com.bruce32.psnprofileviewer.databinding.FragmentSwitchAccountBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            withContext(Dispatchers.IO) {
                val psnID = persistence.getCurrentUser()
                psnID?.let {
                    withContext(Dispatchers.Main) {
                        binding.setPsnIdText.hint = it
                    }
                }
            }
        }

        binding.setIdButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val newId = binding.setPsnIdText.text.toString()
                    persistence.setCurrentUser(newId)
                    repository.refreshProfileAndGames()
                }
            }
        }
    }
}
