package com.harifrizki.crimemapsapps.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.harifrizki.crimemapsapps.R
import com.orhanobut.logger.Logger
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

fun getVersion(context: Context?): String {
    return try {
        context?.packageManager?.getPackageInfo(context.packageName, 0)!!.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        Logger.e("getVersion: ".plus(e.message.toString()))
        EMPTY_STRING
    }
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

fun isValidPasswordAndConfirmPassword(password: String?,
                                      confirmPassword: String?
): Boolean {
    return password.equals(confirmPassword, false)
}

fun makeSpannable(isSpanBold: Boolean?,
                  text: String?,
                  regex: String?,
                  color: Int?): SpannableStringBuilder {
    val stringBuffer = StringBuffer()
    val spannableStringBuilder = SpannableStringBuilder()

    val pattern = Pattern.compile(regex!!)
    val matcher = pattern.matcher(text!!)

    while (matcher.find())
    {
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