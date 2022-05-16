package com.harifrizki.crimemapsapps.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.harifrizki.crimemapsapps.R
import com.harifrizki.crimemapsapps.data.remote.response.ErrorResponse
import com.harifrizki.crimemapsapps.databinding.ActivityConnectionErrorBinding
import com.harifrizki.crimemapsapps.utils.ERROR_MESSAGE
import com.harifrizki.crimemapsapps.utils.ERROR_RESPONSE
import com.harifrizki.crimemapsapps.utils.IS_AFTER_ERROR
import com.harifrizki.crimemapsapps.utils.errorMessage

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

        binding.apply {
            tvMessage.text = if (resources.getBoolean(R.bool.app_debug_mode))
                errorMessage(message, errorResponse)
            else getString(R.string.message_error_general)
            btnBack.setOnClickListener {
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        Intent().apply {
            putExtra(IS_AFTER_ERROR, true)
            setResult(RESULT_OK, this)
        }
        super.onBackPressed()
    }
}