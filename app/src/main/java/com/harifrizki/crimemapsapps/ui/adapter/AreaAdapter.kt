package com.harifrizki.crimemapsapps.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harifrizki.crimemapsapps.databinding.ItemAreaBinding
import com.harifrizki.crimemapsapps.model.Province
import com.harifrizki.crimemapsapps.utils.*
import com.harifrizki.crimemapsapps.utils.MenuAreaType.*

class AreaAdapter(
    var context: Context?,
    var menuAreaType: MenuAreaType?,
    var isShimmer: Boolean?
) : RecyclerView.Adapter<AreaAdapter.HolderView>() {

    var areas: ArrayList<Any?>? = ArrayList()

    var onClickDelete: ((Any?) -> Unit)? = null
    var onClickArea: ((Any?) -> Unit)? = null

    @JvmName("initializeAreas")
    @SuppressLint("NotifyDataSetChanged")
    fun setAreas(areas: ArrayList<Any>?) {
        this.areas?.apply {
            clear()
            addAll(areas!!)
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAreas(areas: ArrayList<Any>?) {
        this.areas?.apply {
            addAll(areas!!)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        return HolderView(
            ItemAreaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ), parent.context
        )
    }

    override fun onBindViewHolder(holder: HolderView, position: Int) {
        val area: Any?
        if (!isShimmer!!) {
            area = areas!![position]
            holder.apply {
                binding.apply {
                    ivDeleteArea.setOnClickListener {
                        onClickDelete?.invoke(area)
                    }
                }
                itemView.setOnClickListener {
                    onClickArea?.invoke(area)
                }
            }
        } else area = Any()
        holder.bind(area, menuAreaType, isShimmer)
    }

    override fun getItemCount(): Int {
        return if (isShimmer!!) MAX_ITEM_LIST_SHIMMER
        else areas!!.size
    }

    class HolderView(
        val binding: ItemAreaBinding,
        private val context: Context?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            area: Any?,
            menuAreaType: MenuAreaType?,
            isShimmer: Boolean?
        ) {
            binding.apply {
                if (!isShimmer!!) {
                    when (menuAreaType) {
                        MENU_AREA_PROVINCE_ID -> {
                            val province = getModel(area, Province::class.java)
                            binding.apply {
                                tvNameArea.text = province.provinceName
                                tvCreatedByArea.text = makeSpannable(
                                    text = getCreateBy(
                                        context,
                                        province.createdBy?.adminName
                                    )
                                )
                                tvCreatedDateArea.text = makeSpannable(
                                    text = getCreateAt(context, province.createdDate)
                                )
                            }
                        }
                        else -> {}
                    }
                } else {
                    layoutStartDrawableShimmer(
                        arrayOf(
                            llBackgroundArea
                        ), context
                    )
                    widgetStartDrawableShimmer(
                        arrayOf(
                            ivIcArea,
                            ivIcCreatedByArea,
                            ivIcCreatedDateArea,
                            ivDeleteArea
                        ), context
                    )
                    widgetStartDrawableShimmer(
                        arrayOf(
                            tvNameArea, tvCreatedByArea, tvCreatedDateArea
                        ), context
                    )
                }
            }
        }
    }
}