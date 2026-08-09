package com.mesajil.app.ui.inventario

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mesajil.app.databinding.ActivityFormInventarioBinding
import com.mesajil.app.models.Producto
import com.mesajil.app.models.request.InventarioCreateRequest
import com.mesajil.app.models.request.InventarioUpdateRequest
import com.mesajil.app.repository.ProductoRepository
import com.mesajil.app.viewmodel.InventarioViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FormInventarioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormInventarioBinding
    private val viewModel: InventarioViewModel by viewModels()
    private val productoRepository = ProductoRepository()
    private var idInventarioEditar: Int = 0
    private var idProductoEditar: Int = 0
    private var productos: List<Producto> = emptyList()
    private var productoSeleccionado: Producto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormInventarioBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)
        idInventarioEditar = intent.getIntExtra("ID_INVENTARIO", 0)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )
            view.setPadding(
                view.paddingLeft,
                systemBars.top,
                view.paddingRight,
                systemBars.bottom
            )
            insets
        }
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        cargarProductos()
        if (idInventarioEditar > 0) {
            binding.toolbar.title = "Modificar inventario"
            binding.btnGuardar.text = "Guardar cambios"
            binding.btnEliminar.visibility = android.view.View.VISIBLE
            binding.layoutProducto.visibility = android.view.View.GONE
            binding.layoutProductoEditar.visibility = android.view.View.VISIBLE
            cargarInventario()
        } else {
            binding.toolbar.title = "Nuevo inventario"
            binding.btnGuardar.text = "Registrar inventario"
            binding.layoutProducto.visibility = android.view.View.VISIBLE
            binding.layoutProductoEditar.visibility = android.view.View.GONE
        }
        binding.btnGuardar.setOnClickListener {
            guardarInventario()
        }
        binding.btnEliminar.setOnClickListener {
            eliminarInventario()
        }
    }

    private fun cargarProductos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val lista = productoRepository.obtenerProductos()
                withContext(Dispatchers.Main) {
                    productos = lista
                    val nombres = productos.map {
                        "${it.nombre} - ${it.marca}"
                    }
                    val adapter =
                        ArrayAdapter(
                            this@FormInventarioActivity,
                            android.R.layout.simple_dropdown_item_1line,
                            nombres
                        )
                    binding.autoProducto.setAdapter(adapter)
                    binding.autoProducto.setOnItemClickListener { _, _, position, _ ->
                        productoSeleccionado =
                            productos[position]
                    }
                    if (idInventarioEditar > 0) {
                        seleccionarProductoDelInventario(
                            idProductoEditar
                        )
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@FormInventarioActivity,
                        "No se pudieron cargar los productos.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun cargarInventario() {
        viewModel.obtenerInventarioPorId(
            idInventario = idInventarioEditar,
            onSuccess = { inventario ->
                runOnUiThread {
                    idProductoEditar = inventario.idProducto
                    binding.edtStockActual.setText(inventario.stockActual.toString())
                    binding.edtStockMinimo.setText(inventario.stockMinimo.toString())
                    seleccionarProductoDelInventario(inventario.idProducto)
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

    private fun seleccionarProductoDelInventario(
        idProducto: Int? = null
    ) {
        val idBuscado = idProducto
            ?: productoSeleccionado?.idProducto
            ?: return
        val posicion =
            productos.indexOfFirst {
                it.idProducto == idBuscado
            }
        if (posicion >= 0) {
            productoSeleccionado = productos[posicion]
            binding.edtProducto.setText("${productos[posicion].nombre} - ${productos[posicion].marca}")
        }
    }

    private fun guardarInventario() {
        val producto = productoSeleccionado
        val stockActual = binding.edtStockActual.text
            .toString()
            .toIntOrNull()
        val stockMinimo = binding.edtStockMinimo.text
            .toString()
            .toIntOrNull()
        if (producto == null) {
            Toast.makeText(
                this,
                "Selecciona un producto.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (stockActual == null || stockActual < 0) {
            Toast.makeText(
                this,
                "Ingresa un stock actual válido.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (stockMinimo == null || stockMinimo < 0) {
            Toast.makeText(
                this,
                "Ingresa un stock mínimo válido.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (idInventarioEditar > 0) {
            val request = InventarioUpdateRequest(
                idInventario = idInventarioEditar,
                idProducto = producto.idProducto,
                stockActual = stockActual,
                stockMinimo = stockMinimo
            )
            viewModel.actualizarInventario(
                request = request,
                onSuccess = {
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Inventario actualizado correctamente.",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
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
        } else {
            val request = InventarioCreateRequest(
                idProducto = producto.idProducto,
                stockActual = stockActual,
                stockMinimo = stockMinimo
            )
            viewModel.crearInventario(
                request = request,
                onSuccess = {
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Inventario registrado correctamente.",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
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

    private fun eliminarInventario() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Eliminar inventario")
            .setMessage("¿Estás seguro de eliminar este registro de inventario?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.eliminarInventario(
                    idInventario = idInventarioEditar,
                    onSuccess = {
                        runOnUiThread {
                            Toast.makeText(
                                this,
                                "Inventario eliminado correctamente.",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
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
            }.show()
    }
}