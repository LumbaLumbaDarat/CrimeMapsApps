package com.harifrizki.crimemapsapps.ui.component

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.harifrizki.crimemapsapps.R
import com.harifrizki.crimemapsapps.databinding.BottomInputBinding
import com.harifrizki.crimemapsapps.utils.textInputEditTextIsEmpty
import com.lumbalumbadrt.colortoast.ColorToast

class BottomInput(
    var activity: AppCompatActivity? = null,
    var hint: String? = null,
    var input: String? = null,
    var buttonNegative: String? = null,
    var buttonPositive: String? = null
): BottomSheetDialogFragment() {
    private var binding: BottomInputBinding? = null
    var onClickPositive: ((String?) -> Unit?)? = null
    var onClickNegative: (() -> Unit)? = null

    override fun setupDialog(dialog: Dialog, style: Int) {
        binding = BottomInputBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding!!.root)

        binding?.apply {
            tilBottomInput.hint = hint
            if (!TextUtils.isEmpty(input))
                tieBottomInput.setText(input)

            btnPositiveBottomInput.text = buttonPositive
            btnNegativeBottomInput.setOnClickListener(onClickListener())

            btnNegativeBottomInput.text = buttonNegative
            btnPositiveBottomInput.setOnClickListener(onClickListener())
        }
    }

    private fun onClickListener() = View.OnClickListener {
        when (it.id)
        {
            R.id.btn_negative_bottom_input -> {
                onClickNegative?.invoke()
            }
            R.id.btn_positive_bottom_input -> {
                validateInput()
            }
        }
    }

    private fun validateInput() {
        if (!textInputEditTextIsEmpty(binding!!.tieBottomInput))
            onClickPositive?.invoke(
                binding!!.tieBottomInput.text.toString().trim())
        else ColorToast.roundLineWarning(
            activity,
            context?.resources?.getString(R.string.app_name),
            context?.resources?.getString(R.string.message_error_empty_non_spannable, hint),
            Toast.LENGTH_SHORT
        );
    }
}