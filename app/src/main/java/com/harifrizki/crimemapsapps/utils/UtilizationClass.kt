package com.harifrizki.crimemapsapps.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.harifrizki.crimemapsapps.R
import com.harifrizki.crimemapsapps.data.remote.response.*
import com.harifrizki.crimemapsapps.model.*
import com.orhanobut.logger.Logger
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

@SuppressLint("UseCompatLoadingForDrawables")
private fun drawableBackgroundLightGrayForShimmer(context: Context?): Drawable {
    return context?.resources?.getDrawable(
        R.drawable.frame_background_light_gray_shimmer,
        null
    )!!
}

@SuppressLint("UseCompatLoadingForDrawables")
private fun drawableBackgroundGrayForShimmer(context: Context?): Drawable {
    return context?.resources?.getDrawable(
        R.drawable.frame_background_gray_shimmer,
        null
    )!!
}

private fun colorTransparentForShimmer(context: Context?): Int {
    return context?.resources?.getColor(
        R.color.transparent, null
    )!!
}

private val requestOptions =
    RequestOptions().centerCrop().placeholder(R.drawable.animation_loading_image)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .priority(Priority.HIGH)
        .dontAnimate()
        .dontTransform()

fun getVersion(context: Context?): String {
    return try {
        context?.packageManager?.getPackageInfo(context.packageName, 0)!!.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        Logger.e("getVersion: ".plus(e.message.toString()))
        EMPTY_STRING
    }
}

fun <T> getModel(any: Any?, modelClass: Class<T>): T {
    return when {
        modelClass.isAssignableFrom(ProvinceResponse::class.java) -> {
            (any as ProvinceResponse) as T
        }
        modelClass.isAssignableFrom(Province::class.java) -> {
            (any as Province) as T
        }
        modelClass.isAssignableFrom(CityResponse::class.java) -> {
            (any as CityResponse) as T
        }
        modelClass.isAssignableFrom(City::class.java) -> {
            (any as City) as T
        }
        modelClass.isAssignableFrom(SubDistrictResponse::class.java) -> {
            (any as SubDistrictResponse) as T
        }
        modelClass.isAssignableFrom(SubDistrict::class.java) -> {
            (any as SubDistrict) as T
        }
        modelClass.isAssignableFrom(UrbanVillageResponse::class.java) -> {
            (any as UrbanVillageResponse) as T
        }
        modelClass.isAssignableFrom(UrbanVillage::class.java) -> {
            (any as UrbanVillage) as T
        }
        modelClass.isAssignableFrom(MessageResponse::class.java) -> {
            (any as MessageResponse) as T
        }
        else -> {
            Logger.e("Unknown modelClass class: " + modelClass.name)
            throw Throwable("Unknown modelClass class: " + modelClass.name)
        }
    }
}

fun getCreateBy(context: Context?, createdBy: String?): String? {
    return if (createdBy == null)
        context?.getString(R.string.label_null_created_and_updated)
    else context?.getString(
        R.string.label_create_by,
        createdBy
    )
}

fun getCreateAt(context: Context?, createdAt: String?): String? {
    return if (createdAt == null)
        context?.getString(R.string.label_null_created_and_updated)
    else context?.getString(
        R.string.label_create_at,
        createdAt
    )
}

fun getNameForImageTemp(context: Context?, imageType: ImageType?): String {
    return context?.getString(R.string.app_name)!!.replace(SPACE_STRING, UNDER_LINE_STRING)
        .plus(UNDER_LINE_STRING).plus(imageType?.name).plus(UNDER_LINE_STRING)
        .plus(context.getString(R.string.temp)).uppercase()
}

fun isNetworkConnected(context: Context?): Boolean {
    val result: Boolean
    val connectivityManager: ConnectivityManager =
        context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities =
        connectivityManager.activeNetwork ?: return false
    val activeNetwork =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    result = when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
    return result
}

fun checkBuildOS(buildOs: Int?): Boolean {
    return Build.VERSION.SDK_INT >= buildOs!!
}

fun textInputEditTextIsEmpty(textInputEditText: TextInputEditText?): Boolean {
    return TextUtils.isEmpty(
        textInputEditText?.text.toString().trim()
    )
}

fun isValidEmail(target: CharSequence?): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(target!!).matches()
}

fun isValidPasswordAndConfirmPassword(
    password: String?,
    confirmPassword: String?
): Boolean {
    return password.equals(confirmPassword, false)
}

fun toRequestBody(value: String?, mediaType: String?): RequestBody {
    return value?.toRequestBody(mediaType?.toMediaType())!!
}

fun toRequestBody(file: File?, mediaType: String?): RequestBody {
    return file?.asRequestBody(mediaType?.toMediaType())!!
}

fun toMultipartBody(file: File?, name: String?, mediaType: String?): MultipartBody.Part {
    return MultipartBody.Part.createFormData(
        name!!,
        file?.name,
        toRequestBody(file, mediaType)
    )
}

