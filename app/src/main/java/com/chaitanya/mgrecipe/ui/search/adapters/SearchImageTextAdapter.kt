package com.chaitanya.mgrecipe.ui.search.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chaitanya.mgrecipe.databinding.ItemImageNameSearchBinding
import com.chaitanya.mgrecipe.utility.loadImage

class SearchImageTextAdapter() : ListAdapter<Pair<String,String>, SearchImageTextAdapter.ViewHolder>(
    DiffUtilCallBack()
) {

    inner class ViewHolder(val binding: ItemImageNameSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Pair<String,String>) {
            binding.apply {
                ivImage.loadImage(item.first)
                tvName.text = item.second
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemImageNameSearchBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<Pair<String,String>>() {

        override fun areItemsTheSame(
            oldItem: Pair<String,String>,
            newItem: Pair<String,String>
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Pair<String,String>,
            newItem: Pair<String,String>
        ): Boolean {
            return oldItem == newItem
        }

    }


}