package com.bruce32.psnprofileviewer.common

import androidx.recyclerview.widget.DiffUtil

class ListItemViewModelDiffer : DiffUtil.ItemCallback<ListItemViewModel>() {

    override fun areItemsTheSame(oldItem: ListItemViewModel, newItem: ListItemViewModel): Boolean {
        return if (oldItem.id != null && newItem.id != null) {
            oldItem.id == newItem.id
        } else {
            oldItem.title == newItem.title
        }
    }

    override fun areContentsTheSame(oldItem: ListItemViewModel, newItem: ListItemViewModel): Boolean {
        return oldItem.title == newItem.title &&
            oldItem.secondary == newItem.secondary &&
            oldItem.description == newItem.description &&
            oldItem.leadingIconURL == newItem.leadingIconURL &&
            oldItem.trailingText == newItem.trailingText &&
            oldItem.highlighted == newItem.highlighted
    }
}

