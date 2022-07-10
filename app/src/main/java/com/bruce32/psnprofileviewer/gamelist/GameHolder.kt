package com.bruce32.psnprofileviewer.gamelist

import androidx.recyclerview.widget.RecyclerView
import com.bruce32.psnprofileviewer.databinding.ListItemGameBinding
import com.bumptech.glide.Glide
import com.google.android.material.R.color.material_dynamic_secondary0

class GameHolder(
    private val binding: ListItemGameBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: GameViewModel, onClick: (href: String) -> Unit) {
        binding.titleView.text = viewModel.title
        binding.secondaryView.text = viewModel.secondary
        binding.descriptionView.text = viewModel.description
        binding.trailingView.text = viewModel.trailingText

        if (viewModel.highlighted) {
            binding.root.setBackgroundColor(material_dynamic_secondary0)
        } else {
            binding.root.setBackgroundColor(android.R.color.transparent)
        }

        Glide.with(binding.imageView)
            .load(viewModel.leadingIconURL.toString())
            .into(binding.imageView)

        binding.root.setOnClickListener {
            onClick(viewModel.id)
        }
    }
}
