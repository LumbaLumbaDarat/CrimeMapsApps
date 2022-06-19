package com.harifrizki.crimemapsapps.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.harifrizki.core.R
import com.harifrizki.core.component.imageslider.DotSlider
import com.harifrizki.core.component.imageslider.ImagePagerAdapter
import com.harifrizki.core.model.CrimeLocation
import com.harifrizki.core.utils.*
import com.harifrizki.crimemapsapps.databinding.ItemCrimeLocationBinding

class CrimeLocationAdapter(
    var context: Context?,
    var isShimmer: Boolean?
) : RecyclerView.Adapter<CrimeLocationAdapter.HolderView>() {
    var crimeLocations: ArrayList<CrimeLocation>? = ArrayList()

    var onClickDelete: ((CrimeLocation?) -> Unit)? = null
    var onClickCrimeLocation: ((CrimeLocation?) -> Unit)? = null

    @JvmName("initializeCrimeLocations")
    @SuppressLint("NotifyDataSetChanged")
    fun setCrimeLocations(crimeLocations: ArrayList<CrimeLocation>?) {
        this.crimeLocations?.apply {
            clear()
            addAll(crimeLocations!!)
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addCrimeLocations(crimeLocations: ArrayList<CrimeLocation>?) {
        this.crimeLocations?.apply {
            addAll(crimeLocations!!)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HolderView {
        return HolderView(
            ItemCrimeLocationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ), parent.context
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: HolderView, position: Int) {
        val crimeLocation: CrimeLocation
        if (!isShimmer!!) {
            crimeLocation = crimeLocations!![position]
            holder.apply {
                binding.apply {
                    tvLink.setOnClickListener {
                        onClickCrimeLocation?.invoke(crimeLocation)
                    }
                    ivDeleteLocation.setOnClickListener {
                        onClickDelete?.invoke(crimeLocation)
                    }
                }
                itemView.setOnClickListener { onClickCrimeLocation?.invoke(crimeLocation) }
            }
        }
        else crimeLocation = CrimeLocation()
        holder.bind(crimeLocation, isShimmer)
    }

    override fun getItemCount(): Int {
        return if (isShimmer!!) getMaxShimmerList()
        else crimeLocations!!.size
    }

    class HolderView(
        val binding: ItemCrimeLocationBinding,
        private val context: Context?
    ) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables", "NotifyDataSetChanged")
        fun bind(
            crimeLocation: CrimeLocation?,
            isShimmer: Boolean?
        ) {
            binding.apply {
                if (!isShimmer!!) {
                    var currentPage: Int? = ZERO
                    val imagePagerAdapter = ImagePagerAdapter(context as FragmentActivity).
                    apply {
                        isCanDelete = false
                        setImageCrimeLocations(crimeLocation?.imageCrimeLocations)
                        notifyDataSetChanged()
                    }
                    iImageLocation.apply {
                        val dotSlider = DotSlider(
                            context = context).
                        apply {
                            linearLayout = llDotSlider
                            sizePage = imagePagerAdapter.getImageCrimeLocationSize()
                            addBottomIcons(ZERO)
                        }
                        vpImage.apply {
                            adapter = imagePagerAdapter
                            updateLayoutParams {
                                height = resources.getDimensionPixelOffset(
                                    R.dimen.image_height_rectangular_default)
                            }
                            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                                override fun onPageSelected(position: Int) {
                                    super.onPageSelected(position)
                                    dotSlider.addBottomIcons(position)
                                    currentPage = position
                                }
                            })
                        }
                        root.visibility = View.VISIBLE
                    }
                    ivImageLocation.visibility = View.GONE
                    tvNameLocation.text = crimeLocation?.crimeMapsName
                    tvAddressLocation.text = crimeLocation?.crimeMapsAddress
                    tvCreatedBy.text = makeSpannable(
                        text = getCreateBy(
                            context,
                            crimeLocation?.createdBy?.adminName
                        )
                    )
                    tvCreatedDate.text = makeSpannable(
                        text = getCreateAt(context, crimeLocation?.createdDate)
                    )
                }
                else {
                    layoutStartDrawableShimmer(
                        arrayOf(
                            llCrimeLocationBackground
                        ), context
                    )
                    widgetStartDrawableShimmer(
                        arrayOf(
                            ivImageLocation,
                            ivIconLocation,
                            ivDeleteLocation,
                            ivIconCreatedBy,
                            ivIconCreatedDate,
                        ), context
                    )
                    widgetStartDrawableShimmer(
                        arrayOf(
                            tvNameLocation,
                            tvAddressLocation,
                            tvLink,
                            tvCreatedBy,
                            tvCreatedDate
                        ), context
                    )
                }
            }
        }
    }
}