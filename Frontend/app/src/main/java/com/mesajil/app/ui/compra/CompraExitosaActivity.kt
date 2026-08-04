package com.mesajil.app.ui.compra

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mesajil.app.R
import com.mesajil.app.databinding.ActivityCompraExitosaBinding

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
//        Pendiente conectar con pedidos
        }
        binding.btnVolverInicio.setOnClickListener {
//            Pendiente conectar con el home
        }
    }
}