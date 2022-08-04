package com.bruce32.psnprofileviewer.common

import androidx.recyclerview.widget.RecyclerView
import com.bruce32.psnprofileviewer.databinding.ListItemTemplateBinding
import com.google.android.material.R.color.material_dynamic_secondary0

class ListItemHolder(
    private val binding: ListItemTemplateBinding,
    private val imageLoader: ImageLoader = GlideImageLoader()
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: ListItemViewModel, onClick: ((href: String) -> Unit)? = null) {
        binding.titleView.text = viewModel.title
        binding.secondaryView.text = viewModel.secondary
        binding.descriptionView.text = viewModel.description
        binding.trailingView.text = viewModel.trailingText

        if (viewModel.highlighted) {
            binding.root.setBackgroundColor(material_dynamic_secondary0)
        } else {
            binding.root.setBackgroundColor(android.R.color.transparent)
        }

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
}
