package com.bruce32.psnprofileviewer.trophylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bruce32.psnprofileviewer.databinding.ListItemTrophyBinding
import com.bruce32.psnprofileviewer.model.Trophy

class TrophyListAdapter(
    private var trophies: List<Trophy>
) : RecyclerView.Adapter<TrophyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrophyHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTrophyBinding.inflate(inflater, parent, false)
        return TrophyHolder(binding)
    }

    override fun onBindViewHolder(holder: TrophyHolder, position: Int) {
        val trophy = trophies[position]
        holder.bind(trophy)
    }

    override fun getItemCount() = trophies.size

    fun update(trophies: List<Trophy>) {
        this.trophies = trophies
        notifyDataSetChanged()
    }
}
