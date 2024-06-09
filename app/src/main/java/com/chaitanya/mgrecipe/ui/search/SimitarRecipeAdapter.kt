package com.chaitanya.mgrecipe.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chaitanya.mgrecipe.databinding.ItemAllRecipeBinding
import com.chaitanya.mgrecipe.utility.loadImage
import com.chaitanya.recipedata.models.SimilarRecipeResponseItem

class SimitarRecipeAdapter(val clickListener: (String) -> Unit
) : ListAdapter<SimilarRecipeResponseItem, SimitarRecipeAdapter.ViewHolder>(DiffUtilCallBack()) {

    inner class ViewHolder(val binding: ItemAllRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SimilarRecipeResponseItem) {
            binding.apply {
                tvName.text = item.title
                tvTime.text = "Ready in ${item.readyInMinutes} min"
                ivImage.loadImage("https://img.spoonacular.com/recipes/${item.id}-90x90.${item.imageType}")
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

    class DiffUtilCallBack : DiffUtil.ItemCallback<SimilarRecipeResponseItem>() {

        override fun areItemsTheSame(
            oldItem: SimilarRecipeResponseItem,
            newItem: SimilarRecipeResponseItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: SimilarRecipeResponseItem,
            newItem: SimilarRecipeResponseItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

    }


}