package com.harifrizki.crimemapsapps.ui.component

import com.harifrizki.crimemapsapps.databinding.LayoutMenuAreaDetailBinding
import com.harifrizki.crimemapsapps.utils.EMPTY_STRING
import com.harifrizki.crimemapsapps.utils.ZERO

class MenuAreaDetail(
    var bindingDetail: LayoutMenuAreaDetailBinding?,

    var date: String?,
    var countAreaToday: Int?,
    var countAreaMonth: Int?
) : MenuArea() {

    constructor(): this(
        null,

        EMPTY_STRING,
        ZERO,
        ZERO
    )

    override fun create() {
        bindingDetail?.apply {
            tvNameAreaDetail.text = titleMenuArea
            tvDateAreaDetail.text = date
            tvCountDataToday.text = countAreaToday.toString()
            tvCountDataMonth.text = countAreaMonth.toString()
        }
    }
}