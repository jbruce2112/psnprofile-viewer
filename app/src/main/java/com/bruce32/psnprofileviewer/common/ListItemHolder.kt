package com.bruce32.psnprofileviewer.common

import androidx.recyclerview.widget.RecyclerView
import com.bruce32.psnprofileviewer.databinding.ListItemTemplateBinding
import com.google.android.material.R

class ListItemHolder(
    private val binding: ListItemTemplateBinding,
    private val imageLoader: ImageLoader = GlideImageLoader()
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: ListItemViewModel, onClick: ((itemId: String) -> Unit)? = null) {
        binding.titleView.text = viewModel.title
        binding.secondaryView.text = viewModel.secondary
        binding.descriptionView.text = viewModel.description
        binding.trailingView.text = viewModel.trailingText

        binding.root.setBackgroundColor(backgroundColor(viewModel.highlighted))

        imageLoader.load(
            url = viewModel.leadingIconURL,
            view = binding.imageView
        )

        viewModel.id?.let { viewModelId ->
            binding.root.setOnClickListener {
                onClick?.invoke(viewModelId)
            }
        }
    }

    private fun backgroundColor(highlighted: Boolean): Int {
        return if (highlighted) {
            R.color.material_dynamic_secondary0
        } else {
            android.R.color.transparent
        }
    }
}
