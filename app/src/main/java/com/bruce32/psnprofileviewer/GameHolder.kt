package com.bruce32.psnprofileviewer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bruce32.psnprofileviewer.databinding.ListItemGameBinding
import com.bruce32.psnprofileviewer.model.Game
import com.bumptech.glide.Glide

class GameHolder(
    private val binding: ListItemGameBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(game: Game, onClick: (href: String) -> Unit) {
        binding.nameView.text = game.name
        binding.trophyView.text =
            "${game.platform}\n${game.gold} Gold, ${game.silver} Silver, ${game.bronze} Bronze"
        game.coverURL?.let {
            Glide.with(binding.coverImageView)
                .load(it.toString())
                .into(binding.coverImageView)
        }
        binding.root.setOnClickListener {
            onClick(game.href)
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
