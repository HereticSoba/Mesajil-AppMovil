package com.mesajil.app.ui.categoria

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mesajil.app.adapters.CategoriaAdapter
import com.mesajil.app.adapters.ProductoAdapter
import com.mesajil.app.databinding.FragmentCategoriasBinding
import com.mesajil.app.models.Producto
import com.mesajil.app.models.request.DetalleCarritoRequest
import com.mesajil.app.preferences.SessionManager
import com.mesajil.app.repository.CategoriaRepository
import com.mesajil.app.repository.DetalleCarritoRepository
import com.mesajil.app.repository.ProductoRepository
import com.mesajil.app.ui.producto.DetalleProductoActivity
import kotlinx.coroutines.launch
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mesajil.app.R

class CategoriasFragment : Fragment() {
    private var _binding: FragmentCategoriasBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private val categoriaRepository = CategoriaRepository()
    private val productoRepository = ProductoRepository()
    private val detalleCarritoRepository = DetalleCarritoRepository()
    private var listaProductos: List<Producto> = emptyList()
    private lateinit var productoAdapter: ProductoAdapter
    private var marcaSeleccionada: String? = null
    private var categoriaSeleccionada: Int? = null
    private var precioMinimo: Double? = null
    private var precioMaximo: Double? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoriasBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(requireContext())
        configurarProductos()
        configurarBusqueda()
        binding.btnFiltrar.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                mostrarFiltros()
            }
        }
        return binding.root
    }

    private suspend fun mostrarFiltros() {
        val vista = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_filtro_productos, null)
        val autoMarca =
            vista.findViewById<android.widget.AutoCompleteTextView>(
                R.id.autoMarca
            )
        val autoCategoria =
            vista.findViewById<android.widget.AutoCompleteTextView>(
                R.id.autoCategoria
            )
        val edtPrecioMin =
            vista.findViewById<com.google.android.material.textfield.TextInputEditText>(
                R.id.edtPrecioMin
            )
        val edtPrecioMax =
            vista.findViewById<com.google.android.material.textfield.TextInputEditText>(
                R.id.edtPrecioMax
            )
        val marcas = listaProductos
            .map { it.marca }
            .distinct()
            .sorted()
        val categorias = categoriaRepository
            .obtenerCategorias()
            .filter { it.estado }
        val listaMarcas = mutableListOf("Todas")
        listaMarcas.addAll(marcas)
        val listaCategorias = mutableListOf("Todas")
        listaCategorias.addAll(
            categorias.map { it.nombre }
        )
        autoMarca.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                listaMarcas
            )
        )
        autoCategoria.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                listaCategorias
            )
        )
        autoMarca.setText(
            marcaSeleccionada ?: "Todas",
            false
        )
        val categoriaActual = categorias.indexOfFirst {
            it.idCategoria == categoriaSeleccionada
        }
        autoCategoria.setText(
            if (categoriaActual >= 0) {
                categorias[categoriaActual].nombre
            } else {
                "Todas"
            },
            false
        )
        edtPrecioMin.setText(
            precioMinimo?.toString() ?: ""
        )
        edtPrecioMax.setText(
            precioMaximo?.toString() ?: ""
        )
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Filtrar productos")
            .setView(vista)
            .setNegativeButton("Cancelar", null)
            .setNeutralButton("Limpiar") { _, _ ->
                marcaSeleccionada = null
                categoriaSeleccionada = null
                precioMinimo = null
                precioMaximo = null
                binding.edtBuscar.setText("")
                productoAdapter.actualizarLista(
                    listaProductos
                )
                binding.txtTituloProductos.text =
                    "Todos los productos"
            }
            .setPositiveButton("Aplicar") { _, _ ->
                val marcaTexto =
                    autoMarca.text.toString()
                marcaSeleccionada =
                    if (
                        marcaTexto.isBlank() ||
                        marcaTexto == "Todas"
                    ) {
                        null
                    } else {
                        marcaTexto
                    }
                val categoriaTexto =
                    autoCategoria.text.toString()
                categoriaSeleccionada =
                    categorias.find {
                        it.nombre == categoriaTexto
                    }?.idCategoria
                precioMinimo =
                    edtPrecioMin.text
                        ?.toString()
                        ?.toDoubleOrNull()
                precioMaximo =
                    edtPrecioMax.text
                        ?.toString()
                        ?.toDoubleOrNull()
                aplicarFiltros()
            }
            .show()
    }

    private fun aplicarFiltros() {
        var productosFiltrados = listaProductos
        marcaSeleccionada?.let { marca ->
            productosFiltrados =
                productosFiltrados.filter {
                    it.marca.equals(
                        marca,
                        ignoreCase = true
                    )
                }
        }
        categoriaSeleccionada?.let { idCategoria ->
            productosFiltrados =
                productosFiltrados.filter { it.idCategoria == idCategoria }
        }
        precioMinimo?.let { minimo ->
            productosFiltrados =
                productosFiltrados.filter { it.precio >= minimo }
        }
        precioMaximo?.let { maximo ->
            productosFiltrados =
                productosFiltrados.filter { it.precio <= maximo }
        }
        productoAdapter.actualizarLista(productosFiltrados)
        binding.txtTituloProductos.text = "Resultados: ${productosFiltrados.size}"
    }

    private fun configurarBusqueda() {
        binding.edtBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                val texto = s
                    ?.toString()
                    ?.trim()
                    ?.lowercase()
                    ?: ""
                val productosFiltrados =
                    if (texto.isEmpty()) {
                        listaProductos
                    } else {
                        listaProductos.filter { producto ->
                            producto.nombre.lowercase()
                                .contains(texto) ||
                                    producto.modelo.lowercase()
                                        .contains(texto)
                        }
                    }
                productoAdapter.actualizarLista(productosFiltrados)
                binding.txtTituloProductos.text =
                    if (texto.isEmpty()) {
                        "Productos"
                    } else {
                        "Resultados de búsqueda"
                    }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun configurarProductos() {
        productoAdapter = ProductoAdapter(
            listaProductos = emptyList(),
            onProductoClick = { producto ->
                val intent = Intent(
                    requireContext(),
                    DetalleProductoActivity::class.java
                )
                intent.putExtra(DetalleProductoActivity.EXTRA_ID, producto.idProducto)
                intent.putExtra(DetalleProductoActivity.EXTRA_NOMBRE, producto.nombre)
                intent.putExtra(DetalleProductoActivity.EXTRA_DESCRIPCION, producto.descripcion)
                intent.putExtra(DetalleProductoActivity.EXTRA_PRECIO, producto.precio)
                intent.putExtra(DetalleProductoActivity.EXTRA_STOCK, producto.stock)
                intent.putExtra(DetalleProductoActivity.EXTRA_IMAGEN, producto.urlImagen)
                intent.putExtra(DetalleProductoActivity.EXTRA_MARCA, producto.marca)
                intent.putExtra(DetalleProductoActivity.EXTRA_MODELO, producto.modelo)
                startActivity(intent)
            },
            onAgregarClick = { producto ->
                agregarAlCarrito(producto)
            }
        )
        binding.rvProductosCategoria.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProductosCategoria.adapter = productoAdapter
        cargarDatos()
    }

    private fun cargarDatos() {
        viewLifecycleOwner.lifecycleScope.launch {
            val categorias = categoriaRepository.obtenerCategorias()
                .filter { it.estado }
            listaProductos = productoRepository.obtenerProductos()
            productoAdapter.actualizarLista(listaProductos)
            binding.txtTituloProductos.text = "Todos los productos"
            val categoriaAdapter = CategoriaAdapter(
                categorias = categorias,
                onCategoriaClick = { categoria ->
                    binding.txtTituloProductos.text = "${categoria.nombre}"
                    val productosFiltrados = listaProductos.filter { producto ->
                        producto.idCategoria == categoria.idCategoria
                    }
                    productoAdapter.actualizarLista(productosFiltrados)
                }
            )
            binding.rvCategorias.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            binding.rvCategorias.adapter = categoriaAdapter
        }
    }

    private fun agregarAlCarrito(producto: Producto) {
        viewLifecycleOwner.lifecycleScope.launch {
            if (producto.stock <= 0) {
                Toast.makeText(
                    requireContext(),
                    "Producto sin stock.",
                    Toast.LENGTH_SHORT
                ).show()
                return@launch
            }
            val idCarrito = sessionManager.obtenerIdCarrito()
            if (idCarrito == 0) {
                Toast.makeText(
                    requireContext(),
                    "No se encontró el carrito.",
                    Toast.LENGTH_SHORT
                ).show()
                return@launch
            }
            val existente = detalleCarritoRepository.obtenerPorCarritoYProducto(
                idCarrito,
                producto.idProducto
            )
            if (existente == null) {
                val request = DetalleCarritoRequest(
                    idCarrito = idCarrito,
                    idProducto = producto.idProducto,
                    cantidad = 1,
                    precioUnitario = producto.precio
                )
                val resultado = detalleCarritoRepository.crear(request)
                if (resultado != null) {
                    Toast.makeText(
                        requireContext(),
                        "${producto.nombre} agregado al carrito.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error al agregar el producto.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                if (existente.cantidad >= producto.stock) {
                    Toast.makeText(
                        requireContext(),
                        "Stock máximo alcanzado.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }
                val request = DetalleCarritoRequest(
                    idCarrito = existente.idCarrito,
                    idProducto = existente.idProducto,
                    cantidad = existente.cantidad + 1,
                    precioUnitario = existente.precioUnitario
                )
                val actualizado = detalleCarritoRepository.actualizar(
                    existente.idDetalleCarrito,
                    request
                )
                if (actualizado) {
                    Toast.makeText(
                        requireContext(),
                        "Cantidad actualizada.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error al actualizar el carrito.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}