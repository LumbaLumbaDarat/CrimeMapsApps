package com.harifrizki.crimemapsapps.ui.module.crimelocation.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.harifrizki.crimemapsapps.databinding.ActivityDetailCrimeLocationBinding

class DetailCrimeLocationActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityDetailCrimeLocationBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}