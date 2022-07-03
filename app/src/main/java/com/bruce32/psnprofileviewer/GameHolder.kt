package com.bruce32.psnprofileviewer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bruce32.psnprofileviewer.databinding.ListItemGameBinding
import com.bruce32.psnprofileviewer.model.Game
import com.bumptech.glide.Glide

class GameHolder(
    val binding: ListItemGameBinding
) : RecyclerView.ViewHolder(binding.root) {

}

class GameListAdapter(
    private var games: List<Game>
): RecyclerView.Adapter<GameHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHolder {
        val inflator = LayoutInflater.from(parent.context)
        val binding = ListItemGameBinding.inflate(inflator, parent, false)
        return GameHolder(binding)
    }

    override fun onBindViewHolder(holder: GameHolder, position: Int) {
        val game = games[position]
        holder.apply {
            binding.nameView.text = game.name
            binding.trophyView.text = "${game.platform}\n${game.gold} Gold, ${game.silver} Silver, ${game.bronze} Bronze"
            game.coverURL?.let {
                println("download from ${it}")
                Glide.with(binding.coverImageView)
                    .load(it.toString())
                    .into(binding.coverImageView)
            }
        }
    }

    override fun getItemCount() = games.size

    fun update(games: List<Game>) {
        this.games = games
        notifyDataSetChanged()
    }
}