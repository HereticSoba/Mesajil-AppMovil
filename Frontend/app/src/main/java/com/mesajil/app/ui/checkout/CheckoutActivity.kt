package com.mesajil.app.ui.checkout

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mesajil.app.databinding.ActivityCheckoutBinding
import com.mesajil.app.preferences.SessionManager
import com.mesajil.app.repository.DetalleCarritoRepository
import kotlinx.coroutines.launch
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import com.mesajil.app.ui.pago.PagoActivity

class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckoutBinding
    private val detalleCarritoRepository = DetalleCarritoRepository()
    private lateinit var sessionManager: SessionManager
    private var subtotal = 0.00
    private var costoEnvio = 0.00
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )
            view.setPadding(
                0, systemBars.top, 0, systemBars.bottom
            )
            insets
        }
        sessionManager = SessionManager(this)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        configurarTipoEntrega()
        cargarResumen()
    }

    private fun configurarTipoEntrega() {
        binding.rgTipoEntrega.setOnCheckedChangeListener { _, checkedId ->
            binding.txtErrorTipoEntrega.visibility = View.GONE
            when (checkedId) {
                binding.rbDelivery.id -> {
                    binding.layoutDireccion.visibility = View.VISIBLE
                    binding.cardRecojo.visibility = View.GONE

                    costoEnvio = 10.00
                    actualizarTotales()
                }

                binding.rbRecojo.id -> {
                    binding.layoutDireccion.visibility = View.GONE
                    binding.cardRecojo.visibility = View.VISIBLE
                    costoEnvio = 0.00
                    actualizarTotales()
                }
            }
        }
        binding.btnContinuar.setOnClickListener {
            validarCheckout()

        }
    }

    private fun cargarResumen() {
        val idCarrito = sessionManager.obtenerIdCarrito()
        if (idCarrito == 0) {
            Toast.makeText(
                this,
                "No se encontró el carrito.",
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return
        }
        lifecycleScope.launch {
            val detalles = detalleCarritoRepository.obtenerPorCarrito(idCarrito)
            if (detalles.isNullOrEmpty()) {
                Toast.makeText(
                    this@CheckoutActivity,
                    "El carrito está vacío.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
                return@launch
            }
            subtotal = detalles.sumOf {
                it.subtotal
            }
            actualizarTotales()
        }
    }

    private fun actualizarTotales() {
        val total = subtotal + costoEnvio
        binding.txtSubtotal.text = "S/. %.2f".format(subtotal)
        binding.txtEnvio.text = if (costoEnvio == 0.0) {
            "Gratis"
        } else {
            "S/. %.2f".format(costoEnvio)
        }
        binding.txtTotal.text = "S/. %.2f".format(total)
    }

    private fun validarCheckout() {
        if (binding.rgTipoEntrega.checkedRadioButtonId == -1) {
            binding.txtErrorTipoEntrega.text = "Seleccione un tipo de entrega"
            binding.txtErrorTipoEntrega.visibility = View.VISIBLE
            return
        }
        binding.txtErrorTipoEntrega.visibility = View.GONE
        val tipoEntrega = when (
            binding.rgTipoEntrega.checkedRadioButtonId
        ) {
            binding.rbDelivery.id -> "Delivery"
            binding.rbRecojo.id -> "Recojo"
            else -> return
        }

        if (tipoEntrega == "Delivery") {
            val direccion = binding.edtDireccion.text
                ?.toString()
                ?.trim()
                .orEmpty()
            if (direccion.isBlank()) {
                binding.layoutDireccion.error =
                    "Ingrese la dirección de entrega."
                return
            }
            binding.layoutDireccion.error = null
        }
        val direccion = if (tipoEntrega == "Delivery") {
            binding.edtDireccion.text
                ?.toString()
                ?.trim()
                .orEmpty()
        } else {
            null
        }
        val total = subtotal + costoEnvio
        val intent = Intent(
            this,
            PagoActivity::class.java
        )
        intent.putExtra("TIPO_ENTREGA", tipoEntrega)
        intent.putExtra("DIRECCION_ENTREGA", direccion)
        intent.putExtra("TOTAL", total)
        startActivity(intent)
    }
}