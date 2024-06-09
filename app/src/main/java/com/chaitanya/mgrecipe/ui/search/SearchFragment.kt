package com.chaitanya.mgrecipe.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.chaitanya.mgrecipe.databinding.FragmentSearchBinding
import com.chaitanya.mgrecipe.ui.search.adapters.SearchAdapter
import com.chaitanya.mgrecipe.utility.IskeyboardVisible
import com.chaitanya.mgrecipe.utility.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    val viewModel: SearchViewModel by viewModels()
    var searchAdapter: SearchAdapter? = null
    var recipeBottomSheet:RecipeBottomSheet? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentSearchBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        searchAdapter = SearchAdapter{
            lifecycleScope.launch {
                if (binding.etSearch.IskeyboardVisible) {
                    imm?.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
                    delay(500)
                }
                recipeBottomSheet = RecipeBottomSheet()
                val bundle = Bundle()
                bundle.putString("id",it.id.toString())
                bundle.putString("title",it.title)
                recipeBottomSheet?.arguments = bundle
                recipeBottomSheet?.showNow(childFragmentManager,"RecipeBottomSheet")
            }

        }
        binding.apply {

            rvSuggestion.adapter = searchAdapter
            ivClear.setOnClickListener {
                etSearch.setText("")
                searchAdapter?.submitList(null)
            }
            ivBack.setOnClickListener {
                imm?.hideSoftInputFromWindow(binding.etSearch.windowToken,0)
                findNavController().popBackStack()
            }
            etSearch.doOnTextChanged { text, start, before, count ->
                if (text != null){
                    if (text.length > 2) {
                        viewModel.getsearchQueryResponse(text.toString())
                    }
                }
            }
            etSearch.requestFocus()
            imm?.showSoftInput(etSearch,InputMethodManager.SHOW_IMPLICIT)
        }
        bindObservers()
    }

    private fun bindObservers() {
        viewModel.searchQueryResponse.observe(viewLifecycleOwner){networkResult->
            when(networkResult){
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    searchAdapter?.submitList(networkResult.data)
                }
            }
        }
    }

}