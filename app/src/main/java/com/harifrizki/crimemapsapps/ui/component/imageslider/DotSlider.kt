package com.harifrizki.crimemapsapps.ui.component.imageslider

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import com.harifrizki.crimemapsapps.R
import com.harifrizki.crimemapsapps.utils.ZERO

class DotSlider(
    var context: Context? = null
) {
    var sizePage: Int? = null
    var linearLayout: LinearLayout? = null

    fun addBottomIconsShimmer(currentPage: Int?) {
        val ivDots = arrayOfNulls<ImageView>(sizePage!!)
        val iconActive = iconDotsIndicatorShimmer(true)
        val iconInActive = iconDotsIndicatorShimmer(false)

        linearLayout?.removeAllViews()
        for (i in ivDots.indices) {
            ivDots[i] = ImageView(context)
            ivDots[i]?.setImageResource(iconInActive[currentPage!!]!!)
            context?.resources?.getColor(R.color.light_gray, null)
                ?.let { ivDots[i]?.setColorFilter(it) }
            linearLayout?.addView(ivDots[i])
        }

        if (ivDots.size > ZERO) {
            ivDots[currentPage!!]?.setImageResource(iconActive[currentPage]!!)
            context?.resources?.getColor(R.color.light_gray, null)
                ?.let { ivDots[currentPage]?.setColorFilter(it) }
        }
    }

    private fun iconDotsIndicatorShimmer(active: Boolean?): Array<Int?> {
        val iconIndicator = arrayOfNulls<Int>(sizePage!!)
        for (i in iconIndicator.indices) {
            if (active!!)
                iconIndicator[i] = R.drawable.ic_round_brightness_24
            else iconIndicator[i] = R.drawable.ic_round_location_on_24
        }
        return iconIndicator
    }

    fun addBottomIcons(currentPage: Int?) {
        val ivDots = arrayOfNulls<ImageView>(sizePage!!)
        val iconActive = iconDotsIndicator(true)
        val iconInActive = iconDotsIndicator(false)

        linearLayout?.removeAllViews()
        for (i in ivDots.indices) {
            ivDots[i] = ImageView(context)
            ivDots[i]?.setImageResource(iconInActive[currentPage!!]!!)
            context?.resources?.getColor(R.color.light_gray, null)
                ?.let { ivDots[i]?.setColorFilter(it) }
            linearLayout?.addView(ivDots[i])
        }

        if (ivDots.size > ZERO) {
            ivDots[currentPage!!]?.setImageResource(iconActive[currentPage]!!)
            context?.resources?.getColor(R.color.primary, null)
                ?.let { ivDots[currentPage]?.setColorFilter(it) }
        }
    }

    private fun iconDotsIndicator(active: Boolean?): Array<Int?> {
        val iconIndicator = arrayOfNulls<Int>(sizePage!!)
        for (i in iconIndicator.indices) {
            if (active!!)
                iconIndicator[i] = R.drawable.ic_round_catching_pokemon_8
            else iconIndicator[i] = R.drawable.ic_round_brightness_8
        }
        return iconIndicator
    }
}