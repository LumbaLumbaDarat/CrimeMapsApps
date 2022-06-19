package com.harifrizki.crimemapsapps.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harifrizki.core.component.MenuAreaDetail
import com.harifrizki.core.databinding.LayoutMenuAreaDetailBinding
import com.harifrizki.core.utils.getMaxShimmerList
import com.harifrizki.core.utils.layoutStartDrawableShimmer
import com.harifrizki.core.utils.widgetStartDrawableShimmer

class MenuAreaDetailAdapter(
    var isShimmer: Boolean?
): RecyclerView.Adapter<MenuAreaDetailAdapter.HolderView>() {
    var menuAreaDetails: ArrayList<MenuAreaDetail> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun addMenuAreaDetails(menuAreaDetails: ArrayList<MenuAreaDetail>) {
        this.menuAreaDetails.apply {
            clear()
            addAll(menuAreaDetails)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HolderView {
        return HolderView(
            LayoutMenuAreaDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ), parent.context
        )
    }

    override fun onBindViewHolder(holder: HolderView, position: Int) {
        val menuAreaDetail: MenuAreaDetail = if (!isShimmer!!)
            menuAreaDetails[position]
        else MenuAreaDetail()
        holder.bind(menuAreaDetail, isShimmer)
    }

    override fun getItemCount(): Int {
        return if (isShimmer!!) getMaxShimmerList()
        else menuAreaDetails.size
    }

    inner class HolderView(
        private val binding: LayoutMenuAreaDetailBinding,
        private val context: Context?) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(menuAreaDetail: MenuAreaDetail, isShimmer: Boolean?) = with(binding)
        {
            if (!isShimmer!!)
            {
                menuAreaDetail.bindingDetail = this
                menuAreaDetail.context = context
                menuAreaDetail.apply {
                    create()
                }
            } else {
                layoutStartDrawableShimmer(
                    arrayOf(
                        clBackgroundMenuAreaDetail
                    ), context!!
                )
                widgetStartDrawableShimmer(
                    arrayOf(
                        ivIconToday,
                        ivIconMonth
                    ), context
                )
                widgetStartDrawableShimmer(
                    arrayOf(
                        tvNameAreaDetail,
                        tvDateAreaDetail,
                        tvLabelCountToday,
                        tvLabelCountMonth,
                        tvCountDataToday,
                        tvCountDataMonth
                    ), context
                )
            }
        }
    }
}