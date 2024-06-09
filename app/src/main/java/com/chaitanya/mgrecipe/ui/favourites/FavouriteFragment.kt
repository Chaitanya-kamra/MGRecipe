package com.chaitanya.mgrecipe.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chaitanya.mgrecipe.databinding.FragmentFavouriteBinding
import com.chaitanya.mgrecipe.ui.favourites.adapters.FavouriteProductsAdapter
import com.chaitanya.mgrecipe.utility.gone
import com.chaitanya.mgrecipe.utility.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteFragment : Fragment() {


    private var binding: FragmentFavouriteBinding? = null
    val viewModel: FavouriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentFavouriteBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = FavouriteProductsAdapter {
            viewModel.getRecipeById(it) { recipe ->
                val action =
                    FavouriteFragmentDirections.actionFavouriteFragmentToRecipeDetailFragment(
                        recipeEntity = recipe,
                        recipeId = null
                    )
                findNavController().navigate(action)
            }
        }
        binding?.rvFourite?.adapter = adapter
        viewModel.allRecipes.observe(viewLifecycleOwner) {
            binding?.apply {
                pbFavouriteLoad.gone()
                rvFourite.visible()
            }
            adapter.submitList(it)
        }
    }

}