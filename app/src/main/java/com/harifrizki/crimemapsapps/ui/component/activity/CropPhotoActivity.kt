package com.harifrizki.crimemapsapps.ui.component.activity

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import com.harifrizki.crimemapsapps.R
import com.harifrizki.crimemapsapps.databinding.ActivityCropPhotoBinding
import com.harifrizki.crimemapsapps.utils.*
import com.harifrizki.crimemapsapps.utils.ActivityName.Companion.getEnumActivityName
import com.harifrizki.crimemapsapps.utils.ActivityName.Companion.getNameOfActivity
import com.harifrizki.crimemapsapps.utils.ActivityName.*
import com.harifrizki.crimemapsapps.utils.ImageType.*
import com.orhanobut.logger.Logger
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.FileOutputStream
import java.io.IOException

class CropPhotoActivity : BaseActivity() {
    private val binding by lazy {
        ActivityCropPhotoBinding.inflate(layoutInflater)
    }

    private var cropImageView: CropImageView? = null
    private var map: HashMap<String, Any>? = null
    private var fromActivity: ActivityName? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        create(this, resultLauncher)

        @Suppress("DEPRECATION")
        window.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                insetsController?.hide(WindowInsets.Type.statusBars())
            else setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        map = getMap(intent)
        fromActivity = getEnumActivityName(map!![FROM_ACTIVITY].toString())
        cropImageView = binding.civCropPhoto.apply {
            setImageUriAsync(Uri.parse(map!![URI_IMAGE].toString()))
            guidelines = CropImageView.Guidelines.ON
            cropShape = CropImageView.CropShape.RECTANGLE
            setFixedAspectRatio(true)
            isShowProgressBar = true
        }

        binding.apply {
            btnOkCropPhoto.setOnClickListener(onClickListener)
            btnCancelCropPhoto.setOnClickListener(onClickListener)
            ibRotateCropPhoto.setOnClickListener(onClickListener)
        }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {

    }

    private val onClickListener = View.OnClickListener {
        when (it.id)
        {
            R.id.btn_cancel_crop_photo -> { onBackPressed() }
            R.id.ib_rotate_crop_photo -> {
                cropImageView?.rotateImage(ROTATE_DEGREE) }
            R.id.btn_ok_crop_photo -> {
                try {
                    onBackPressed(hashMapOf(
                        FROM_ACTIVITY to getNameOfActivity(CROP_PHOTO),
                        URI_IMAGE to crop()
                    ), directOnBackPressed = true)
                } catch (e: IOException) {
                    Logger.e(e.message.toString())
                    showError(
                        message = e.message.toString(),
                        onClick = { onBackPressed() })
                }
            }
        }
    }

    private fun crop(): Uri {
        val bitmapNew: Bitmap
        val croppedBitmap: Bitmap = cropImageView?.croppedImage as Bitmap

        val outWidth: Int
        val outHeight: Int
        val inWidth: Int = croppedBitmap.width
        val inHeight: Int = croppedBitmap.height

        if (inWidth > inHeight) {
            outWidth = MAX_SIZE
            outHeight = (inHeight * MAX_SIZE) / inWidth
        } else {
            outHeight = MAX_SIZE;
            outWidth = (inWidth * MAX_SIZE) / inHeight
        }

        bitmapNew = Bitmap.createScaledBitmap(
            croppedBitmap, outWidth, outHeight, true
        )

        try
        {
            Thread.sleep(100)
        } catch (e: InterruptedException) {
            Logger.e(e.message.toString())
            showError(
                message = e.message.toString(),
                onClick = { onBackPressed() })
        }

        val fileImageSend = getTempFile(IMAGE_PROFILE)
        try
        {
            val fileOutputStream = FileOutputStream(fileImageSend)
            bitmapNew.compress(
                Bitmap.CompressFormat.JPEG, 100, fileOutputStream
            )
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: IOException) {
            Logger.e(e.message.toString())
            showError(
                message = e.message.toString(),
                onClick = { onBackPressed() })
        }

        return Uri.fromFile(fileImageSend)
    }
}