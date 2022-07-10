package com.bruce32.psnprofileviewer.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bruce32.psnprofileviewer.databinding.ListItemTemplateBinding

class ListItemAdapter(
    private var itemViewModels: List<ListItemViewModel>,
    private val onClick: ((href: String) -> Unit)? = null
) : RecyclerView.Adapter<ListItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTemplateBinding.inflate(inflater, parent, false)
        return ListItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val viewModel = itemViewModels[position]
        holder.bind(viewModel, onClick)
    }

    override fun getItemCount() = itemViewModels.size

    fun update(listItemViewModels: List<ListItemViewModel>) {
        this.itemViewModels = listItemViewModels
        notifyDataSetChanged()
    }
}
