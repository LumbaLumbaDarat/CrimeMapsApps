package com.harifrizki.crimemapsapps.ui.component

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.harifrizki.crimemapsapps.R
import com.harifrizki.crimemapsapps.databinding.LoadingBinding
import com.harifrizki.crimemapsapps.utils.makeSpannable

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