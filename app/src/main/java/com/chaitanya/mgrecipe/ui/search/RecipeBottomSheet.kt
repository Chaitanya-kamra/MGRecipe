package com.chaitanya.mgrecipe.ui.search

import android.content.res.Resources
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chaitanya.mgrecipe.R
import com.chaitanya.mgrecipe.databinding.LayoutRecipeBottomSheetBinding
import com.chaitanya.mgrecipe.ui.detail.ImageTextAdapter
import com.chaitanya.mgrecipe.utility.AnimationUtil
import com.chaitanya.mgrecipe.utility.NetworkResult
import com.chaitanya.mgrecipe.utility.gone
import com.chaitanya.mgrecipe.utility.loadImage
import com.chaitanya.mgrecipe.utility.visible
import com.chaitanya.recipedata.models.SearchQueryResponseItem
import com.chaitanya.recipedata.models.SingleRecipeResponse
import com.chaitanya.recipedata.models.formatNutrition
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Locale


class RecipeBottomSheet() : BottomSheetDialogFragment() {
    private val fullScreen: Boolean = true
    private val expand: Boolean = true
    private var binding : LayoutRecipeBottomSheetBinding? =  null
    private val viewModel: SearchViewModel by viewModels(
        ownerProducer = {requireParentFragment()}
    )
    var equipmentAdapter : ImageTextAdapter? = null

    var simitarRecipeAdapter:SimitarRecipeAdapter? = null

