package com.mesajil.app.ui.producto

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mesajil.app.databinding.ActivityAdminImagenesProductosBinding
import com.mesajil.app.viewmodel.ProductoViewModel

class AdminImagenesProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminImagenesProductosBinding
    private val viewModel: ProductoViewModel by viewModels()
    private lateinit var adapter: AdminProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminImagenesProductosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->

            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

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

        adapter = AdminProductoAdapter(
            mutableListOf()
        ) { producto ->

            val intent = Intent(
                this,
                GestionarImagenProductoActivity::class.java
            )
            intent.putExtra(
                "ID_PRODUCTO",
                producto.idProducto
            )
            startActivity(intent)
        }

        binding.rvProductos.layoutManager =
            LinearLayoutManager(this)

        binding.rvProductos.adapter = adapter

        cargarProductos()
    }

    private fun cargarProductos() {

        viewModel.obtenerProductosAdmin(

            onSuccess = { productos ->

                runOnUiThread {
                    adapter.actualizarLista(productos)
                }
            },

            onError = { mensaje ->

                runOnUiThread {
                    Toast.makeText(
                        this,
                        mensaje,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }
    override fun onResume() {
        super.onResume()
        cargarProductos()
    }
}