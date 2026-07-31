package com.mesajil.app.ui.producto

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mesajil.app.databinding.ActivityDetalleProductoBinding
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.mesajil.app.models.request.DetalleCarritoRequest
import com.mesajil.app.preferences.SessionManager
import com.mesajil.app.repository.DetalleCarritoRepository
import kotlinx.coroutines.launch

class DetalleProductoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetalleProductoBinding
    private var idProducto = 0
    private var nombre = ""
    private var descripcion = ""
    private var precio = 0.0
    private var stock = 0
    private var cantidad = 1
    private lateinit var sessionManager: SessionManager
    private val detalleCarritoRepository = DetalleCarritoRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)
        binding = ActivityDetalleProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        obtenerDatosIntent()
        configurarToolbar()
        configurarInsets()
        mostrarDatosProducto()
        configurarEventos()
        actualizarCantidad()
    }

    private fun obtenerDatosIntent() {
        idProducto = intent.getIntExtra(EXTRA_ID, 0)
        nombre = intent.getStringExtra(EXTRA_NOMBRE) ?: ""
        descripcion = intent.getStringExtra(EXTRA_DESCRIPCION) ?: ""
        precio = intent.getDoubleExtra(EXTRA_PRECIO, 0.0)
        stock = intent.getIntExtra(EXTRA_STOCK, 0)
    }

    private fun configurarToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun configurarInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.scrolldetalleProducto) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                view.paddingLeft,
                systemBars.top,
                view.paddingRight,
                systemBars.bottom
            )
            insets
        }
    }

    private fun mostrarDatosProducto() {
        binding.txtNombre.text = nombre
        binding.txtDescripcion.text = descripcion
        binding.txtPrecio.text = "S/. %.2f".format(precio)
        binding.txtStock.text = when {
            stock <= 0 -> "Sin stock."
            stock <= 5 -> "¡Últimas $stock unidades!"
            else -> "$stock unidades disponibles."
        }
        binding.btnAgregarCarrito.isEnabled = stock > 0
        binding.btnComprar.isEnabled = stock > 0
    }

    private fun configurarEventos() {
        binding.btnMas.setOnClickListener {
            if (cantidad < stock) {
                cantidad++
                actualizarCantidad()
            } else {
                Toast.makeText(
                    this,
                    "Stock máximo alcanzado",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.btnMenos.setOnClickListener {
            if (cantidad > 1) {
                cantidad--
                actualizarCantidad()
            }
        }
        binding.btnAgregarCarrito.setOnClickListener {
            lifecycleScope.launch {
                val idCarrito = sessionManager.obtenerIdCarrito()
                if (idCarrito == 0) {
                    Toast.makeText(
                        this@DetalleProductoActivity,
                        "No se encontró el carrito.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }
                val existente = detalleCarritoRepository.obtenerPorCarritoYProducto(
                    idCarrito,
                    idProducto
                )
                if (existente == null) {
                    val request = DetalleCarritoRequest(
                        idCarrito = idCarrito,
                        idProducto = idProducto,
                        cantidad = cantidad,
                        precioUnitario = precio
                    )
                    val resultado = detalleCarritoRepository.crear(request)
                    if (resultado != null) {
                        Toast.makeText(
                            this@DetalleProductoActivity,
                            "$cantidad unidad(es) agregadas al carrito.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@DetalleProductoActivity,
                            "Error al agregar el producto.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    if (stock <= 0) {
                        Toast.makeText(
                            this@DetalleProductoActivity,
                            "Producto sin stock.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@launch
                    }
                    val nuevaCantidad = existente.cantidad + cantidad
                    if (nuevaCantidad > stock) {
                        Toast.makeText(
                            this@DetalleProductoActivity,
                            "No hay suficiente stock.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@launch
                    }
                    val request = DetalleCarritoRequest(
                        idCarrito = existente.idCarrito,
                        idProducto = existente.idProducto,
                        cantidad = existente.cantidad + cantidad,
                        precioUnitario = existente.precioUnitario
                    )
                    val actualizado = detalleCarritoRepository.actualizar(
                        existente.idDetalleCarrito,
                        request
                    )
                    if (actualizado) {
                        Toast.makeText(
                            this@DetalleProductoActivity,
                            "Cantidad actualizada.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@DetalleProductoActivity,
                            "Error al actualizar el carrito.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.btnComprar.setOnClickListener {
            Toast.makeText(
                this,
                "Redirigiendo a finalización de compra...",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun actualizarCantidad() {
        binding.txtCantidad.text = cantidad.toString()
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NOMBRE = "extra_nombre"
        const val EXTRA_DESCRIPCION = "extra_descripcion"
        const val EXTRA_PRECIO = "extra_precio"
        const val EXTRA_STOCK = "extra_stock"
    }
}