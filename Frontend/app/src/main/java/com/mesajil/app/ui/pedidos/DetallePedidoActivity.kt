package com.mesajil.app.ui.pedidos

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mesajil.app.databinding.ActivityDetallePedidoBinding
import com.mesajil.app.viewmodel.PedidoViewModel

class DetallePedidoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetallePedidoBinding
    private val viewModel: PedidoViewModel by viewModels()
    private lateinit var adapter: DetallePedidoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallePedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = DetallePedidoAdapter(mutableListOf())

        binding.rvProductos.layoutManager =
            LinearLayoutManager(this)

        binding.rvProductos.adapter = adapter

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        val idPedido = intent.getIntExtra(
            "ID_PEDIDO",
            0
        )

        observarDetalle()

        viewModel.obtenerDetallePedido(idPedido)
    }

    private fun observarDetalle() {

        viewModel.detallePedido.observe(this) { pedido ->

            binding.txtPedido.text =
                "Pedido #${pedido.idPedido}"

            binding.txtEstado.text =
                "Estado: ${pedido.estadoPedido}"

            binding.txtFecha.text =
                "Fecha: ${pedido.fechaPedido}"

            binding.txtEntrega.text =
                "Entrega: ${pedido.tipoEntrega}"

            binding.txtLugar.text =
                pedido.direccionEntrega
                    ?: pedido.tiendaRecojo.orEmpty()

            binding.txtEnvio.text =
                "Envío: S/. %.2f".format(pedido.costoEnvio)

            binding.txtTotal.text =
                "Total: S/. %.2f".format(pedido.total)

            binding.txtOrden.text =
                "Orden MP: ${pedido.idOrdenMercadoPago}"

            adapter.actualizarLista(
                pedido.productos
            )
        }
    }
}