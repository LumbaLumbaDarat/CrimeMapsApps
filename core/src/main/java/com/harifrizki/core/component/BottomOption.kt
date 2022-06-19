package com.harifrizki.core.component

import android.app.Dialog
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.harifrizki.core.adapter.MenuAdapter
import com.harifrizki.core.databinding.BottomOptionBinding
import com.harifrizki.core.model.Menu

class BottomOption(
    var title: String? = null,
    var options: ArrayList<Menu>? = null
) : BottomSheetDialogFragment() {
    private var binding: BottomOptionBinding? = null
    var onClick: ((Menu?) -> Unit)? = null

    override fun setupDialog(dialog: Dialog, style: Int) {
        binding = BottomOptionBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding!!.root)

        binding?.apply {
            tvTitleBottomOption.text = title
            rvOption.apply {
                layoutManager =
                    LinearLayoutManager(context)
                adapter = MenuAdapter().apply {
                    this.menus = options
                    onClickMenu = {
                        onClick?.invoke(it)
                    }
                }
            }
            btnCancelBottomOption.setOnClickListener {
                dialog.dismiss()
            }
        }
    }
}