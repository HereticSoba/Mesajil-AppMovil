package com.mesajil.app.ui.producto

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mesajil.app.databinding.ActivityDetalleProductoBinding
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mesajil.app.ui.carrito.CarritoItem
import com.mesajil.app.ui.carrito.CarritoManager

class DetalleProductoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetalleProductoBinding
    private var idProducto = 0
    private var nombre = ""
    private var descripcion = ""
    private var precio = 0.0
    private var stock = 0
    private var cantidad = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        binding.txtStock.text = "Disponible: $stock unidades"
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
            val item = CarritoItem(
                idProducto = idProducto,
                nombre = nombre,
                precio = precio,
                cantidad = cantidad
            )
            CarritoManager.agregarProducto(item)
            Toast.makeText(
                this,
                "$cantidad unidad(es) agregadas al carrito.",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.btnComprar.setOnClickListener {
            Toast.makeText(
                this,
                "Compra realizada con éxito.",
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