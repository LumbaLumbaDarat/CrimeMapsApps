package com.harifrizki.crimemapsapps.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harifrizki.crimemapsapps.databinding.LayoutMenuAreaDetailBinding
import com.harifrizki.crimemapsapps.ui.component.MenuAreaDetail
import com.harifrizki.crimemapsapps.utils.MAX_ITEM_LIST_SHIMMER
import com.harifrizki.crimemapsapps.utils.layoutStartDrawableShimmer
import com.harifrizki.crimemapsapps.utils.widgetStartDrawableShimmer

class MenuAreaDetailAdapter(
    var context: Context?,
    var isShimmer: Boolean?,
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
        context = parent.context
        return HolderView(
            LayoutMenuAreaDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: HolderView, position: Int) {
        val menuAreaDetail: MenuAreaDetail = if (!isShimmer!!)
            menuAreaDetails[position]
        else MenuAreaDetail()
        holder.bind(context, menuAreaDetail, isShimmer)
    }

    override fun getItemCount(): Int {
        return if (isShimmer!!) MAX_ITEM_LIST_SHIMMER
        else menuAreaDetails.size
    }

    class HolderView(private val binding: LayoutMenuAreaDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context?, menuAreaDetail: MenuAreaDetail, isShimmer: Boolean?) {
            if (!isShimmer!!)
            {
                menuAreaDetail.bindingDetail = binding
                menuAreaDetail.context = context
                menuAreaDetail.apply {
                    create()
                }
            } else {
                layoutStartDrawableShimmer(
                    arrayOf(
                        binding.clBackgroundMenuAreaDetail
                    ), context!!
                )
                widgetStartDrawableShimmer(
                    arrayOf(
                        binding.ivIconToday,
                        binding.ivIconMonth
                    ), context
                )
                widgetStartDrawableShimmer(
                    arrayOf(
                        binding.tvNameAreaDetail,
                        binding.tvDateAreaDetail,
                        binding.tvLabelCountToday,
                        binding.tvLabelCountMonth,
                        binding.tvCountDataToday,
                        binding.tvCountDataMonth
                    ), context
                )
            }
        }
    }
}