package com.harifrizki.crimemapsapps.ui.module.crimelocation.form

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.harifrizki.crimemapsapps.databinding.ActivityFormCrimeLocationBinding

class FormCrimeLocationActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityFormCrimeLocationBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}