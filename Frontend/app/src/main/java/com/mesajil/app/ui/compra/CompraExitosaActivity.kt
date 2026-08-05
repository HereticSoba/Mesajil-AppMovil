package com.mesajil.app.ui.compra

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mesajil.app.databinding.ActivityCompraExitosaBinding
import android.content.Intent
import com.mesajil.app.MainActivity
import com.mesajil.app.ui.pedidos.HistorialPedidosActivity

class CompraExitosaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCompraExitosaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompraExitosaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, systemBars.top, 0, systemBars.bottom)
            insets
        }
        cargarDatosCompra()
        configurarBotones()
    }

    private fun cargarDatosCompra() {
        val idPedido = intent.getIntExtra(
            "ID_PEDIDO",
            0
        )
        val monto = intent.getDoubleExtra(
            "MONTO",
            0.0
        )
        val tipoEntrega = intent.getStringExtra(
            "TIPO_ENTREGA"
        ).orEmpty()
        val direccionEntrega = intent.getStringExtra(
            "DIRECCION_ENTREGA"
        )
        binding.txtPedido.text = "Pedido #$idPedido"
        binding.txtTotal.text = "Total: S/. %.2f".format(monto)
        if (tipoEntrega.equals(
                "Delivery",
                ignoreCase = true
            )
        ) {
            binding.txtTipoEntrega.text = "Entrega a domicilio"
            binding.txtLugarEntrega.text = direccionEntrega.orEmpty()
        } else {
            binding.txtTipoEntrega.text = "Recojo en tienda"
            binding.txtLugarEntrega.text = "Mesajil - Compuplaza Lima"
        }
    }

    private fun configurarBotones() {
        binding.btnVerPedidos.setOnClickListener {
            val intentInicio = Intent(
                this,
                MainActivity::class.java
            ).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intentInicio)

            val intentPedidos = Intent(
                this,
                HistorialPedidosActivity::class.java
            )
            startActivity(intentPedidos)
            finish()
        }

        binding.btnVolverInicio.setOnClickListener {
            val intentInicio = Intent(
                this,
                MainActivity::class.java
            ).apply {
                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intentInicio)
            finish()
        }
    }
}