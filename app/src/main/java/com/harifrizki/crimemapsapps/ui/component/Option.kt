package com.harifrizki.crimemapsapps.ui.component

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.harifrizki.crimemapsapps.databinding.OptionBinding
import com.harifrizki.crimemapsapps.utils.*

class Option(
    var alertDialog: AlertDialog? = null
) {
    private var context: Context? = null
    private var binding: OptionBinding? = null
    var onClickPositive: (() -> Unit)? = null
    var onClickNegative: (() -> Unit)? = null

    fun create(context: Context?) {
        this.context = context
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

    fun titleOption(titleOption: String?, color: Int? = Color.BLACK) {
        binding?.tvTitleOption?.text = titleOption
    }

    fun option(option: String?, color: Int? = Color.BLACK) {
        binding?.tvMessageOption?.text =
            makeSpannable(isSpanBold = true, option, color = color)
    }

    fun buttonPositive(buttonPositive: String?) {
        binding?.btnPositive?.text = buttonPositive
    }

    fun buttonPositive(color: Int?) {
        binding?.btnPositive?.backgroundTintList =
            ContextCompat.getColorStateList(context!!, color!!)
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