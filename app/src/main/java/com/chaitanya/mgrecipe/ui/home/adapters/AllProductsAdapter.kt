package com.chaitanya.mgrecipe.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chaitanya.mgrecipe.databinding.ItemAllRecipeBinding
import com.chaitanya.mgrecipe.utility.loadImage
import com.chaitanya.recipedata.models.SearchResult

class AllProductsAdapter(val clickListener: (String) -> Unit
) : ListAdapter<SearchResult, AllProductsAdapter.ViewHolder>(DiffUtilCallBack()) {

    inner class ViewHolder(val binding: ItemAllRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SearchResult) {
            binding.apply {
                tvName.text = item.title
                tvTime.text = "Ready in ${item.readyInMinutes} min"
                ivImage.loadImage(item.image)
                root.setOnClickListener {
                    clickListener(item.id.toString())
                }
            }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemAllRecipeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<SearchResult>() {

        override fun areItemsTheSame(
            oldItem: SearchResult,
            newItem: SearchResult
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: SearchResult,
            newItem: SearchResult
        ): Boolean {
            return oldItem.id == newItem.id
        }

    }


}