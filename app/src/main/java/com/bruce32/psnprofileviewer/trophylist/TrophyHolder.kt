package com.bruce32.psnprofileviewer.trophylist

import androidx.recyclerview.widget.RecyclerView
import com.bruce32.psnprofileviewer.databinding.ListItemTrophyBinding
import com.bruce32.psnprofileviewer.model.Trophy
import com.bumptech.glide.Glide
import com.google.android.material.R.color.material_dynamic_secondary0

class TrophyHolder(
    private val binding: ListItemTrophyBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(trophy: Trophy) {
        binding.nameView.text = trophy.name
        binding.descriptionView.text = trophy.description
        binding.gradeView.text = trophy.grade
        if (trophy.earned) {
            binding.root.setBackgroundColor(material_dynamic_secondary0)
        } else {
            binding.root.setBackgroundColor(android.R.color.transparent)
        }

        Glide.with(binding.imageView)
            .load(trophy.imageURL.toString())
            .into(binding.imageView)
    }
}
