package com.chaitanya.mgrecipe.ui.home

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chaitanya.mgrecipe.R
import com.chaitanya.mgrecipe.databinding.FragmentHomeBinding
import com.chaitanya.mgrecipe.utility.SharedPreference
import com.chaitanya.mgrecipe.ui.home.adapters.AllProductsAdapter
import com.chaitanya.mgrecipe.ui.home.adapters.PopularProductAdapter
import com.chaitanya.mgrecipe.utility.NetworkResult
import com.chaitanya.mgrecipe.utility.gone
import com.chaitanya.mgrecipe.utility.setStatusBarColor
import com.chaitanya.mgrecipe.utility.visible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    val viewModel: HomeViewModel by viewModels()
    private var popularProductAdapter : PopularProductAdapter? = null

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
            activity?.setStatusBarColor(Color.WHITE)
            binding?.apply {
                tvUserName.text = "👋 Hey "+ (SharedPreference.getUserName()?:"")

                btnSearch.setOnClickListener {
                    findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
                }
                rvAll.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val totalItemCount = layoutManager.itemCount
                        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                        println(totalItemCount)
                        println(lastVisibleItemPosition)
                        if (!viewModel.isLoading && !viewModel.isMaxPageReached&& totalItemCount <= (lastVisibleItemPosition + 4)) {
                            viewModel.fetchPaginatedData()
                        }
                    }
                })
            }
            bindObservers()
            viewModel.fetchCombinedData()
        }

    }

    private fun bindObservers() {
        viewModel.combinedData.observe(viewLifecycleOwner){networkResult->
            when(networkResult){
                is NetworkResult.Error -> {
                    binding?.apply {
                        flProgressOrError.visible()
                        tvError.visible()
                        pbLoad.gone()
                    }
                }
                is NetworkResult.Loading -> {
                    binding?.apply {
                        flProgressOrError.visible()
                        tvError.gone()
                        pbLoad.visible()
                    }
                }
                is NetworkResult.Success -> {
                    binding?.apply {
                        flProgressOrError.gone()
                        rvAll.visible()
                    }
                    viewModel.currentOffset = (networkResult.data?.first?.offset ?:0)+20
                    viewModel.isMaxPageReached = (networkResult.data?.first?.offset
                        ?: 0) >= (networkResult.data?.first?.totalResults ?: 20)
                    viewModel.isLoading = false
                    allProductsAdapter = AllProductsAdapter(networkResult.data?.second){
                        val action = HomeFragmentDirections.actionHomeFragmentToRecipeDetailFragment(recipeId = it,recipeEntity = null)
                        findNavController().navigate(action)
                    }
                    binding.rvAll.apply {
                        adapter = allProductsAdapter
                    }
                    val allRecipes = networkResult.data?.first?.results?.toMutableList()
                    allRecipes?.add(0,null)
                    allProductsAdapter?.submitList(allRecipes)
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
                    viewModel.isLoading = false
                    viewModel.currentOffset = (networkResult.data?.offset?:0)+20
                    viewModel.isMaxPageReached = (networkResult.data?.offset
                        ?: 0) >= (networkResult.data?.totalResults ?: 20)
                    val currentList = allProductsAdapter?.currentList?.toMutableList()
                    networkResult.data?.results?.toMutableList()
                        ?.let { currentList?.addAll(currentList.size, it) }
                    allProductsAdapter?.submitList(currentList)
                }
            }

        }
    }

}