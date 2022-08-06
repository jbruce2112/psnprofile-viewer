package com.bruce32.psnprofileviewer.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bruce32.psnprofileviewer.databinding.ListItemTemplateBinding

class ListItemAdapter(
    private val onClick: ((itemId: String) -> Unit)? = null
) : ListAdapter<ListItemViewModel, ListItemHolder>(ListItemViewModelDiffer()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTemplateBinding.inflate(inflater, parent, false)
        return ListItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val viewModel = getItem(position)
        holder.bind(viewModel, onClick)
    }
}
