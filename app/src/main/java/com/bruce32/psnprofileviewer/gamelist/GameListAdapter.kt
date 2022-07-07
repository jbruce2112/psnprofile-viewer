package com.bruce32.psnprofileviewer.gamelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bruce32.psnprofileviewer.databinding.ListItemGameBinding
import com.bruce32.psnprofileviewer.model.Game

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
