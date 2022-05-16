package com.harifrizki.crimemapsapps.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harifrizki.crimemapsapps.databinding.LayoutMenuAreaBinding
import com.harifrizki.crimemapsapps.ui.component.MenuArea
import com.harifrizki.crimemapsapps.utils.MAX_ITEM_LIST_SHIMMER
import com.harifrizki.crimemapsapps.utils.layoutStartDrawableShimmer
import com.harifrizki.crimemapsapps.utils.widgetStartDrawableShimmer

class MenuAreaAdapter(
    var context: Context?,
    var isShimmer: Boolean?,
): RecyclerView.Adapter<MenuAreaAdapter.HolderView>() {
    var menuAreas: ArrayList<MenuArea>? = ArrayList()
    var onClickMenuArea: ((MenuArea) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun addMenuAreas(menuAreas: ArrayList<MenuArea>?) {
        this.menuAreas?.apply {
            clear()
            addAll(menuAreas!!)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        context = parent.context
        return HolderView(
            LayoutMenuAreaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false)
        )
    }

    override fun onBindViewHolder(holder: HolderView, position: Int) {
        val menuArea: MenuArea
        if (!isShimmer!!)
        {
            menuArea = menuAreas!![position]
            holder.itemView.setOnClickListener {
                onClickMenuArea?.invoke(menuArea)
            }
        } else menuArea = MenuArea()
        holder.bind(context, menuArea, isShimmer)
    }

    override fun getItemCount(): Int {
        return if (isShimmer!!) MAX_ITEM_LIST_SHIMMER
        else menuAreas!!.size
    }

    class HolderView(private val binding: LayoutMenuAreaBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context?, menuArea: MenuArea, isShimmer: Boolean?) {
            if (!isShimmer!!)
            {
                menuArea.binding = binding
                menuArea.context = context
                menuArea.apply {
                    create()
                }
            } else {
                layoutStartDrawableShimmer(
                    arrayOf(
                        binding.clBackgroundMenuArea
                    ), context!!
                )
                widgetStartDrawableShimmer(
                    arrayOf(
                        binding.ivIconMenuArea
                    ), context
                )
                widgetStartDrawableShimmer(
                    arrayOf(
                        binding.tvTitleMenuArea,
                        binding.tvCountMenuArea
                    ), context
                )
            }
        }
    }
}