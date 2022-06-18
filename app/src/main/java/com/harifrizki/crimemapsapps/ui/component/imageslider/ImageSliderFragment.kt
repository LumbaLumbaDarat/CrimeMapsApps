package com.harifrizki.crimemapsapps.ui.component.imageslider

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.harifrizki.crimemapsapps.R
import com.harifrizki.crimemapsapps.databinding.ActivityProfileBinding
import com.harifrizki.crimemapsapps.databinding.FragmentImageSliderBinding
import com.harifrizki.crimemapsapps.model.ImageCrimeLocation
import com.harifrizki.crimemapsapps.model.ImageResource
import com.harifrizki.crimemapsapps.utils.PreferencesManager
import com.harifrizki.crimemapsapps.utils.URL_CONNECTION_API_IMAGE_ADMIN
import com.harifrizki.crimemapsapps.utils.URL_CONNECTION_API_IMAGE_CRIME_LOCATION
import com.harifrizki.crimemapsapps.utils.doGlide

class ImageSliderFragment(
    var imageCrimeLocation: ImageCrimeLocation?,
    var onClick: ((ImageCrimeLocation) -> Unit)? = null) : Fragment() {
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
            doGlide(
                view.context,
                ivPhoto,
                imageName = imageCrimeLocation?.imageCrimeLocationName,
                url = PreferencesManager
                    .getInstance(view.context)
                    .getPreferences(URL_CONNECTION_API_IMAGE_CRIME_LOCATION))
            ivEditPhoto.setOnClickListener { onClick?.invoke(imageCrimeLocation!!) }
        }
    }
}