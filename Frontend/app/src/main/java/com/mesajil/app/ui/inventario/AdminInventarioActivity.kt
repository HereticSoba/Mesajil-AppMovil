package com.mesajil.app.ui.inventario

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mesajil.app.databinding.ActivityAdminInventarioBinding
import com.mesajil.app.repository.ProductoRepository
import com.mesajil.app.viewmodel.InventarioViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class AdminInventarioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminInventarioBinding
    private val viewModel: InventarioViewModel by viewModels()
    private lateinit var adapter: AdminInventarioAdapter
    private val productoRepository = ProductoRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminInventarioBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(
            binding.root
        ) { view, insets ->
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
        adapter = AdminInventarioAdapter(
            mutableListOf(),
            mutableListOf()
        ) { inventario ->
            val intent = Intent(this,FormInventarioActivity::class.java)
            intent.putExtra("ID_INVENTARIO",inventario.idInventario)
            startActivity(intent)
        }
        binding.rvInventario.layoutManager =
            LinearLayoutManager(this)
        binding.rvInventario.adapter = adapter
        binding.cardAgregarInventario.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    FormInventarioActivity::class.java
                )
            )
        }
        cargarInventario()
    }

    private fun cargarInventario() {

        viewModel.obtenerInventarios(

            onSuccess = { inventarios ->

                runOnUiThread {

                    adapter.actualizarLista(inventarios)

                    cargarProductos()
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

    private fun cargarProductos() {
        lifecycleScope.launch {
            try {
                val productos =productoRepository.obtenerProductos()
                adapter.actualizarProductos(productos)
            } catch (e: Exception) {
                Toast.makeText(
                    this@AdminInventarioActivity,
                    "No se pudieron cargar los productos.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized) {
            cargarInventario()
        }
    }
}