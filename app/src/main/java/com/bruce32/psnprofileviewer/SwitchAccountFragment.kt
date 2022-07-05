package com.bruce32.psnprofileviewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bruce32.psnprofileviewer.application.UserSource
import com.bruce32.psnprofileviewer.application.UserSourceImpl
import com.bruce32.psnprofileviewer.databinding.FragmentSwitchAccountBinding
import kotlinx.coroutines.launch

class SwitchAccountFragment(
    private val userSource: UserSource = UserSourceImpl.get()
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
            binding.setPsnIdText.hint = userSource.currentPsnId() ?: ""
        }

        binding.setIdButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                userSource.setCurrentPsnId(binding.setPsnIdText.text.toString())
            }
        }
    }
}
