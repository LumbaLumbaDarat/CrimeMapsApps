package com.harifrizki.core.component

import com.harifrizki.core.databinding.SearchBinding
import com.harifrizki.core.utils.makeSpannable

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