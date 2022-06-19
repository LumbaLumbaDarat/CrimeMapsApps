package com.harifrizki.crimemapsapps.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harifrizki.core.component.MenuArea
import com.harifrizki.core.databinding.LayoutMenuAreaBinding
import com.harifrizki.core.utils.getMaxShimmerList
import com.harifrizki.core.utils.layoutStartDrawableShimmer
import com.harifrizki.core.utils.widgetStartDrawableShimmer

class MenuAreaAdapter(
    var isShimmer: Boolean?
): RecyclerView.Adapter<MenuAreaAdapter.HolderView>() {
    var menuAreas: ArrayList<MenuArea>? = ArrayList()
    var onClickItem: ((MenuArea) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun addMenuAreas(menuAreas: ArrayList<MenuArea>?) {
        this.menuAreas?.apply {
            clear()
            addAll(menuAreas!!)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        return HolderView(
            LayoutMenuAreaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ), parent.context
        )
    }

    override fun onBindViewHolder(holder: HolderView, position: Int) {
        val menuArea: MenuArea = if (!isShimmer!!) {
            menuAreas!![position]
        } else MenuArea()
        holder.bind(menuArea, isShimmer)
    }

    override fun getItemCount(): Int {
        return if (isShimmer!!) getMaxShimmerList()
        else menuAreas!!.size
    }

    inner class HolderView(
        private val binding: LayoutMenuAreaBinding,
        private val context: Context?):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(menuArea: MenuArea, isShimmer: Boolean?) =
            with(binding)
        {
            if (!isShimmer!!)
            {
                menuArea.binding = this
                menuArea.context = context
                menuArea.apply {
                    create().apply {
                        onClickMenuArea = {
                            onClickItem?.invoke(it!!)
                        }
                    }
                }
            } else {
                layoutStartDrawableShimmer(
                    arrayOf(
                        clBackgroundMenuArea
                    ), context!!
                )
                widgetStartDrawableShimmer(
                    arrayOf(
                        ivIconMenuArea
                    ), context
                )
                widgetStartDrawableShimmer(
                    arrayOf(
                        tvTitleMenuArea,
                        tvCountMenuArea
                    ), context
                )
            }
        }
    }
}