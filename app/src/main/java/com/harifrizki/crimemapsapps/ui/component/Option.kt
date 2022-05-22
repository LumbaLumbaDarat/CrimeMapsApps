package com.harifrizki.crimemapsapps.ui.component

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.harifrizki.crimemapsapps.databinding.OptionBinding
import com.harifrizki.crimemapsapps.utils.*

class Option(
    var alertDialog: AlertDialog? = null
) {
    var binding: OptionBinding? = null
    var onClickPositive: (() -> Unit)? = null
    var onClickNegative: (() -> Unit)? = null

    fun create(context: Context?) {
        val builder: AlertDialog.Builder =
            context.let { AlertDialog.Builder(it!!) }
        binding = OptionBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding!!.root)

        binding?.apply {
            btnPositive.setOnClickListener {
                onClickPositive?.invoke()
            }
            btnNegative.setOnClickListener {
                onClickNegative?.invoke()
            }
        }

        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog!!.window?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
    }

    fun titleOption(titleOption: String?) {
        binding?.tvTitleOption?.text = titleOption
    }

    fun option(option: String?, color: Int = Color.BLACK) {
        binding?.tvMessageOption?.text =
            option?.let { makeSpannable(true, it, SPAN_REGEX, color) }
    }

    fun buttonPositive(buttonPositive: String?) {
        binding?.btnPositive?.text = buttonPositive
    }

    fun buttonNegative(buttonNegative: String?) {
        binding?.btnNegative?.text = buttonNegative
    }

    fun optionAnimation(optionAnimation: String?) {
        binding?.ltOption?.setAnimation(optionAnimation)
    }

    fun show() {
        alertDialog?.show()
    }

    fun dismiss() {
        alertDialog?.dismiss()
    }
}