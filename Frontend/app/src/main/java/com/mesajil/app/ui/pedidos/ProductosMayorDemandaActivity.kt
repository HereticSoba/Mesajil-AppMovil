package com.mesajil.app.ui.pedidos

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mesajil.app.adapters.ProductoDemandaAdapter
import com.mesajil.app.databinding.ActivityProductosMayorDemandaBinding
import com.mesajil.app.viewmodel.PedidoViewModel
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProductosMayorDemandaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductosMayorDemandaBinding
    private val viewModel: PedidoViewModel by viewModels()
    private lateinit var adapter: ProductoDemandaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductosMayorDemandaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )
            view.setPadding(
                view.paddingLeft,
                systemBars.top,
                view.paddingRight,
                view.paddingBottom
            )
            insets
        }
        configurarLista()
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        cargarProductos()
    }

    private fun configurarLista() {
        adapter = ProductoDemandaAdapter(emptyList())
        binding.rvProductosDemanda.layoutManager = LinearLayoutManager(this)
        binding.rvProductosDemanda.adapter = adapter
    }

    private fun cargarProductos() {
        viewModel.obtenerProductosMayorDemanda(
            onSuccess = { productos ->
                runOnUiThread {
                    adapter.actualizarLista(
                        productos
                    )
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
}