package com.harifrizki.crimemapsappsuser.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.harifrizki.core.component.imageslider.ImageSlider
import com.harifrizki.core.model.CrimeLocation
import com.harifrizki.core.model.ImageCrimeLocation
import com.harifrizki.core.utils.*
import com.harifrizki.crimemapsappsuser.databinding.ItemNearestCrimeLocationBinding

class CrimeLocationNearestAdapter(
    var context: Context?,
    var isShimmer: Boolean?
) : RecyclerView.Adapter<CrimeLocationNearestAdapter.HolderView>() {
    var crimeLocations: ArrayList<CrimeLocation>? = ArrayList()

    var onClickPreviewImage: ((ImageCrimeLocation) -> Unit)? = null
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
            ItemNearestCrimeLocationBinding.inflate(
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
                }
                itemView.setOnClickListener { onClickCrimeLocation?.invoke(crimeLocation) }
            }
        }
        else crimeLocation = CrimeLocation()
        holder.bind(
            crimeLocation,
            isShimmer,
            onClickPreviewImage = { onClickPreviewImage?.invoke(it)
        })
    }

    override fun getItemCount(): Int {
        return if (isShimmer!!) getMaxShimmerList()
        else crimeLocations!!.size
    }

    class HolderView(
        val binding: ItemNearestCrimeLocationBinding,
        private val context: Context?
    ) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables", "NotifyDataSetChanged")
        fun bind(
            crimeLocation: CrimeLocation?,
            isShimmer: Boolean?,
            onClickPreviewImage: ((ImageCrimeLocation) -> Unit)?
        ) {
            binding.apply {
                if (!isShimmer!!) {
                    ImageSlider()
                        .apply {
                            create(iImageLocation)
                            isCanEdit = false
                            setImageSlider(
                                context as FragmentActivity,
                                crimeLocation?.imageCrimeLocations
                            )
                            onClickPreview = {
                                onClickPreviewImage?.invoke(it)
                            }
                            iImageLocation.root.visibility = View.VISIBLE
                        }
                    ivImageLocation.visibility = View.GONE
                    tvNameLocation.text = crimeLocation?.crimeMapsName
                    tvDistance.text = makeSpannable(
                        isSpanBold = true,
                        getNearestLocation(context, String.format("%.3f", crimeLocation?.distance)),
                        color = Color.RED)
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
                            ivIconCreatedBy,
                            ivIconCreatedDate,
                        ), context
                    )
                    widgetStartDrawableShimmer(
                        arrayOf(
                            tvNameLocation,
                            tvDistance,
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