package com.chaitanya.mgrecipe.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chaitanya.mgrecipe.databinding.ItemPopularRecipeBinding
import com.chaitanya.mgrecipe.utility.loadImage
import com.chaitanya.recipedata.models.Recipe

class PopularProductAdapter(val clickListener: (String) -> Unit
) : ListAdapter<Recipe, PopularProductAdapter.ViewHolder>(DiffUtilCallBack()) {

    inner class ViewHolder(val binding: ItemPopularRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Recipe) {
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
        return ViewHolder(
            ItemPopularRecipeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<Recipe>() {

        override fun areItemsTheSame(
            oldItem: Recipe,
            newItem: Recipe
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Recipe,
            newItem: Recipe
        ): Boolean {
            return oldItem.id == newItem.id
        }

    }


}