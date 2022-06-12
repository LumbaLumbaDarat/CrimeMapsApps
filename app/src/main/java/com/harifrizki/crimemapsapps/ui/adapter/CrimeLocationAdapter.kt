package com.harifrizki.crimemapsapps.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harifrizki.crimemapsapps.R
import com.harifrizki.crimemapsapps.databinding.ItemCrimeLocationBinding
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.model.CrimeLocation
import com.harifrizki.crimemapsapps.utils.*

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
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(
            crimeLocation: CrimeLocation?,
            isShimmer: Boolean?
        ) {
            binding.apply {
                if (!isShimmer!!) {
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
                } else {
                    layoutStartDrawableShimmer(
                        arrayOf(
                            llCrimeLocationBackground
                        ), context
                    )
                    widgetStartDrawableShimmer(
                        arrayOf(
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