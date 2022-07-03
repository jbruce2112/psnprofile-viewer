package com.bruce32.psnprofileviewer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bruce32.psnprofileviewer.api.CompleteGame
import com.bruce32.psnprofileviewer.api.Trophy
import com.bruce32.psnprofileviewer.databinding.ListItemTrophyBinding

class TrophyHolder(
    private val binding: ListItemTrophyBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(trophy: Trophy) {
        binding.nameView.text = trophy.name
        binding.descriptionView.text = trophy.description
        binding.gradeView.text = trophy.grade
    }
}

class TrophyListAdapter(
    private var game: CompleteGame
) : RecyclerView.Adapter<TrophyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrophyHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTrophyBinding.inflate(inflater, parent, false)
        return TrophyHolder(binding)
    }

    override fun onBindViewHolder(holder: TrophyHolder, position: Int) {
        val trophy = game.trophies[position]
        holder.bind(trophy)
    }

    override fun getItemCount() = game.trophies.size

    fun update(game: CompleteGame) {
        this.game = game
        notifyDataSetChanged()
    }
}
