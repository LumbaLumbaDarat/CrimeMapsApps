package com.harifrizki.crimemapsapps.ui.component

import com.harifrizki.crimemapsapps.databinding.SearchBinding
import com.harifrizki.crimemapsapps.utils.makeSpannable

class Search {
    private var binding: SearchBinding? = null
    var onClickSearch: ((String?) -> Unit)? = null

    fun create(binding: SearchBinding?) {
        this.binding = binding
        this.binding?.apply {
            ibSearch.setOnClickListener {
                onClickSearch?.invoke(
                    tieSearch.text?.toString()?.trim()
                )
            }
        }
    }

    fun hint(hint: String?) {
        binding?.apply {
            tilSearch.hint = makeSpannable(isSpanBold = true, hint)
        }
    }
}