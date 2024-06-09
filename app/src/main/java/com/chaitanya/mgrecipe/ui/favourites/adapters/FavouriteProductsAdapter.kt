package com.chaitanya.mgrecipe.ui.favourites.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chaitanya.mgrecipe.databinding.ItemAllRecipeBinding
import com.chaitanya.mgrecipe.utility.loadImage
import com.chaitanya.recipedata.local.entity.RecipeEntity

class FavouriteProductsAdapter(val clickListener: (Long) -> Unit
) : ListAdapter<RecipeEntity, FavouriteProductsAdapter.ViewHolder>(DiffUtilCallBack()) {

    inner class ViewHolder(val binding: ItemAllRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RecipeEntity) {
            binding.apply {
                tvName.text = item.title
                tvTime.text = "Ready in ${item.readyInMinutes} min"
                ivImage.loadImage(item.image)
                root.setOnClickListener {
                    clickListener(item.id)
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

    class DiffUtilCallBack : DiffUtil.ItemCallback<RecipeEntity>() {

        override fun areItemsTheSame(
            oldItem: RecipeEntity,
            newItem: RecipeEntity
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: RecipeEntity,
            newItem: RecipeEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }

    }


}