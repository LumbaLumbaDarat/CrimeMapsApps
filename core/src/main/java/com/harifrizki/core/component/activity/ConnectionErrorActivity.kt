package com.harifrizki.core.component.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.harifrizki.core.R
import com.harifrizki.core.data.remote.response.ErrorResponse
import com.harifrizki.core.databinding.ActivityConnectionErrorBinding
import com.harifrizki.core.utils.Error.*
import com.harifrizki.core.utils.*
import com.harifrizki.core.utils.ERROR_RESPONSE
import com.harifrizki.core.utils.ERROR_STATE
import com.harifrizki.core.utils.IS_AFTER_ERROR
import com.harifrizki.core.utils.makeSpannable

class ConnectionErrorActivity : BaseActivity() {

    private val binding by lazy {
        ActivityConnectionErrorBinding.inflate(layoutInflater)
    }

    private var map: HashMap<String, Any>? = null
    private var error: Error? = null
    private var errorResponse: ErrorResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)

        map = getMap(intent)
        error = map!![ERROR_STATE] as Error
        when (error) {
            IS_NO_NETWORK -> {
                with(binding)
                {
                    tvTitleMessage.text = getString(R.string.message_no_title_network)
                    tvMessage.text = getString(R.string.message_no_network)
                }
            }
            IS_API_RESPONSE -> {
                errorResponse = map!![ERROR_RESPONSE] as ErrorResponse
                if (resources.getBoolean(R.bool.app_debug_mode))
                {
                    with(binding)
                    {
                        if (errorResponse?.errorThrow == null)
                        {
                            tvTitleMessage.text = getString(R.string.label_plus_two_string,
                                errorResponse?.errorCode,
                                errorResponse?.errorMessage)
                            tvMessage.text = makeSpannable(
                                isSpanBold = true,
                                getString(R.string.message_error_network,
                                    errorResponse?.errorUrl,
                                    errorResponse?.errorMessage,
                                    errorResponse?.errorTime),
                                color = Color.BLACK)
                        } else
                        {
                            tvTitleMessage.text = errorResponse?.errorMessage
                            tvMessage.text = errorResponse?.errorThrow
                        }
                    }
                } else {
                    with(binding) {
                        tvTitleMessage.text = getString(R.string.message_error_title_general)
                        tvMessage.text = getString(R.string.message_error_general)
                    }
                }
            }
            else -> {}
        }
        binding.btnBack.setOnClickListener { onBackPressed() }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    { }

    override fun onBackPressed() {
        Intent().apply {
            putExtra(IS_AFTER_ERROR, true)
            setResult(RESULT_OK, this)
        }
        super.onBackPressed()
    }
}