package com.mesajil.app.ui.producto

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mesajil.app.databinding.ActivityAdministrarProductosBinding

class AdministrarProductosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdministrarProductosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdministrarProductosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                view.paddingLeft,
                systemBars.top,
                view.paddingRight,
                systemBars.bottom
            )
            insets
        }
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.cardAgregarProducto.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    NuevoProductoActivity::class.java
                )
            )
        }
        binding.cardModificarProducto.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    AdminProductosActivity::class.java
                )
            )
        }
        binding.cardGestionarImagenes.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    AdminImagenesProductoActivity::class.java
                )
            )
        }
    }
}