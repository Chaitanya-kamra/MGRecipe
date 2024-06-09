package com.chaitanya.mgrecipe.ui.home.adapters

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chaitanya.mgrecipe.databinding.ItemAdBinding
import com.chaitanya.mgrecipe.databinding.ItemAllRecipeBinding
import com.chaitanya.mgrecipe.databinding.ItemHeaderHomeBinding
import com.chaitanya.mgrecipe.utility.loadImage
import com.chaitanya.recipedata.models.RandomRecipeResponse
import com.chaitanya.recipedata.models.SearchResult

class AllProductsAdapter(
    val popularData: RandomRecipeResponse?, val clickListener: (String) -> Unit
) : ListAdapter<SearchResult, RecyclerView.ViewHolder>(DiffUtilCallBack()) {

    private val layoutManagerStates = hashMapOf<String, Parcelable?>()
    private val HEADER_VIEW_TYPE = 0
    private val ITEM_VIEW_TYPE = 1
    private val AD_VIEW_TYPE = 2

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


    inner class AdViewHolder(val binding: ItemAdBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {

        }
    }

    inner class HeaderViewHolder(val binding: ItemHeaderHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val popularProductAdapter = PopularProductAdapter {
            clickListener(it)
        }

        fun bind() {

            binding.apply {
                //restoring scroll state
                rvPopular.layoutManager?.let {
                    val state: Parcelable? = layoutManagerStates[rvPopular.id.toString()]
                    if (state != null) {
                        it.onRestoreInstanceState(state)
                    } else {
                        it.scrollToPosition(0)
                    }
                }
                rvPopular.adapter = popularProductAdapter
                popularProductAdapter.submitList(popularData?.recipes)
                rvPopular.setRecycledViewPool(RecyclerView.RecycledViewPool())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER_VIEW_TYPE -> HeaderViewHolder(
                ItemHeaderHomeBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            AD_VIEW_TYPE -> AdViewHolder(
                ItemAdBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            else -> ViewHolder(
                ItemAllRecipeBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is HeaderViewHolder) {
            val state = holder.binding.rvPopular.layoutManager?.onSaveInstanceState()
            layoutManagerStates[holder.binding.rvPopular.id.toString()] = state
        }
        super.onViewRecycled(holder)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is ViewHolder -> holder.bind(getItem(position))
            is HeaderViewHolder -> {
                holder.bind()
            }

            is AdViewHolder -> holder.bind()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> HEADER_VIEW_TYPE
            else -> if (position % 6 == 0) AD_VIEW_TYPE else ITEM_VIEW_TYPE
        }
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