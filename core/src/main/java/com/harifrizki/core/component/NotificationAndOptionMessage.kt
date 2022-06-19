package com.harifrizki.core.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import com.harifrizki.core.databinding.NotificationAndOptionMessageBinding
import com.harifrizki.core.utils.makeSpannable

class NotificationAndOptionMessage {
    private var context: Context? = null
    private var binding: NotificationAndOptionMessageBinding? = null
    var onClickPositive: (() -> Unit)? = null
    var onClickNegative: (() -> Unit)? = null

    private var buttons: Array<Button>? = null

    fun create(context: Context?,
               binding: NotificationAndOptionMessageBinding?) {
        this.context = context
        this.binding = binding
        this.binding?.apply {
            buttons = arrayOf(
                btnPositive, btnNegative
            )
            btnPositive.setOnClickListener { onClickPositive?.invoke() }
            btnNegative.setOnClickListener { onClickNegative?.invoke() }
        }
    }

    fun title(title: String?) {
        binding?.tvTitle?.text = makeSpannable(
            isSpanBold = true,
            title,
            color = Color.BLACK)
    }

    fun message(message: String?) {
        binding?.tvMessage?.text = makeSpannable(
            isSpanBold = true,
            message,
            color = Color.BLACK)
    }

    fun animation(animation: String?, useAnimation: Boolean?) {
        binding?.ltEmpty?.apply {
            setAnimation(animation)
            if (useAnimation!!)
                playAnimation()
            else pauseAnimation()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun background(context: Context?, background: Int?) {
        binding?.llBackground?.background = background?.let { context?.getDrawable(it) }
    }

    fun buttonPositive(color: Int?) {
        binding?.btnPositive?.backgroundTintList =
            ContextCompat.getColorStateList(context!!, color!!)
    }

    fun buttonPositive(buttonPositive: String?) {
        binding?.btnPositive?.text = makeSpannable(true, buttonPositive)
    }

    fun buttonNegative(color: Int?) {
        binding?.btnNegative?.setTextColor(color!!)
    }

    fun buttonNegative(buttonNegative: String?) {
        binding?.btnNegative?.text = makeSpannable(true, buttonNegative)
    }

    fun useButton(use: Boolean?) {
        buttons?.forEach {
            if (use!!) it.visibility = View.VISIBLE
            else it.visibility = View.GONE
        }
    }

    fun show() {
        binding?.root?.visibility = View.VISIBLE
    }

    fun dismiss() {
        binding?.root?.visibility = View.GONE
    }
}