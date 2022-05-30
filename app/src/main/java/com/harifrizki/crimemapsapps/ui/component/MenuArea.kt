package com.harifrizki.crimemapsapps.ui.component

import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat
import com.harifrizki.crimemapsapps.databinding.LayoutMenuAreaBinding
import com.harifrizki.crimemapsapps.utils.EMPTY_STRING
import com.harifrizki.crimemapsapps.utils.MenuAreaType
import com.harifrizki.crimemapsapps.utils.ZERO
import com.harifrizki.crimemapsapps.utils.MenuAreaType.*
import com.harifrizki.crimemapsapps.utils.checkBuildOS

open class MenuArea(
    var context: Context?,
    var binding: LayoutMenuAreaBinding?,

    var menuAreaType: MenuAreaType?,
    var titleMenuArea: String?,
    var iconMenuArea: Int?,
    var countMenuArea: Int?,

    var colorTitleMenuArea: Int?,
    var colorIconMenuArea: Int?,
    var colorCountMenuArea: Int?,
    var backgroundMenuArea: Int?
) {
    var onClickMenuArea: ((MenuArea?) -> Unit?)? = null

    constructor() : this(
        null,
        null,

        MENU_NONE,
        EMPTY_STRING,
        ZERO,
        ZERO,

        ZERO,
        ZERO,
        ZERO,
        ZERO
    )

    open fun create() {
        binding?.apply {
            tvTitleMenuArea.apply {
                text = titleMenuArea
                if (checkBuildOS(Build.VERSION_CODES.M))
                    colorTitleMenuArea?.let {
                        setTextColor(ContextCompat.getColor(context, it))
                    }
                else colorTitleMenuArea?.let { setTextColor(context.getColor(it)) }
            }

            tvCountMenuArea.apply {
                text = countMenuArea.toString()
                if (checkBuildOS(Build.VERSION_CODES.M))
                    colorCountMenuArea?.let {
                        setTextColor(ContextCompat.getColor(context, it))
                    }
                else colorCountMenuArea?.let { setTextColor(context.getColor(it)) }
            }

            ivIconMenuArea.apply {
                iconMenuArea?.let { setImageResource(it) }
                colorIconMenuArea?.let {
                    ContextCompat.getColor(
                        context,
                        it
                    )
                }?.let {
                    setColorFilter(
                        it, android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                }
            }

            backgroundMenuArea?.let {
                clBackgroundMenuArea.setBackgroundResource(it)
            }

            clBackgroundMenuArea.setOnClickListener {
                onClickMenuArea?.invoke(
                    MenuArea(
                        context,
                        binding,

                        menuAreaType,
                        titleMenuArea,
                        iconMenuArea,
                        countMenuArea,

                        colorTitleMenuArea,
                        colorIconMenuArea,
                        colorCountMenuArea,
                        backgroundMenuArea
                    )
                )
            }
        }
    }
}