package com.harifrizki.core.component

import android.graphics.Color
import android.view.View
import com.harifrizki.core.databinding.EmptyBinding
import com.harifrizki.core.utils.makeSpannable

class Empty {
    private var binding: EmptyBinding? = null

    fun create(binding: EmptyBinding?) {
        this.binding = binding
    }

    fun animation(animationEmpty: String?,
                  useAnimation: Boolean? = true) {
        binding?.ltEmpty?.apply {
            setAnimation(animationEmpty)
            if (useAnimation!!)
                playAnimation()
            else pauseAnimation()
        }
    }

    fun title(title: String?) {
        binding?.apply {
            tvTitleMessageEmpty.text = title
        }
    }

    fun message(message: String?) {
        binding?.apply {
            tvMessageEmpty.text = makeSpannable(
                isSpanBold = true,
                message,
                color = Color.BLACK)
        }
    }

    fun show() {
        binding?.root?.visibility = View.VISIBLE
    }

    fun dismiss() {
        binding?.root?.visibility = View.GONE
    }
}