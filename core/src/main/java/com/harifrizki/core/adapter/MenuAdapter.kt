package com.harifrizki.core.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.harifrizki.core.databinding.ItemMenuBinding
import com.harifrizki.core.model.Menu
import com.harifrizki.core.utils.EMPTY_STRING
import com.harifrizki.core.utils.checkBuildOS

class MenuAdapter: RecyclerView.Adapter<MenuAdapter.HolderView>() {
    var menus: ArrayList<Menu>? = ArrayList()
    var onClickMenu: ((Menu) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun addMenus(menus: ArrayList<Menu>?) {
        this.menus?.apply {
            clear()
            addAll(menus!!)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        return HolderView(
            ItemMenuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ), parent.context
        )
    }

    override fun onBindViewHolder(holder: HolderView, position: Int) {
        val menu = menus!![position]
        holder.apply {
            bind(menu)
            itemView.setOnClickListener {
                onClickMenu?.invoke(menu)
            }
        }
    }

    override fun getItemCount(): Int {
        return menus!!.size
    }

    inner class HolderView(
        private val binding: ItemMenuBinding,
        private val context: Context?):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(menu: Menu?) = with(binding)
        {
            if (menu?.visibility!!)
            {
                tvNameMenu.apply {
                    text = menu.name
                    if (checkBuildOS(Build.VERSION_CODES.M))
                        menu.nameColor?.let {
                            setTextColor(ContextCompat.getColor(context!!, it)) }
                    else menu.nameColor?.let { setTextColor(context!!.getColor(it)) }
                }
                if (menu.useAnimation != EMPTY_STRING)
                {
                    ltMenu.apply {
                        setAnimation(menu.useAnimation)
                        visibility = View.VISIBLE
                    }
                    ivIconMenu.visibility = View.GONE
                } else {
                    ivIconMenu.apply {
                        menu.useIcon?.let { setImageResource(it) }
                        menu.iconColor?.let {
                            ContextCompat.getColor(
                                context!!,
                                it
                            )
                        }?.let {
                            setColorFilter(
                                it, android.graphics.PorterDuff.Mode.MULTIPLY)
                        }
                    }
                }
                menu.background?.let { llBackgroundMenu.setBackgroundResource(it) }
            }
        }
    }
}