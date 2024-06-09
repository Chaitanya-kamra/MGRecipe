package com.chaitanya.mgrecipe.ui.detail

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.chaitanya.mgrecipe.R
import com.chaitanya.mgrecipe.databinding.FragmentRecipeDetailBinding
import com.chaitanya.mgrecipe.ui.detail.adapters.ImageTextAdapter
import com.chaitanya.mgrecipe.utility.AnimationUtil
import com.chaitanya.mgrecipe.utility.NetworkResult
import com.chaitanya.mgrecipe.utility.gone
import com.chaitanya.mgrecipe.utility.loadImage
import com.chaitanya.mgrecipe.utility.visible
import com.chaitanya.recipedata.local.entity.RecipeEntity
import com.chaitanya.recipedata.models.SingleRecipeResponse
import com.chaitanya.recipedata.models.formatNutrition
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class RecipeDetailFragment : Fragment() {

    private var binding: FragmentRecipeDetailBinding? = null

    val args: RecipeDetailFragmentArgs by navArgs()
    val viewModel: DetailViewModel by viewModels()
    var equipmentAdapter: ImageTextAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = args.recipeId
        val recipeLocal = args.recipeEntity
        equipmentAdapter = ImageTextAdapter()
        binding?.layoutCommon?.rvEquipments?.adapter = equipmentAdapter

        binding?.mcvback?.setOnClickListener {
            findNavController().popBackStack()
        }
        bindObserver()
        id?.let {
            viewModel.getRecipeById(it)
        }
        recipeLocal?.let {
            setUpLocaldata(it)
            binding?.apply {
                scroll.visible()
                pbLoading.gone()
                tvError.gone()
            }
        }
    }

    private fun setUpLocaldata(data: RecipeEntity) {
        binding?.apply {
            ivImage.loadImage(data.image)
            layoutReadin.apply {
                type.text = "Ready in"
                value.text = data.readyInMinutes.toString()
            }
            layoutServing.apply {
                type.text = "Servings"
                value.text = data.servings.toString()
            }
            layoutPrice.apply {
                type.text = "Price/serving"
                value.text = data.pricePerServing.toString()
            }
            data.id?.let { id ->
                viewModel.isRecipeExist(id) {
                    if (!it) {
                        binding?.ivFavorite?.setImageResource(R.drawable.heart_outlined)
                    } else {
                        binding?.ivFavorite?.setImageResource(R.drawable.heart_filled)
                    }
                }
            }
            val ingredientAdapter = ImageTextAdapter()
            rvIngredients.adapter = ingredientAdapter
            ingredientAdapter.submitList(data.extendedIngredients?.map { ingredient ->
                Pair(("https://img.spoonacular.com/ingredients_100x100/" + ingredient.image),
                    ingredient.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
            })

            layoutCommon.apply {
                tvInstructions.text = Html.fromHtml(data.instructions, Html.FROM_HTML_MODE_LEGACY)
                tvSummary.text = Html.fromHtml(data.summary, Html.FROM_HTML_MODE_LEGACY)
                tvNutrients.text = data.nutrition?.let { formatNutrition(it) } ?: ""
                AnimationUtil.toggleArrow(ivToogleNutrients, false)
                AnimationUtil.expand(tvNutrients)
                ivToogleNutrients.setOnClickListener {
                    if (tvNutrients.visibility == View.VISIBLE) {
                        AnimationUtil.collapse(tvNutrients)
                        AnimationUtil.toggleArrow(ivToogleNutrients, true)
                    } else {
                        AnimationUtil.toggleArrow(ivToogleNutrients, false)
                        AnimationUtil.expand(tvNutrients)
                    }
                }
            }
            mcvFavorite.setOnClickListener {

                viewModel.isRecipeExist(data.id) {
                    if (it) {
                        viewModel.removeFavourite(data.id)
                        ivFavorite.setImageResource(R.drawable.heart_outlined)
                    } else {
                        viewModel.addLocalToFavorite(data)
                        ivFavorite.setImageResource(R.drawable.heart_filled)
                    }
                }

            }

        }
        equipmentAdapter?.submitList(
            data.equipment?.map { ingredient ->
                Pair("https://img.spoonacular.com/equipment_100x100/" + ingredient.image,
                    ingredient.name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.ROOT
                        ) else it.toString()
                    })
            }
        )
    }

    private fun bindObserver() {
        viewModel.recipeById.observe(viewLifecycleOwner) { networkResult ->
            when (networkResult) {
                is NetworkResult.Error -> {
                    binding?.apply {
                        scroll.gone()
                        pbLoading.gone()
                        tvError.visible()
                    }
                }

                is NetworkResult.Loading -> {
                    binding?.apply {
                        scroll.gone()
                        pbLoading.visible()
                        tvError.gone()
                    }
                }

                is NetworkResult.Success -> {
                    viewModel.recipeData = networkResult.data
                    setUpdata(networkResult.data)
                    binding?.apply {
                        scroll.visible()
                        pbLoading.gone()
                        tvError.gone()
                    }
                }
            }
        }
        viewModel.recipeEquipmmentById.observe(viewLifecycleOwner) { networkResult ->
            when (networkResult) {
                is NetworkResult.Error -> {

                }

                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    viewModel.recipeEquipment = networkResult.data
                    equipmentAdapter?.submitList(
                        networkResult.data?.equipment?.map { ingredient ->
                            Pair("https://img.spoonacular.com/equipment_100x100/" + ingredient.image,
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
            data?.id?.let { id ->
                viewModel.isRecipeExist(id) {
                    if (!it) {
                        binding?.ivFavorite?.setImageResource(R.drawable.heart_outlined)
                    } else {
                        binding?.ivFavorite?.setImageResource(R.drawable.heart_filled)
                    }
                }
            }
            val ingredientAdapter = ImageTextAdapter()
            rvIngredients.adapter = ingredientAdapter
            ingredientAdapter.submitList(data?.extendedIngredients?.map { ingredient ->
                Pair(("https://img.spoonacular.com/ingredients_100x100/" + ingredient.image),
                    ingredient.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
            })

            layoutCommon.apply {
                tvInstructions.text = Html.fromHtml(data?.instructions, Html.FROM_HTML_MODE_LEGACY)
                tvSummary.text = Html.fromHtml(data?.summary, Html.FROM_HTML_MODE_LEGACY)
                tvNutrients.text = data?.nutrition?.let { formatNutrition(it) } ?: ""
                AnimationUtil.toggleArrow(ivToogleNutrients, false)
                AnimationUtil.expand(tvNutrients)
                lnlNutrients.setOnClickListener {
                    if (tvNutrients.visibility == View.VISIBLE) {
                        AnimationUtil.collapse(tvNutrients)
                        AnimationUtil.toggleArrow(ivToogleNutrients, true)
                    } else {
                        AnimationUtil.toggleArrow(ivToogleNutrients, false)
                        AnimationUtil.expand(tvNutrients)
                    }
                }
            }
            mcvFavorite.setOnClickListener {
                if (viewModel.recipeEquipment != null && viewModel.recipeData != null && viewModel.recipeData?.id != null) {
                    viewModel.isRecipeExist(viewModel.recipeData!!.id) {
                        if (it) {
                            viewModel.removeFavourite(viewModel.recipeData!!.id)
                            ivFavorite.setImageResource(R.drawable.heart_outlined)
                        } else {
                            viewModel.addToFavorite()
                            ivFavorite.setImageResource(R.drawable.heart_filled)
                        }
                    }
                }
            }

        }
    }
}