    var recipeId :String? = null
    var recipeTitle :String? = null
    var state = SheetState.IMAGE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = LayoutRecipeBottomSheetBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet: FrameLayout =
                dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)
                    ?: return@setOnShowListener
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            if (fullScreen && bottomSheet.layoutParams != null) {
                showFullScreenBottomSheet(bottomSheet)
            }

            if (!expand) return@setOnShowListener

            bottomSheet.setBackgroundResource(android.R.color.transparent)
            expandBottomSheet(bottomSheetBehavior)
        }
        recipeId= arguments?.getString("id")
        recipeTitle = arguments?.getString("title")
        setUpViews()
        bindObserver()
        recipeId?.let { viewModel.getRecipeById(it) }

    }

    private fun bindObserver() {
        viewModel.recipeById.observe(viewLifecycleOwner){networkResult->
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
                    viewModel.recipeData = networkResult.data
                    setUpdata(networkResult.data)
                    binding?.flProgressOrError?.gone()
                }
            }
        }
        viewModel.recipeSimilarById.observe(viewLifecycleOwner){networkResult->
            when(networkResult){
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    binding?.pbSimilar?.gone()
                    simitarRecipeAdapter?.submitList(networkResult.data)
                }
            }

        }
        viewModel.recipeEquipmmentById.observe(viewLifecycleOwner){networkResult->
            when(networkResult){
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    viewModel.recipeEquipment = networkResult.data
                    equipmentAdapter?.submitList(
                        networkResult.data?.equipment?.map {ingredient ->
                            Pair("https://img.spoonacular.com/equipment_100x100/"+ingredient.image,
                                ingredient.name.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.ROOT
                                    ) else it.toString()
                                })
                        }
                    )
                }
            }

        }
    }

    private fun setUpdata(data: SingleRecipeResponse?) {
        binding?.apply {
            ivImage.loadImage(data?.image)
            layoutReadin.apply {
                type.text = "Ready in"
                value.text = data?.readyInMinutes.toString()
            }
            layoutServing.apply {
                type.text = "Servings"
                value.text = data?.servings.toString()
            }
            layoutPrice.apply {
                type.text = "Price/serving"
                value.text = data?.pricePerServing.toString()
            }
            data?.id?.let {id->
                viewModel.isRecipeExist(id){
                    if (!it){
                        binding?.ivFavouite?.setImageResource(R.drawable.heart_outlined)
                    }else {
                        binding?.ivFavouite?.setImageResource(R.drawable.heart_filled)
                    }
                }
            }
            val ingredientAdapter = SearchImageTextAdapter()
            rvIngredients.adapter = ingredientAdapter
            ingredientAdapter.submitList(data?.extendedIngredients?.map { ingredient ->
                Pair(("https://img.spoonacular.com/ingredients_100x100/" + ingredient.image),
                    ingredient.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
            })
            layoutCommon.apply {
                tvInstructions.text =   Html.fromHtml(data?.instructions, Html.FROM_HTML_MODE_LEGACY)
                tvSummary.text = Html.fromHtml(data?.summary, Html.FROM_HTML_MODE_LEGACY)
                tvNutrients.text = data?.nutrition?.let { formatNutrition(it) }?:""
                AnimationUtil.toggleArrow(ivToogleNutrients,false)
                AnimationUtil.expand(tvNutrients)
                ivToogleNutrients.setOnClickListener {
                    if (tvNutrients.visibility == View.VISIBLE) {
                        AnimationUtil.collapse(tvNutrients)
                        AnimationUtil.toggleArrow(ivToogleNutrients,true)
                    }
                    else {
                        AnimationUtil.toggleArrow(ivToogleNutrients,false)
                        AnimationUtil.expand(tvNutrients)
                    }
                }
            }
            btnNext.setOnClickListener {
                when(state){
                    SheetState.IMAGE -> {
                        btnNext.text ="Get full recipie"
                        secondLayout.visible()
                        AnimationUtil.collapse(firstLayout)
                        state = SheetState.INGREDIENTS

                    }
                    SheetState.INGREDIENTS ->{
                        btnNext.text = "Get similar recipe"
                        thirdLayout.visible()
                        AnimationUtil.collapseRecyclerView(rvIngredients)
                        state = SheetState.RECIPE
                    }
                    SheetState.RECIPE -> {
                        btnNext.gone()
                        forthLayout.visible()
                        AnimationUtil.collapse(scroll)
                        viewModel.getSimilarRecipe(data?.id.toString())
                    }
                    SheetState.SIMILAR -> {

                    }
                }
            }
            val onclickArrow = {
                state = SheetState.IMAGE
                btnNext.text = "Get ingredients"
                btnNext.visible()
                secondLayout.gone()
                thirdLayout.gone()
                forthLayout.gone()
                secondLayout.requestLayout()
                thirdLayout.requestLayout()
                forthLayout.requestLayout()
                scroll.requestLayout()
                rvIngredients.requestLayout()
//                AnimationUtil.expand(scroll)
//                AnimationUtil.expandRecyclerView(rvIngredients)
//                AnimationUtil.collapse(secondLayout)
                AnimationUtil.expand(firstLayout)


            }
            ivClearIngredients.setOnClickListener { onclickArrow() }
            ivClearRecipe.setOnClickListener { onclickArrow() }
            ivClearSimilar.setOnClickListener { onclickArrow() }

        }
    }

    private fun setUpViews() {
        binding?.apply {
            tvTitle.text = recipeTitle?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
            equipmentAdapter = ImageTextAdapter()
            layoutCommon.rvEquipments.adapter = equipmentAdapter
            btnNext.text = "Get ingredients"
            simitarRecipeAdapter = SimitarRecipeAdapter {

            }
            rvSimilar.adapter = simitarRecipeAdapter
            ivBack.setOnClickListener {
                dismiss()
            }
            ivFavouite.setOnClickListener {
                if (viewModel.recipeEquipment!= null&& viewModel.recipeData!= null&& viewModel.recipeData?.id != null){
                    viewModel.isRecipeExist(viewModel.recipeData!!.id){
                        if (it){
                            viewModel.removeFavourite(viewModel.recipeData!!.id)
                            ivFavouite.setImageResource(R.drawable.heart_outlined)
                        }else {
                            viewModel.addToFavorite()
                            ivFavouite.setImageResource(R.drawable.heart_filled)
                        }
                    }
                }else{
                    Toast.makeText(requireContext(),"Try Again!",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    enum class SheetState{
        IMAGE,INGREDIENTS,RECIPE,SIMILAR
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    fun showFullScreenBottomSheet(bottomSheet: FrameLayout) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = Resources.getSystem().displayMetrics.heightPixels -200
        bottomSheet.layoutParams = layoutParams
    }

    private fun expandBottomSheet(bottomSheetBehavior: BottomSheetBehavior<FrameLayout>) {
        bottomSheetBehavior.skipCollapsed = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

}