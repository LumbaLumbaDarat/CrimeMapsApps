package com.harifrizki.core.component.imageslider

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.harifrizki.core.databinding.FragmentImageSliderBinding
import com.harifrizki.core.model.ImageCrimeLocation
import com.harifrizki.core.utils.PreferencesManager
import com.harifrizki.core.utils.URL_CONNECTION_API_IMAGE_CRIME_LOCATION
import com.harifrizki.core.utils.doGlide

class ImageSliderFragment(
    var imageCrimeLocation: ImageCrimeLocation?,

    var isCanEdit: Boolean? = null,

    var onClickPreview: ((ImageCrimeLocation) -> Unit)? = null,
    var onClickEdit: ((ImageCrimeLocation) -> Unit)? = null) : Fragment() {
    private val binding by lazy {
        FragmentImageSliderBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            ivPhoto.scaleType = ImageView.ScaleType.FIT_CENTER
            doGlide(
                view.context,
                ivPhoto,
                useLoadingResize = true,
                scaleType = ImageView.ScaleType.CENTER,
                imageName = imageCrimeLocation?.imageCrimeLocationName,
                url = PreferencesManager
                    .getInstance(view.context)
                    .getPreferences(URL_CONNECTION_API_IMAGE_CRIME_LOCATION))
            if (!isCanEdit!!)
                ivEditPhoto.visibility = View.GONE
            else ivEditPhoto
                .setOnClickListener { onClickEdit?.invoke(imageCrimeLocation!!) }
            ivPhoto.setOnClickListener { onClickPreview?.invoke(imageCrimeLocation!!) }
        }
    }
}