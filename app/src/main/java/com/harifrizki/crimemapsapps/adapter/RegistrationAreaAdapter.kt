package com.harifrizki.crimemapsapps.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harifrizki.core.databinding.ItemRegistrationAreaBinding
import com.harifrizki.core.model.RegistrationArea
import com.harifrizki.core.utils.getMaxShimmerList
import com.harifrizki.core.utils.widgetStartDrawableShimmer

class RegistrationAreaAdapter(
    var context: Context?,
    var isShimmer: Boolean?
) : RecyclerView.Adapter<RegistrationAreaAdapter.HolderView>() {
    var registrationAreas: ArrayList<RegistrationArea>? = ArrayList()

    @JvmName("initializeRegistrationAreas")
    @SuppressLint("NotifyDataSetChanged")
    fun setRegistrationAreas(registrationAreas: ArrayList<RegistrationArea>?) {
        this.registrationAreas?.apply {
            clear()
            addAll(registrationAreas!!)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HolderView {
        return HolderView(
            ItemRegistrationAreaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ), parent.context
        )
    }

    override fun onBindViewHolder(holder: HolderView, position: Int) {
        val registrationArea: RegistrationArea =
            if (!isShimmer!!) registrationAreas!![position]
            else RegistrationArea()
        holder.bind(registrationArea, isShimmer)
    }

    override fun getItemCount(): Int {
        return if (isShimmer!!) getMaxShimmerList()
        else registrationAreas!!.size
    }

    class HolderView(
        val binding: ItemRegistrationAreaBinding,
        private val context: Context?
    ) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(
            registrationArea: RegistrationArea?,
            isShimmer: Boolean?
        ) {
            binding.apply {
                if (!isShimmer!!) {
                    tvLabel.text = registrationArea?.label
                    tvArea.text = registrationArea?.areaRegistration
                }
                else {
                    widgetStartDrawableShimmer(
                        arrayOf(
                            tvLabel,
                            tvArea
                        ), context
                    )
                }
            }
        }
    }
}