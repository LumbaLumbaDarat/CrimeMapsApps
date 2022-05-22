package com.harifrizki.crimemapsapps.ui.component

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.harifrizki.crimemapsapps.databinding.NotificationBinding
import com.harifrizki.crimemapsapps.utils.*
import com.harifrizki.crimemapsapps.utils.NotificationType.*

class Notification (
    var alertDialog: AlertDialog? = null
) {
    var binding: NotificationBinding? = null
    var onClickButton: (() -> Unit)? = null

    fun create(context: Context?) {
        val builder: AlertDialog.Builder =
            context.let { AlertDialog.Builder(it!!) }
        binding = NotificationBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding!!.root)

        binding?.apply {
            btnNotification.setOnClickListener {
                onClickButton?.invoke()
            }
        }

        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog!!.window?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
    }

    fun titleNotification(titleNotification: String?) {
        binding?.tvTitleNotification?.text = titleNotification
    }

    fun notification(notification: String?, color: Int = Color.BLACK) {
        binding?.tvNotification?.text =
            notification?.let { makeSpannable(true, it, SPAN_REGEX, color) }
    }

    fun buttonTitle(buttonTitle: String?) {
        binding?.btnNotification?.text = buttonTitle
    }

    fun notificationType(notificationType: NotificationType) {
        binding?.ltNotification?.apply {
            when (notificationType)
            {
                NOTIFICATION_ERROR -> {
                    setAnimation(LOTTIE_ERROR_FILE_JSON)
                }
                NOTIFICATION_WARNING -> {
                    setAnimation(LOTTIE_WARNING_FILE_JSON)
                }
                NOTIFICATION_INFORMATION -> {
                    setAnimation(LOTTIE_INFORMATION_JSON)
                }
                NOTIFICATION_SUCCESS -> {
                    setAnimation(LOTTIE_SUCCESS_JSON)
                }
                else -> {}
            }
        }
    }

    fun show() {
        alertDialog?.show()
    }

    fun dismiss() {
        alertDialog?.dismiss()
    }
}