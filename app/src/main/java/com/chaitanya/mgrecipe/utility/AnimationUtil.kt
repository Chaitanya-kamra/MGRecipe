package com.chaitanya.mgrecipe.utility

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

object AnimationUtil {
    fun toggleArrow(view: View, isExpanded: Boolean): Boolean {
        return if (isExpanded) {
            view.animate().setDuration(200).rotation(180f)
            true
        } else {
            view.animate().setDuration(200).rotation(0f)
            false
        }
    }

    fun expand(view: View) {
        val animation = expandAction(view)
        view.startAnimation(animation)
    }

    private fun expandAction(view: View): Animation {
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val actualHeight = view.measuredHeight
        view.layoutParams.height = 0
        view.visibility = View.VISIBLE
        val animation: Animation = object : Animation() {
            override fun applyTransformation(
                interpolatedTime: Float,
                t: Transformation?
            ) {
                view.layoutParams.height =
                    if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (actualHeight * interpolatedTime).toInt()
                view.requestLayout()
            }
        }
        animation.duration = (actualHeight / view.context.resources.displayMetrics.density).toLong()
        return animation
    }

    fun collapse(view: View) {
        val actualHeight = view.measuredHeight
        val animation: Animation = object : Animation() {
            override fun applyTransformation(
                interpolatedTime: Float,
                t: Transformation?
            ) {
                if (interpolatedTime == 1f) {
                    view.visibility = View.GONE
                } else {
                    view.layoutParams.height =
                        actualHeight - (actualHeight * interpolatedTime).toInt()
                    view.requestLayout()
                }
            }
        }
        animation.duration = (actualHeight / view.context.resources.displayMetrics.density).toLong()
        view.startAnimation(animation)
    }

    fun collapseLayout(linearLayout: LinearLayout,linkTitle: TextView?,linkUrl: TextView?) {
        val initialHeight = linearLayout.height

        val animation = ValueAnimator.ofInt(initialHeight, 1)
        animation.addUpdateListener { valueAnimator ->
            val layoutParams = linearLayout.layoutParams
            layoutParams.height = valueAnimator.animatedValue as Int
            linearLayout.layoutParams = layoutParams
        }

        animation.duration = 300 // Adjust the duration as needed

        animation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                linearLayout.visibility = View.GONE
                linkTitle?.maxLines = 1
                linkUrl?.maxLines = 1
                linkTitle?.isSingleLine = true
                linkUrl?.isSingleLine = true

                // Reset alpha to 1 (fully visible)
                linkTitle?.alpha = 1f
                linkUrl?.alpha = 1f
            }
        })

        animation.start()
    }
    fun expandLayout(linearLayout: LinearLayout, linkTitle: TextView?,linkUrl: TextView?) {
        linearLayout.measure(
            View.MeasureSpec.makeMeasureSpec(linearLayout.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )

        val targetHeight = linearLayout.measuredHeight
        linearLayout.layoutParams.height = 1
        linearLayout.visibility = View.VISIBLE


        val animation = ValueAnimator.ofInt(1, targetHeight)
        animation.addUpdateListener { valueAnimator ->
            val layoutParams = linearLayout.layoutParams
            layoutParams.height = valueAnimator.animatedValue as Int
            linearLayout.layoutParams = layoutParams

            val animatedvalue = valueAnimator.animatedValue as Int
            val maxLines = if (animatedvalue < targetHeight) 1 else Int.MAX_VALUE
            linkTitle?.maxLines = maxLines
            linkUrl?.maxLines = maxLines
            linkTitle?.isSingleLine = false
            linkUrl?.isSingleLine = false
        }

        animation.interpolator = CustomBounceInterpolator(0.2f, 5f) // Adjust these values
        animation.duration = 500 // Adjust the duration as needed
        animation.start()
    }

    fun expandRecyclerView(recyclerView: RecyclerView) {
        recyclerView.measure(
            View.MeasureSpec.makeMeasureSpec(recyclerView.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )

        val targetHeight = recyclerView.measuredHeight
        recyclerView.layoutParams.height = 1
        recyclerView.visibility = View.VISIBLE

        val animation = ValueAnimator.ofInt(1, targetHeight)
        animation.addUpdateListener { valueAnimator ->
            val layoutParams = recyclerView.layoutParams
            layoutParams.height = valueAnimator.animatedValue as Int
            recyclerView.layoutParams = layoutParams
        }

        animation.interpolator = CustomBounceInterpolator(0.2f, 5f) // Adjust these values
        animation.duration = 500 // Adjust the duration as needed
        animation.start()
    }
    fun collapseRecyclerView(recyclerView: RecyclerView) {
        val initialHeight = recyclerView.height

        val animation = ValueAnimator.ofInt(initialHeight, 1)
        animation.addUpdateListener { valueAnimator ->
            val layoutParams = recyclerView.layoutParams
            layoutParams.height = valueAnimator.animatedValue as Int
            recyclerView.layoutParams = layoutParams
        }

        animation.duration = 300 // Adjust the duration as needed

        animation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                recyclerView.visibility = View.GONE
            }
        })

        animation.start()
    }

    // Custom interpolator for a subtle bounce effect
    class CustomBounceInterpolator(private val amplitude: Float, private val frequency: Float) : TimeInterpolator {
        override fun getInterpolation(input: Float): Float {
            return (-1 * Math.pow(Math.E, (-input / amplitude).toDouble()) *
                    Math.cos((frequency * input).toDouble()) + 1).toFloat()
        }
    }

    fun rotateClockwise(view: View?) {
        val rotate = ObjectAnimator.ofFloat(view, "rotation", 0f, 180f)
        rotate.setDuration(700)
        rotate.start()
    }

}