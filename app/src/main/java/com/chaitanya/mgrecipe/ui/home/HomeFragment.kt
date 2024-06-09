package com.chaitanya.mgrecipe.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chaitanya.mgrecipe.R
import com.chaitanya.mgrecipe.databinding.FragmentHomeBinding
import com.chaitanya.mgrecipe.utility.SharedPreference
import com.chaitanya.mgrecipe.ui.home.adapters.AllProductsAdapter
import com.chaitanya.mgrecipe.ui.home.adapters.PopularProductAdapter
import com.chaitanya.mgrecipe.utility.NetworkResult
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    val viewModel: HomeViewModel by viewModels()
    private var popularProductAdapter:PopularProductAdapter? = null
    private var allProductsAdapter:AllProductsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentHomeBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!SharedPreference.isLoggedIn()) {
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }else {

            allProductsAdapter = AllProductsAdapter{
                val action = HomeFragmentDirections.actionHomeFragmentToRecipeDetailFragment(recipeId = it,recipeEntity = null)
                findNavController().navigate(action)
            }
            popularProductAdapter = PopularProductAdapter{
                val action = HomeFragmentDirections.actionHomeFragmentToRecipeDetailFragment(recipeId = it,recipeEntity = null)
                findNavController().navigate(action)
            }
            binding?.apply {
                tvUserName.text = "👋 Hey "+ (SharedPreference.getUserName()?:"")
                rvPopular.adapter = popularProductAdapter
                rvAll.adapter = allProductsAdapter
                btnSearch.setOnClickListener {
                    findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
                }
            }
            bindObservers()
//            viewModel.getPopularData()
//            viewModel.getAllRecipeData()

        }

    }

    private fun bindObservers() {
        viewModel.popularData.observe(viewLifecycleOwner){networkResult->
            when(networkResult){
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    popularProductAdapter?.submitList(networkResult.data?.recipes)
                }
            }
        }
        viewModel.allRecipesData.observe(viewLifecycleOwner){networkResult->
            when(networkResult){
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    allProductsAdapter?.submitList(networkResult.data?.results)
                }
            }

        }
    }

}