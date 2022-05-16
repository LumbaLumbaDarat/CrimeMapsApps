package com.harifrizki.crimemapsapps.ui.component

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.harifrizki.crimemapsapps.databinding.LoadingBinding

class Loading (
    var alertDialog: AlertDialog? = null,
    var message: String? = null
)
{
    fun create(context: Context) {
        val builder: AlertDialog.Builder? =
            context.let { AlertDialog.Builder(it) }
        val binding: LoadingBinding =
            LoadingBinding.inflate(
                LayoutInflater.from(context)
            )
        builder?.setView(binding.root)

        if (!TextUtils.isEmpty(message))
            binding.tvMessageLoading.text = message

        alertDialog = builder?.create()
        alertDialog!!.setCancelable(false)
        alertDialog!!.window?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
    }

    fun show() {
        alertDialog?.show()
    }

    fun dismiss() {
        alertDialog?.dismiss()
    }
}