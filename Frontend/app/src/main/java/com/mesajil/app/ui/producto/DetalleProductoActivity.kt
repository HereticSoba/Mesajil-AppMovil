package com.mesajil.app.ui.producto

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mesajil.app.databinding.ActivityDetalleProductoBinding
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.mesajil.app.MainActivity
import com.mesajil.app.models.request.DetalleCarritoRequest
import com.mesajil.app.preferences.SessionManager
import com.mesajil.app.repository.DetalleCarritoRepository
import kotlinx.coroutines.launch
import com.bumptech.glide.Glide
import com.mesajil.app.R

class DetalleProductoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetalleProductoBinding
    private var idProducto = 0
    private var nombre = ""
    private var descripcion = ""
    private var precio = 0.0
    private var stock = 0
    private var cantidad = 1
    private var urlImagen: String? = null
    private var marca = ""
    private var modelo = ""
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
        urlImagen = intent.getStringExtra(EXTRA_IMAGEN)
        marca = intent.getStringExtra(EXTRA_MARCA) ?: ""
        modelo = intent.getStringExtra(EXTRA_MODELO) ?: ""
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
        binding.txtMarca.text = "Marca: $marca"
        binding.txtModelo.text = "Modelo: $modelo"
        binding.txtStock.text = when {
            stock <= 0 -> "Sin stock."
            stock <= 5 -> "¡Últimas $stock unidades!"
            else -> "$stock unidades disponibles."
        }
        binding.btnAgregarCarrito.isEnabled = stock > 0
        binding.btnComprar.isEnabled = stock > 0

        if (!urlImagen.isNullOrBlank()) {
            val urlCompleta = "http://192.168.100.54:5228${urlImagen}"
            Glide.with(this)
                .load(urlCompleta)
                .placeholder(R.drawable.logomesajil)
                .error(R.drawable.logomesajil)
                .into(binding.imgProducto)
        } else {
            binding.imgProducto.setImageResource(R.drawable.logomesajil)
        }
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
            agregarAlCarrito(abrirCarrito = false)
        }

        binding.btnComprar.setOnClickListener {
            agregarAlCarrito(abrirCarrito = true)
        }
    }

    private fun agregarAlCarrito(abrirCarrito: Boolean) {
        lifecycleScope.launch {
            if (stock <= 0) {
                Toast.makeText(
                    this@DetalleProductoActivity,
                    "Producto sin stock.",
                    Toast.LENGTH_SHORT
                ).show()
                return@launch
            }
            val idCarrito = sessionManager.obtenerIdCarrito()
            if (idCarrito == 0) {
                Toast.makeText(
                    this@DetalleProductoActivity,
                    "No se encontró el carrito",
                    Toast.LENGTH_SHORT
                ).show()
                return@launch
            }
            val existente = detalleCarritoRepository.obtenerPorCarritoYProducto(
                idCarrito, idProducto
            )
            var operacionExitosa = false
            if (existente == null) {
                if (cantidad > stock) {
                    Toast.makeText(
                        this@DetalleProductoActivity,
                        "No hay suficiente stock disponible.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }
                val request = DetalleCarritoRequest(
                    idCarrito = idCarrito,
                    idProducto = idProducto,
                    cantidad = cantidad,
                    precioUnitario = precio
                )
                val resultado = detalleCarritoRepository.crear(request)
                operacionExitosa = resultado != null
            } else {
                val nuevaCantidad = existente.cantidad + cantidad
                if (nuevaCantidad > stock) {
                    Toast.makeText(
                        this@DetalleProductoActivity,
                        "No hay suficiente stock disponible. Disponible $stock.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }
                val request = DetalleCarritoRequest(
                    idCarrito = existente.idCarrito,
                    idProducto = existente.idProducto,
                    cantidad = nuevaCantidad,
                    precioUnitario = existente.precioUnitario
                )
                operacionExitosa = detalleCarritoRepository.actualizar(
                    existente.idDetalleCarrito,
                    request
                )
            }
            if (!operacionExitosa) {
                Toast.makeText(
                    this@DetalleProductoActivity,
                    "No se pudo agregar el producto.",
                    Toast.LENGTH_SHORT
                ).show()
                return@launch
            }
            if (abrirCarrito) {
                abrirCarrito()
            } else {
                Toast.makeText(
                    this@DetalleProductoActivity,
                    "$cantidad unidad(es) agregadas al carrito.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun abrirCarrito() {
        val intent = Intent(
            this,
            MainActivity::class.java
        )
        intent.putExtra("abrir_carrito", true)
        startActivity(intent)
        finish()
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
        const val EXTRA_IMAGEN = "extra_imagen"
        const val EXTRA_MARCA = "extra_marca"
        const val EXTRA_MODELO = "extra_modelo"
    }
}