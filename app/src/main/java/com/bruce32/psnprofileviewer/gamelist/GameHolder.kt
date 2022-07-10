package com.bruce32.psnprofileviewer.gamelist

import androidx.recyclerview.widget.RecyclerView
import com.bruce32.psnprofileviewer.databinding.ListItemGameBinding
import com.bumptech.glide.Glide
import com.google.android.material.R.color.material_dynamic_secondary0

class GameHolder(
    private val binding: ListItemGameBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: GameViewModel, onClick: (href: String) -> Unit) {
        binding.nameView.text = viewModel.title
        binding.trophyView.text = viewModel.description
        binding.completionView.text = viewModel.trailingText

        if (viewModel.highlighted) {
            binding.root.setBackgroundColor(material_dynamic_secondary0)
        } else {
            binding.root.setBackgroundColor(android.R.color.transparent)
        }

        Glide.with(binding.coverImageView)
            .load(viewModel.leadingIconURL.toString())
            .into(binding.coverImageView)

        binding.root.setOnClickListener {
            onClick(viewModel.id)
        }
    }
}
