package com.mesajil.app.ui.producto

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mesajil.app.databinding.ActivityDetalleProductoBinding

class DetalleProductoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetalleProductoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}