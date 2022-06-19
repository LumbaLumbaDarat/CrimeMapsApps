package com.harifrizki.core.component

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ImageView
import com.harifrizki.core.databinding.ParentAreaBinding
import com.harifrizki.core.utils.ParentAreaAction
import com.harifrizki.core.utils.ParentAreaAction.*
import com.harifrizki.core.utils.ZERO
import com.harifrizki.core.utils.makeSpannable

class ParentArea(
    var title: String? = null,
    var content: String? = null,
    var contentBackground: Int? = ZERO,
    var parentAreaAction: ParentAreaAction? = PARENT_AREA_ACTION_NONE,
    var iconRightAction: Int? = ZERO,
    var backgroundRightAction: Int? = ZERO,
    var iconLeftAction: Int? = ZERO,
    var backgroundLeftAction: Int? = ZERO
) {
    private var context: Context? = null
    private var binding: ParentAreaBinding? = null

    var onActionRight: (() -> Unit)? = null
    var onActionLeft: (() -> Unit)? = null

    fun init(context: Context?, binding: ParentAreaBinding?) {
        this.context = context
        this.binding = binding
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun create() {
        binding?.apply {
            tvTitleParentArea.text = makeSpannable(isSpanBold = true, title)
            if (content != null)
                tvParentArea.text = makeSpannable(isSpanBold = true, content)
            if (contentBackground != ZERO)
                llBackgroundContent.background =
                    contentBackground?.let { context?.resources?.getDrawable(it, null) }
            when (parentAreaAction) {
                PARENT_AREA_ICON_RIGHT -> {
                    whoIsVisible(
                        right = View.VISIBLE,
                        left = View.GONE
                    )
                    ivActionRightParentArea.apply {
                        iconRightAction?.let { setImageResource(it) }
                        isAction(this, false)
                    }
                }
                PARENT_AREA_ACTION_RIGHT -> {
                    whoIsVisible(
                        right = View.VISIBLE,
                        left = View.GONE
                    )
                    ivActionRightParentArea.apply {
                        iconRightAction?.let { setImageResource(it) }
                        isAction(this, true)
                        setOnClickListener { onActionRight?.invoke() }
                    }
                }
                PARENT_AREA_ICON_LEFT -> {
                    whoIsVisible(
                        right = View.GONE,
                        left = View.VISIBLE
                    )
                    ivActionLeftParentArea.apply {
                        iconLeftAction?.let { setImageResource(it) }
                        isAction(this, false)
                    }
                }
                PARENT_AREA_ACTION_LEFT -> {
                    whoIsVisible(
                        right = View.GONE,
                        left = View.VISIBLE
                    )
                    ivActionLeftParentArea.apply {
                        iconLeftAction?.let { setImageResource(it) }
                        isAction(this, true)
                        setOnClickListener { onActionLeft?.invoke() }
                    }
                }
                PARENT_AREA_ICON_BOTH -> {
                    whoIsVisible(
                        right = View.VISIBLE,
                        left = View.VISIBLE
                    )
                    ivActionRightParentArea.apply {
                        iconRightAction?.let { setImageResource(it) }
                        isAction(this, false)
                    }
                    ivActionLeftParentArea.apply {
                        iconLeftAction?.let { setImageResource(it) }
                        isAction(this, false)
                    }
                }
                PARENT_AREA_ACTION_BOTH -> {
                    whoIsVisible(
                        right = View.VISIBLE,
                        left = View.VISIBLE
                    )
                    ivActionRightParentArea.apply {
                        iconRightAction?.let { setImageResource(it) }
                        isAction(this, true)
                        setOnClickListener { onActionRight?.invoke() }
                    }
                    ivActionLeftParentArea.apply {
                        iconLeftAction?.let { setImageResource(it) }
                        isAction(this, true)
                        setOnClickListener { onActionLeft?.invoke() }
                    }
                }
                else -> {
                    ivActionRightParentArea.visibility = View.GONE
                    ivActionLeftParentArea.visibility = View.GONE
                }
            }
        }
    }

    @JvmName("setNewContent")
    fun setContent(content: String?) {
        binding?.apply {
            tvParentArea.text = makeSpannable(isSpanBold = true, content)
        }
    }

    private fun whoIsVisible(right: Int?, left: Int?) {
        binding?.apply {
            ivActionRightParentArea.visibility = right!!
            ivActionLeftParentArea.visibility = left!!
        }
    }

    private fun isAction(imageView: ImageView?, isAction: Boolean?) {
        imageView?.apply {
            isFocusable = isAction!!
            isClickable = isAction
        }
    }
}