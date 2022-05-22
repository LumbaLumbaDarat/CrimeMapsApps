package com.harifrizki.crimemapsapps.ui.component

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.harifrizki.crimemapsapps.R
import com.harifrizki.crimemapsapps.databinding.LoadingBinding
import com.harifrizki.crimemapsapps.utils.SPAN_REGEX
import com.harifrizki.crimemapsapps.utils.makeSpannable

class Loading (
    var alertDialog: AlertDialog? = null,
    var message: String? = null
)
{
    fun create(context: Context?) {
        val builder: AlertDialog.Builder =
            context.let { AlertDialog.Builder(it!!) }
        val binding: LoadingBinding =
            LoadingBinding.inflate(
                LayoutInflater.from(context)
            )
        builder.setView(binding.root)
        binding.tvMessageLoading.apply {
            text = if (!TextUtils.isEmpty(message))
                makeSpannable(
                    true,
                    message!!,
                    SPAN_REGEX,
                    Color.BLACK)
            else makeSpannable(
                true,
                context?.getString(R.string.message_loading),
                SPAN_REGEX,
                Color.BLACK)
        }

        alertDialog = builder.create()
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