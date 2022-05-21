package com.harifrizki.crimemapsapps.ui.module

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.harifrizki.crimemapsapps.R
import com.harifrizki.crimemapsapps.data.remote.response.ErrorResponse
import com.harifrizki.crimemapsapps.databinding.ActivityConnectionErrorBinding
import com.harifrizki.crimemapsapps.ui.component.BaseActivity
import com.harifrizki.crimemapsapps.utils.Error.*
import com.harifrizki.crimemapsapps.utils.*

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
                            tvTitleMessage.text = errorResponse?.errorCode?.plus(SPACE_STRING)?.plus(errorResponse?.errorMessage)
                            tvMessage.text = makeSpannable(
                                true,
                                getString(R.string.message_error_network,
                                    errorResponse?.errorUrl,
                                    errorResponse?.errorMessage,
                                    errorResponse?.errorTime),
                                SPAN_REGEX,
                                Color.BLACK)
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