fun makeSpannable(
    isSpanBold: Boolean? = true,
    text: String?,
    regex: String? = SPAN_REGEX,
    color: Int? = ZERO
): SpannableStringBuilder {
    val stringBuffer = StringBuffer()
    val spannableStringBuilder = SpannableStringBuilder()

    val pattern = Pattern.compile(regex!!)
    val matcher = pattern.matcher(text!!)

    while (matcher.find()) {
        stringBuffer.setLength(0)
        val group = matcher.group()

        val spanText = group.substring(1, group.length - 1)
        matcher.appendReplacement(stringBuffer, spanText)

        spannableStringBuilder.append(stringBuffer.toString())
        val start = spannableStringBuilder.length - spanText.length

        if (isSpanBold!!)
            spannableStringBuilder.setSpan(
                StyleSpan(Typeface.BOLD),
                start,
                spannableStringBuilder.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

        if (color != ZERO)
            spannableStringBuilder.setSpan(
                color?.let { ForegroundColorSpan(it) },
                start,
                spannableStringBuilder.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
    }

    stringBuffer.setLength(0)
    matcher.appendTail(stringBuffer)
    spannableStringBuilder.append(stringBuffer.toString())
    return spannableStringBuilder
}

@SuppressLint("SimpleDateFormat")
fun Date.localDateTimeToString(): String {
    return SimpleDateFormat("dd MMMM yyyy").format(this)
}

@SuppressLint("UseCompatLoadingForDrawables")
fun setActivate(admin: Admin?, imageView: ImageView?) {
    imageView?.apply {
        if (admin?.isActive!!) {
            background =
                context.getDrawable(R.drawable.button_red_ripple_white)
            setImageResource(R.drawable.ic_round_lock_24)
        } else {
            background =
                context.getDrawable(R.drawable.button_dark_green_ripple_white)
            setImageResource(R.drawable.ic_round_lock_open_24)
        }
    }
}

fun layoutStartDrawableShimmer(
    linearLayouts: Array<LinearLayout>?,
    context: Context?
) {
    linearLayouts?.forEach {
        it.apply {
            background =
                drawableBackgroundLightGrayForShimmer(
                    context
                )
        }
    }
}

fun layoutStartDrawableShimmer(
    constraintLayouts: Array<ConstraintLayout>?,
    context: Context?
) {
    constraintLayouts?.forEach {
        it.apply {
            background =
                drawableBackgroundLightGrayForShimmer(
                    context
                )
        }
    }
}

fun widgetStartDrawableShimmer(
    textViews: Array<TextView>?,
    context: Context?
) {
    textViews?.forEach {
        it.apply {
            background =
                drawableBackgroundGrayForShimmer(
                    context
                )
            setTextColor(colorTransparentForShimmer(context))
            setCompoundDrawables(
                null,
                null,
                null,
                null
            )
        }
    }
}

fun widgetStartDrawableShimmer(
    imageViews: Array<ImageView>?,
    context: Context?
) {
    imageViews?.forEach {
        it.apply {
            background = drawableBackgroundGrayForShimmer(
                context
            )
            setColorFilter(
                ContextCompat.getColor(
                    context!!,
                    R.color.transparent
                ),
                PorterDuff.Mode.MULTIPLY
            )
        }
    }
}

fun widgetStartDrawableShimmer(
    imageButtons: Array<ImageButton>?,
    context: Context?
) {
    imageButtons?.forEach {
        it.apply {
            background = drawableBackgroundGrayForShimmer(
                context
            )
            setColorFilter(
                ContextCompat.getColor(
                    context!!,
                    R.color.transparent
                ),
                PorterDuff.Mode.MULTIPLY
            )
        }
    }
}

fun widgetStartDrawableShimmer(
    buttons: Array<Button>?,
    context: Context?
) {
    buttons?.forEach {
        it.apply {
            backgroundTintList = ContextCompat.getColorStateList(
                context!!, R.color.light_gray
            )
            setTextColor(colorTransparentForShimmer(context))
        }
    }
}

fun widgetStartDrawableShimmer(
    textInputEditTexts: Array<TextInputEditText>?
) {
    textInputEditTexts?.forEach {
        it.apply {
            visibility = View.GONE
        }
    }
}

fun widgetStartDrawableShimmer(
    textInputLayouts: Array<TextInputLayout>?,
    context: Context?
) {
    textInputLayouts?.forEach {
        it.apply {
            background = drawableBackgroundGrayForShimmer(context)
        }
    }
}

fun doGlide(
    context: Context?,
    imageView: ImageView?,
    imageName: String?,
    imageError: Int? = R.drawable.ic_round_broken_image_primary_24
) {
    val url =
        PreferencesManager.getInstance(context!!).getPreferences(URL_CONNECTION_API_IMAGE_ADMIN)
    if (url?.isNotEmpty()!!)
        imageView?.let {
            Glide.with(context).applyDefaultRequestOptions(requestOptions)
                .load(url.plus(imageName)).error(imageError)
                .into(it)
        }
    else
        imageView?.let {
            Glide.with(context).load(imageError)
                .into(it)
        }
}

fun doGlide(
    context: Context?,
    imageView: ImageView?,
    uri: Uri?,
    imageError: Int? = R.drawable.ic_round_broken_image_primary_24
) {
    Glide.with(context!!).applyDefaultRequestOptions(requestOptions)
        .load(uri).error(imageError)
        .into(imageView!!)
}
