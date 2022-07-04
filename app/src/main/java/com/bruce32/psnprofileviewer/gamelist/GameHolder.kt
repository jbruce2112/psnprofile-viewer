package com.bruce32.psnprofileviewer.gamelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bruce32.psnprofileviewer.databinding.ListItemGameBinding
import com.bruce32.psnprofileviewer.model.Game
import com.bumptech.glide.Glide
import com.google.android.material.R.color.material_dynamic_secondary0
import kotlin.math.roundToInt

class GameHolder(
    private val binding: ListItemGameBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(game: Game, onClick: (href: String) -> Unit) {
        binding.nameView.text = game.name
        binding.trophyView.text =
            "${game.platform}\n${game.gold} Gold, ${game.silver} Silver, ${game.bronze} Bronze"
        binding.completionView.text = "${game.completionPercent.roundToInt()}%"

        if (game.platinum == 1 || game.earnedTrophies == game.totalTrophies) {
            binding.root.setBackgroundColor(material_dynamic_secondary0)
        } else {
            binding.root.setBackgroundColor(android.R.color.transparent)
        }

        Glide.with(binding.coverImageView)
            .load(game.coverURL.toString())
            .into(binding.coverImageView)

        binding.root.setOnClickListener {
            onClick(game.id)
        }
    }
}

class GameListAdapter(
    private var games: List<Game>,
    private val onClick: ((href: String) -> Unit)
) : RecyclerView.Adapter<GameHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemGameBinding.inflate(inflater, parent, false)
        return GameHolder(binding)
    }

    override fun onBindViewHolder(holder: GameHolder, position: Int) {
        val game = games[position]
        holder.bind(game, onClick)
    }

    override fun getItemCount() = games.size

    fun update(games: List<Game>) {
        this.games = games
        notifyDataSetChanged()
    }
}
