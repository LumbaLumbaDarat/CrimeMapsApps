package com.harifrizki.crimemapsapps.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.harifrizki.crimemapsapps.R
import com.harifrizki.crimemapsapps.data.remote.response.ErrorResponse
import com.harifrizki.crimemapsapps.databinding.ActivityConnectionErrorBinding
import com.harifrizki.crimemapsapps.utils.*

class ConnectionErrorActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityConnectionErrorBinding.inflate(layoutInflater)
    }

    private var errorResponse: ErrorResponse? = null
    private var message: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (intent != null) {
            message = intent.getStringExtra(ERROR_MESSAGE)
            errorResponse = intent.getParcelableExtra(ERROR_RESPONSE)
        }

        if (resources.getBoolean(R.bool.app_debug_mode))
        {
            if (errorResponse?.errorThrow == null)
                binding.apply {
                    tvTitleMessage.text = errorResponse?.errorCode?.plus(SPACE_STRING)?.plus(errorResponse?.errorMessage)
                    tvMessage.text = makeSpannable(
                        true,
                        getString(R.string.message_error_network,
                            errorResponse?.errorUrl,
                            errorResponse?.errorMessage,
                            errorResponse?.errorTime),
                        SPAN_REGEX,
                        Color.BLACK)
                }
            else binding.apply {
                tvTitleMessage.text = errorResponse?.errorMessage
                tvMessage.text = errorResponse?.errorThrow
            }
        } else {
            binding.apply {
                tvTitleMessage.text = getString(R.string.message_error_title_general)
                tvMessage.text = getString(R.string.message_error_general)
            }
        }

        binding.btnBack.setOnClickListener { onBackPressed() }
    }

    override fun onBackPressed() {
        Intent().apply {
            putExtra(IS_AFTER_ERROR, true)
            setResult(RESULT_OK, this)
        }
        super.onBackPressed()
    }
}