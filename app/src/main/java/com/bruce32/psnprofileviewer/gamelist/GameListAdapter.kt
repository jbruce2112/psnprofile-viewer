package com.bruce32.psnprofileviewer.gamelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bruce32.psnprofileviewer.databinding.ListItemGameBinding

class GameListAdapter(
    private var gameViewModels: List<GameViewModel>,
    private val onClick: ((href: String) -> Unit)
) : RecyclerView.Adapter<GameHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemGameBinding.inflate(inflater, parent, false)
        return GameHolder(binding)
    }

    override fun onBindViewHolder(holder: GameHolder, position: Int) {
        val viewModel = gameViewModels[position]
        holder.bind(viewModel, onClick)
    }

    override fun getItemCount() = gameViewModels.size

    fun update(gameViewModels: List<GameViewModel>) {
        this.gameViewModels = gameViewModels
        notifyDataSetChanged()
    }
}
