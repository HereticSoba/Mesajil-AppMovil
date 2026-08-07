package com.mesajil.app.ui.pedidos

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mesajil.app.databinding.ActivityHistorialPedidosBinding
import com.mesajil.app.viewmodel.PedidoViewModel
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HistorialPedidosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistorialPedidosBinding
    private val viewModel: PedidoViewModel by viewModels()
    private lateinit var adapter: PedidoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistorialPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                view.paddingLeft,
                systemBars.top,
                view.paddingRight,
                view.paddingBottom
            )
            insets
        }

        adapter = PedidoAdapter(mutableListOf()){pedido ->
            val intent = Intent(
                this,
                DetallePedidoActivity::class.java
            )
            intent.putExtra("ID_PEDIDO", pedido.idPedido)
            startActivity(intent)
        }
        binding.rvPedidos.layoutManager = LinearLayoutManager(this)
        binding.rvPedidos.adapter = adapter
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        observarPedidos()
        viewModel.obtenerMisPedidos()
    }

    private fun observarPedidos() {
        viewModel.pedidos.observe(this) {
            adapter.actualizarLista(it)
        }
    }
}