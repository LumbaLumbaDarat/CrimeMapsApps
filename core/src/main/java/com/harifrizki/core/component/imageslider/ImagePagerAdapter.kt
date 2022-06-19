package com.harifrizki.core.component.imageslider

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.harifrizki.core.model.ImageCrimeLocation

class ImagePagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private var imageCrimeLocations: ArrayList<ImageCrimeLocation> = arrayListOf()

    var isCanEdit: Boolean? = null

    var onClickPreview: ((ImageCrimeLocation) -> Unit)? = null
    var onClickEdit: ((ImageCrimeLocation) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setImageCrimeLocations(imageCrimeLocations: ArrayList<ImageCrimeLocation>?) {
        this.imageCrimeLocations.apply {
            clear()
            addAll(imageCrimeLocations!!)
            notifyDataSetChanged()
        }
    }

    fun getImageCrimeLocationSize(): Int {
        return imageCrimeLocations.size
    }

    override fun getItemCount(): Int {
        return imageCrimeLocations.size
    }

    override fun createFragment(position: Int): Fragment {
        val imageCrimeLocation = imageCrimeLocations[position]
        return ImageSliderFragment(
            imageCrimeLocation = imageCrimeLocation,
            isCanEdit = isCanEdit,
            onClickPreview = {
                onClickPreview?.invoke(imageCrimeLocation)
            },
            onClickEdit = {
                onClickEdit?.invoke(imageCrimeLocation)
            }
        )
    }
}