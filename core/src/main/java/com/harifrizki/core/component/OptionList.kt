package com.harifrizki.core.component

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import com.harifrizki.core.adapter.MenuAdapter
import com.harifrizki.core.databinding.LayoutListBinding
import com.harifrizki.core.model.Menu

class OptionList(
    var popupWindow: PopupWindow? = null
) {
    private var binding: LayoutListBinding? = null
    var onClick: ((Menu?) -> Unit)? = null

    fun create(context: Context?) {
        binding = LayoutListBinding.inflate(LayoutInflater.from(context))
        popupWindow = PopupWindow(
            binding?.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        ).apply {
            animationStyle = android.R.style.Animation_Dialog
        }
    }

    fun setBackground(drawable: Drawable?) {
        binding?.clBackgroundLayoutList?.background = drawable
    }

    fun setMenus(options: ArrayList<Menu>?) {
        binding?.rvList?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = MenuAdapter().
            apply {
                menus = options
                onClickMenu = {
                    onClick?.invoke(it)
                }
            }
        }
    }

    fun show(view: View?, xOff: Int?, yOff: Int?) {
        popupWindow?.showAsDropDown(view, xOff!!, yOff!!)
    }

    fun dismiss() {
        popupWindow?.dismiss()
    }
}