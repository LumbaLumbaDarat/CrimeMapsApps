package com.harifrizki.core.component

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.harifrizki.core.databinding.LoadingBinding
import com.harifrizki.core.utils.makeSpannable

class Loading(
    var alertDialog: AlertDialog? = null
) {
    var binding: LoadingBinding? = null

    fun create(context: Context?) {
        val builder: AlertDialog.Builder =
            context.let { AlertDialog.Builder(it!!) }
        binding =
            LoadingBinding.inflate(
                LayoutInflater.from(context)
            )
        builder.setView(binding!!.root)

        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog!!.window?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
    }

    fun setMessage(isSpanBold: Boolean?, message: String?, color: Int?) {
        binding?.tvMessageLoading?.text = makeSpannable(
            isSpanBold = isSpanBold,
            message,
            color = color
        )
    }

    fun show() {
        alertDialog?.show()
    }

    fun dismiss() {
        alertDialog?.dismiss()
    }
}