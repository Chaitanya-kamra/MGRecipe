package com.chaitanya.mgrecipe.ui.search.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chaitanya.mgrecipe.databinding.ItemRecipeSearchBinding
import com.chaitanya.recipedata.models.SearchQueryResponseItem

class SearchAdapter(
    val clickListener: (SearchQueryResponseItem) -> Unit
) : ListAdapter<SearchQueryResponseItem, SearchAdapter.ViewHolder>(DiffUtilCallBack()) {

    inner class ViewHolder(val binding: ItemRecipeSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SearchQueryResponseItem) {
            binding.apply {
                tvSuggestion.text = item.title
                root.setOnClickListener {
                    clickListener(item)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRecipeSearchBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<SearchQueryResponseItem>() {

        override fun areItemsTheSame(
            oldItem: SearchQueryResponseItem,
            newItem: SearchQueryResponseItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: SearchQueryResponseItem,
            newItem: SearchQueryResponseItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

    }


}