package com.harifrizki.crimemapsapps.ui.component

import android.app.Dialog
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.harifrizki.crimemapsapps.databinding.BottomOptionBinding
import com.harifrizki.crimemapsapps.model.Menu
import com.harifrizki.crimemapsapps.ui.adapter.MenuAdapter

class BottomOption(
    var title: String? = null,
    var options: ArrayList<Menu>? = null
) : BottomSheetDialogFragment() {
    var binding: BottomOptionBinding? = null

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