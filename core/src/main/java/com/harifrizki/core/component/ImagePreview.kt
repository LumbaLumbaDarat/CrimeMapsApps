package com.harifrizki.core.component

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.harifrizki.core.databinding.ImagePreviewBinding
import com.harifrizki.core.utils.doGlide

class ImagePreview(
    var alertDialog: AlertDialog? = null
) {
    var binding: ImagePreviewBinding? = null

    fun create(context: Context?) {
        val builder: AlertDialog.Builder =
            context.let { AlertDialog.Builder(it!!) }
        binding =
            ImagePreviewBinding.inflate(
                LayoutInflater.from(context)
            )
        builder.setView(binding!!.root)

        alertDialog = builder.create()
        alertDialog!!.setCancelable(true)
        alertDialog!!.window?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
    }

    fun setImageToPreview(context: Context?,
                          imagePath: String?,
                          url: String?) {
        binding?.apply {
            ivImagePreview.scaleType = ImageView.ScaleType.CENTER_CROP
            doGlide(context,
                ivImagePreview,
                imagePath,
                useLoadingResize = true,
                url = url)
        }
    }

    fun setImageToPreview(context: Context?,
                          imageUri: Uri?) {
        binding?.apply {
            ivImagePreview.scaleType = ImageView.ScaleType.CENTER_CROP
            doGlide(context,
                ivImagePreview,
                imageUri,
                useLoadingResize = true)
        }
    }

    fun show() {
        alertDialog?.show()
    }

    fun dismiss() {
        alertDialog?.dismiss()
    }
}