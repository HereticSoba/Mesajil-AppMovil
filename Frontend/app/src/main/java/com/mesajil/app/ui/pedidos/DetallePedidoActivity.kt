package com.mesajil.app.ui.pedidos

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mesajil.app.databinding.ActivityDetallePedidoBinding
import com.mesajil.app.viewmodel.PedidoViewModel
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding

class DetallePedidoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetallePedidoBinding
    private val viewModel: PedidoViewModel by viewModels()
    private lateinit var adapter: DetallePedidoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallePedidoBinding.inflate(layoutInflater)
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

        cargarDetalle(idPedido)
        binding.btnCancelarPedido.setOnClickListener {
            cancelarPedido(idPedido)
        }
    }

    private fun cargarDetalle(idPedido: Int) {
        viewModel.obtenerDetallePedido(
            idPedido = idPedido,
            onSuccess = { pedido ->

                runOnUiThread {

                    binding.txtPedido.text =
                        "Pedido #${pedido.idPedido}"

                    binding.txtEstado.text =
                        "Estado: ${pedido.estadoPedido}"

                    binding.txtFecha.text =
                        "Fecha: ${pedido.fechaPedido}"

                    binding.txtEntrega.text =
                        "Tipo de entrega: ${pedido.tipoEntrega}"

                    binding.txtLugar.text =
                        pedido.direccionEntrega
                            ?: pedido.tiendaRecojo.orEmpty()

                    binding.txtSubtotal.text =
                        "Subtotal: S/. %.2f".format(
                            pedido.productos.sumOf { it.subtotal }
                        )

                    binding.txtEnvio.text =
                        "Envío: S/. %.2f".format(
                            pedido.costoEnvio
                        )

                    binding.txtTotal.text =
                        "Total: S/. %.2f".format(
                            pedido.total
                        )

                    binding.txtOrden.text =
                        "Orden MP: ${pedido.idOrdenMercadoPago}"

                    adapter.actualizarLista(
                        pedido.productos
                    )

                    binding.btnCancelarPedido.visibility =
                        if (pedido.estadoPedido.equals(
                                "Pendiente",
                                ignoreCase = true
                            )
                        ) {
                            android.view.View.VISIBLE
                        } else {
                            android.view.View.GONE
                        }
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

    private fun cancelarPedido(idPedido: Int) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Cancelar pedido")
            .setMessage("¿Estás seguro de que deseas cancelar este pedido?")
            .setNegativeButton("No", null)
            .setPositiveButton("Sí, cancelar") { _, _ ->
                viewModel.cancelarPedido(
                    idPedido = idPedido,
                    onSuccess = {
                        runOnUiThread {
                            android.widget.Toast.makeText(
                                this,
                                "Pedido cancelado correctamente.",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    },
                    onError = { mensaje ->
                        runOnUiThread {
                            android.widget.Toast.makeText(
                                this,
                                mensaje,
                                android.widget.Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                )
            }.show()
    }
}