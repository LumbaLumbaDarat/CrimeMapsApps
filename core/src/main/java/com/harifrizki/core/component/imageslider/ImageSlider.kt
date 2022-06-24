package com.harifrizki.core.component.imageslider

import androidx.fragment.app.FragmentActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.harifrizki.core.databinding.ImageSliderBinding
import com.harifrizki.core.model.ImageCrimeLocation

class ImageSlider(
    var isCanEdit: Boolean? = null,
) {
    private var binding: ImageSliderBinding? = null

    var onClickPreview: ((ImageCrimeLocation) -> Unit)? = null
    var onClickEdit: ((ImageCrimeLocation) -> Unit)? = null

    fun create(binding: ImageSliderBinding?) {
        this.binding = binding
    }

    fun setImageSlider(fragmentActivity: FragmentActivity?,
                       imageCrimeLocations: ArrayList<ImageCrimeLocation>?) {
        binding?.apply {
            vpImage.apply {
                adapter = ImagePagerAdapter(fragmentActivity!!).apply {
                    isCanEdit = this@ImageSlider.isCanEdit
                    setImageCrimeLocations(imageCrimeLocations)
                    onClickPreview = {
                        this@ImageSlider.onClickPreview?.invoke(it)
                    }
                    onClickEdit = {
                        this@ImageSlider.onClickEdit?.invoke(it)
                    }
                }
            }
            TabLayoutMediator(tlIndicator, vpImage) { _, _ ->}.attach()
        }
    }
}