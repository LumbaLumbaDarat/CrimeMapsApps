package com.harifrizki.crimemapsapps.ui.component.imageslider

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.harifrizki.crimemapsapps.model.ImageCrimeLocation
import com.harifrizki.crimemapsapps.model.ImageResource

class ImagePagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private var imageCrimeLocations: ArrayList<ImageCrimeLocation> = arrayListOf()
    var onClick: ((ImageCrimeLocation) -> Unit)? = null

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
        return ImageSliderFragment(
            imageCrimeLocation = imageCrimeLocations[position],
            onClick = {
                onClick?.invoke(imageCrimeLocations[position])
            }
        )
    }
}