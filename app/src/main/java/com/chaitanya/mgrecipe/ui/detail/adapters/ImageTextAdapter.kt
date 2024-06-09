package com.chaitanya.mgrecipe.ui.detail.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chaitanya.mgrecipe.databinding.ItemImageNameBinding
import com.chaitanya.mgrecipe.utility.loadImage

// Adapter for image and text
class ImageTextAdapter : ListAdapter<Pair<String, String>, ImageTextAdapter.ViewHolder>(
    DiffUtilCallBack()
) {

    // ViewHolder for image and text
    inner class ViewHolder(val binding: ItemImageNameBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Pair<String, String>) {
            binding.apply {
                ivImage.loadImage(item.first)
                tvName.text = item.second
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemImageNameBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    // DiffUtilCallBack for smooth update
    class DiffUtilCallBack : DiffUtil.ItemCallback<Pair<String, String>>() {

        override fun areItemsTheSame(
            oldItem: Pair<String, String>, newItem: Pair<String, String>
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Pair<String, String>, newItem: Pair<String, String>
        ): Boolean {
            return oldItem == newItem
        }

    }


}